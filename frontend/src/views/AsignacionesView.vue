<template>
  <div class="page-container">
    <div class="card">
      <div class="page-header">
        <div class="page-title">
          <h1>Asignación de empleados a proyectos</h1>
          <p>Selecciona un proyecto y marca los empleados asignados.</p>
        </div>

        <button class="btn btn-secondary" @click="$router.push('/')">
          Volver al menú
        </button>
      </div>

      <div class="selector-container">
        <label>Proyecto</label>

        <div class="project-selector">
          <div class="search-box">
            <span class="search-icon">🔍</span>

            <input
              v-model="busquedaProyecto"
              type="text"
              placeholder="Selecciona o busca un proyecto..."
              @focus="mostrarLista = true"
            />
          </div>

          <div
            v-if="mostrarLista"
            class="project-dropdown"
          >
            <button
              v-for="proyecto in proyectosFiltrados"
              :key="proyecto.id"
              type="button"
              class="project-option"
              @click="seleccionarProyecto(proyecto)"
            >
              {{ proyecto.descripcion }}
            </button>

            <div
              v-if="proyectosFiltrados.length === 0"
              class="project-empty"
            >
              No hay proyectos que coincidan.
            </div>
          </div>
        </div>
      </div>

      <div v-if="proyectoSeleccionado" class="empleados-container">
        <div class="empleados-header">
          <h2>Empleados activos</h2>
          <span>{{ empleadosAsignados.length }} asignados</span>
        </div>

        <div class="table-toolbar">
          <div class="search-box">
            <span class="search-icon">🔍</span>

            <input
              v-model="busqueda"
              type="text"
              placeholder="Buscar por nombre, apellido o email..."
            />
          </div>

          <div class="filters">
            <select v-model="filtroAsignacion">
              <option value="">Todos</option>
              <option value="asignados">Solo asignados</option>
              <option value="sinAsignar">Solo sin asignar</option>
            </select>
          </div>
        </div>

        <div v-if="cargando" class="loading">
          Cargando empleados...
        </div>
        
        
        <table v-else class="table-container">
          <thead>
            <tr>
              <th>Asignado</th>
              <th>Nombre</th>
              <th>Primer apellido</th>
              <th>Segundo apellido</th>
              <th>Email</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="empleado in empleadosPaginados" :key="empleado.id">
              <td class="check-column">
                <input
                  type="checkbox"
                  :checked="empleadosAsignados.includes(empleado.id)"
                  @change="toggleEmpleado(empleado.id, $event.target.checked)"
                />
              </td>

              <td>{{ empleado.nombre }}</td>
              <td>{{ empleado.apellido1 }}</td>
              <td>{{ empleado.apellido2 }}</td>
              <td>{{ empleado.email }}</td>
            </tr>

            <tr v-if="empleadosFiltrados.length === 0">
              <td colspan="5" class="sin-datos">
                No hay empleados activos.
              </td>
            </tr>
          </tbody>
        </table>
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
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from "vue";

import { useProyectoStore } from '@/stores/proyectoStore'
import { useEmpleadoStore } from '@/stores/empleadoStore'

import { mostrarExito, mostrarError } from '@/utils/alerts'
import { obtenerMensajeError } from "@/utils/obtenerMensajeError";

import {
  obtenerEmpleadosAsignados,
  asignarEmpleadoAProyecto,
  desasignarEmpleadoDeProyecto
} from "@/services/empleadoProyectoService";



const proyectoStore = useProyectoStore()
const empleadoStore = useEmpleadoStore()

// Estado de la vista
const proyectoSeleccionado = ref('')
const empleadosAsignados = ref([])
const cargando = ref(false)
const busqueda = ref("");
const paginaActual = ref(1);
const empleadosPorPagina = 8;
const filtroAsignacion = ref("");
const busquedaProyecto = ref("");
const mostrarLista = ref(false);

const proyectos = computed(() => proyectoStore.proyectos)
const empleados = computed(() => empleadoStore.empleados)

// Carga inicial de proyectos y empleados
async function cargarDatosIniciales() {
  try {
    await Promise.all([
      proyectoStore.cargarProyectos(),
      empleadoStore.cargarEmpleados()
    ])
  } catch (error) {
    await mostrarError( 'No se han podido cargar los proyectos y empleados.', 'Error al cargar datos' )
  }
}

// Carga los empleados asignados al proyecto seleccionado. 
// Si no hay proyecto seleccionado, se vacía la lista de asignaciones
async function cargarAsignaciones() {
  if (!proyectoSeleccionado.value) {
    empleadosAsignados.value = []
    return
  }

  cargando.value = true

  try {
    
    empleadosAsignados.value = await obtenerEmpleadosAsignados(
      proyectoSeleccionado.value
    );


  } catch (error) {
    empleadosAsignados.value = []

    await mostrarError( 'No se han podido cargar las asignaciones del proyecto seleccionado.', 'Error al cargar asignaciones' )
  } finally {
    cargando.value = false
  }
}

// Asigna o desasigna un empleado al proyecto seleccionado. 
// Si el checkbox se marca, se crea la relación. 
// Si se desmarca, se elimina la relación existente.
async function toggleEmpleado(idEmpleado, marcado) {
  try {
    if (marcado) {
      
      await asignarEmpleadoAProyecto(
        proyectoSeleccionado.value,
        idEmpleado
      );

      if (!empleadosAsignados.value.includes(idEmpleado)) {
        empleadosAsignados.value.push(idEmpleado)
      }
      await mostrarExito( 'El empleado se ha asignado correctamente al proyecto.', 'Empleado asignado' )

    } else {
     
        await desasignarEmpleadoDeProyecto(
          proyectoSeleccionado.value,
          idEmpleado
        );
          
        empleadosAsignados.value = empleadosAsignados.value.filter(
          id => id !== idEmpleado
        )

        await mostrarExito( 'El empleado se ha desasignado correctamente del proyecto.', 'Empleado desasignado' )
    }
  } catch (error) {
    const mensaje = obtenerMensajeError(
      error,
      "No se ha podido actualizar la asignación."
    );

    await mostrarError(
      mensaje,
      "No se ha podido actualizar la asignación"
    );

    await cargarAsignaciones();
  }
}


// Calcula el número total de páginas necesarias a partir
// del número de empleados filtrados
const empleadosFiltrados = computed(() => {
  const texto = busqueda.value.toLowerCase();

  return empleados.value.filter(empleado => {
    const coincideBusqueda =
      empleado.nombre?.toLowerCase().includes(texto) ||
      empleado.apellido1?.toLowerCase().includes(texto) ||
      empleado.apellido2?.toLowerCase().includes(texto) ||
      empleado.email?.toLowerCase().includes(texto);

    const estaAsignado = empleadosAsignados.value.includes(empleado.id);

    const coincideAsignacion =
      filtroAsignacion.value === "" ||
      (filtroAsignacion.value === "asignados" && estaAsignado) ||
      (filtroAsignacion.value === "sinAsignar" && !estaAsignado);

    return coincideBusqueda && coincideAsignacion;
  });
});


// Devuelve únicamente los empleados correspondientes
// a la página actualmente seleccionada.
const totalPaginas = computed(() => {
  return Math.ceil(
    empleadosFiltrados.value.length / empleadosPorPagina
  );
});

// Si cambia el texto de búsqueda, el proyecto o el filtro,
// la paginación vuelve automáticamente a la primera página.
const empleadosPaginados = computed(() => {
  const inicio = (paginaActual.value - 1) * empleadosPorPagina;
  const fin = inicio + empleadosPorPagina;

  return empleadosFiltrados.value.slice(inicio, fin);
});


// Filtra la lista de proyectos a partir del texto introducido
// en el buscador del selector de proyectos.
watch([busqueda, proyectoSeleccionado, filtroAsignacion], () => {
  paginaActual.value = 1;
});


// Filtra la lista de proyectos a partir del texto introducido
// en el buscador del selector de proyectos.
const proyectosFiltrados = computed(() => {
  const texto = busquedaProyecto.value.toLowerCase();

  return proyectos.value.filter(proyecto =>
    proyecto.descripcion?.toLowerCase().includes(texto)
  );
});

// Selecciona un proyecto desde el desplegable, actualiza
// el texto visible en el buscador y carga sus asignaciones.
function seleccionarProyecto(proyecto) {
  proyectoSeleccionado.value = proyecto.id;
  busquedaProyecto.value = proyecto.descripcion;
  mostrarLista.value = false;
  cargarAsignaciones();
}

// Carga los datos iniciales de la pantalla al montarse la vista.
onMounted(async () => {
  await cargarDatosIniciales()
})
</script>

<style scoped>

.selector-container {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 2rem;
}

.selector-container label {
  font-weight: 600;
  color: #334155;
}

.selector-container select {
  max-width: 400px;
  padding: 0.85rem 1rem;
  border-radius: 12px;
  border: 1px solid #cbd5e1;
  font-size: 1rem;
}

.empleados-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.empleados-header h2 {
  margin: 0;
}

.empleados-header span {
  background: #dbeafe;
  color: #1d4ed8;
  padding: 0.35rem 0.8rem;
  border-radius: 999px;
  font-size: 0.9rem;
  font-weight: 600;
}



.check-column {
  width: 100px;
  text-align: center;
}

.check-column input {
  width: 18px;
  height: 18px;
  cursor: pointer;
}



.loading {
  padding: 2rem;
  text-align: center;
  color: #64748b;
}

.btn {
  border: none;
  border-radius: 12px;
  padding: 0.8rem 1.2rem;
  font-size: 0.95rem;
  cursor: pointer;
  transition: 0.2s;
}

.btn-secondary {
  background: #e2e8f0;
  color: #1e293b;
}

.btn-secondary:hover {
  background: #cbd5e1;
}

.project-selector {
  position: relative;
  max-width: 520px;
}

.project-dropdown {
  position: absolute;
  top: calc(100% + 6px);
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #dbe3ec;
  border-radius: 12px;
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.12);
  max-height: 260px;
  overflow-y: auto;
  z-index: 20;
}

.project-option {
  width: 100%;
  border: none;
  background: white;
  text-align: left;
  padding: 13px 16px;
  cursor: pointer;
  font-size: 14px;
  color: #334155;
}

.project-option:hover {
  background: #eff6ff;
}

.project-empty {
  padding: 14px 16px;
  color: #64748b;
  font-size: 14px;
}


</style>

