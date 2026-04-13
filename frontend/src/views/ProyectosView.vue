<!-- src/views/ProyectosView.vue -->
<template>
  <div class="page-container">
    <div class="card">
      <div class="page-header">
        <div class="page-title">
          <h1>Gestión de proyectos</h1>
          <p>
            Consulta los proyectos registrados y administra su información.
          </p>
          <p class="employee-count">
            {{ store.proyectos.length }} proyectos registrados
          </p>
        </div>

        <button
          class="btn-primary"
          @click="$router.push('/proyectos/nuevo')"
        >
          + Nuevo proyecto
        </button>
      </div>

      <div class="table-toolbar">
        <div class="search-box">
          <span class="search-icon">🔍</span>
          <input
            v-model="busqueda"
            type="text"
            placeholder="Buscar por descripción o lugar..."
          />
        </div>

        <div class="filters">
          <select v-model="filtroEstado">
            <option value="">Todos los proyectos</option>
            <option value="activos">Activos</option>
            <option value="finalizados">Finalizados</option>
          </select>
        </div>
      </div>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Descripción</th>
              <th>Fecha inicio</th>
              <th>Fecha fin</th>
              <th>Lugar</th>
              <th class="acciones">Acciones</th>
            </tr>
          </thead>

          <tbody>
            <tr
              v-for="proyecto in proyectosPaginados"
              :key="proyecto.id"
            >
              <td>{{ proyecto.id }}</td>

              <td class="project-name">
                {{ proyecto.descripcion }}
              </td>

              <td>{{ formatearFecha(proyecto.fechaInicio) }}</td>

              <td>
                {{ proyecto.fechaFin
                  ? formatearFecha(proyecto.fechaFin)
                  : "En curso" }}
              </td>

              <td>{{ proyecto.lugar }}</td>

              <td class="acciones">
                <button
                  class="btn-danger"
                  @click="borrarProyecto(proyecto.id)"
                  title="Eliminar proyecto"
                >
                  ✕
                </button>
              </td>
            </tr>

            <tr v-if="proyectosFiltrados.length === 0">
              <td colspan="6" class="sin-datos">
                No hay proyectos que coincidan con los filtros seleccionados.
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-if="totalPaginas > 1" class="pagination">
        <button
          class="pagination-btn"
          :disabled="paginaActual === 1"
          @click="paginaActual--"
        >
          ← Anterior
        </button>

        <div class="pagination-pages">
          <button
            v-for="pagina in totalPaginas"
            :key="pagina"
            class="pagination-number"
            :class="{ active: pagina === paginaActual }"
            @click="paginaActual = pagina"
          >
            {{ pagina }}
          </button>
        </div>

        <button
          class="pagination-btn"
          :disabled="paginaActual === totalPaginas"
          @click="paginaActual++"
        >
          Siguiente →
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed, watch } from "vue";
import { useRoute } from "vue-router";
import { useProyectoStore } from "@/stores/proyectoStore";
import { eliminarProyecto } from "@/services/proyectoService";
import { confirmarBajaProyecto, mostrarExito, mostrarError } from "@/utils/alerts";
import { obtenerMensajeError } from "@/utils/obtenerMensajeError";

const store = useProyectoStore();
const route = useRoute();

// Estado de la vista
const busqueda = ref("");
const filtroEstado = ref("");

// Configuración de paginación
const paginaActual = ref(1);
const proyectosPorPagina = 8;

// Carga inicial de proyectos y muestra un mensaje temporal
// si se viene desde la pantalla de alta correctamente.
onMounted(async () => {
  await store.cargarProyectos();

  if (route.query.creado === "ok") {
   await mostrarExito( "El proyecto se ha creado correctamente.", "Proyecto creado" );
  }
});

// Formatea las fechas al formato habitual español.
function formatearFecha(fecha) {
  if (!fecha) return "";

  return new Date(fecha).toLocaleDateString("es-ES");
}

// Da de baja un proyecto. Si el backend devuelve una restricción
// (por ejemplo, proyecto con empleados asignados), se muestra al usuario.
async function borrarProyecto(id) {
  const resultado = await confirmarBajaProyecto();

  if (!resultado.isConfirmed) return;

  try {
    await eliminarProyecto(id);
    await store.refrescarProyectos();

    await mostrarExito(
      "El proyecto se ha dado de baja correctamente.",
      "Proyecto dado de baja"
    );
  } catch (error) {
    const mensaje = obtenerMensajeError(
      error,
      "No se ha podido dar de baja el proyecto."
    );

    await mostrarError(
      mensaje,
      "No se puede dar de baja"
    );
  }
}

// Lista de proyectos filtrada por texto y estado
// (activos o finalizados según la fecha de fin).
const proyectosFiltrados = computed(() => {
  return store.proyectos.filter(proyecto => {
    const texto = busqueda.value.toLowerCase();

    const coincideBusqueda =
      proyecto.descripcion?.toLowerCase().includes(texto) ||
      proyecto.lugar?.toLowerCase().includes(texto);

    const hoy = new Date();

    const estaActivo =
      !proyecto.fechaFin || new Date(proyecto.fechaFin) >= hoy;

    const coincideEstado =
      filtroEstado.value === "" ||
      (filtroEstado.value === "activos" && estaActivo) ||
      (filtroEstado.value === "finalizados" && !estaActivo);

    return coincideBusqueda && coincideEstado;
  });
});

// Número total de páginas en función de los resultados filtrados.
const totalPaginas = computed(() => {
  return Math.ceil(proyectosFiltrados.value.length / proyectosPorPagina);
});

// Proyectos visibles en la página actual.
const proyectosPaginados = computed(() => {
  const inicio = (paginaActual.value - 1) * proyectosPorPagina;
  const fin = inicio + proyectosPorPagina;

  return proyectosFiltrados.value.slice(inicio, fin);
});

// Si cambia la búsqueda o el filtro,
// la paginación vuelve automáticamente a la primera página.
watch([busqueda, filtroEstado], () => {
  paginaActual.value = 1;
});
</script>

