<template>
  <div class="page-container">
    <div class="card">
      <div class="page-header">
        <div class="page-title">
          <h1>Gestión de empleados</h1>
          <p>
            Consulta los empleados activos de la compañía y gestiona sus datos.
          </p>
          <p class="employee-count">
            {{ store.empleados.length }} empleados activos registrados
          </p>
        </div>

        <button
          class="btn-primary"
          @click="$router.push('/empleados/nuevo')"
        >
          + Alta empleado
        </button>
      </div>

      <div class="table-toolbar">
        <div class="search-box">
          <span class="search-icon">🔍</span>
          <input
            v-model="busqueda"
            type="text"
            placeholder="Buscar por nombre, NIF o email..."
          />
        </div>

        <div class="filters">
          <select v-model="filtroEstadoCivil">
            <option value="">Todos los estados</option>
            <option value="S">Soltero</option>
            <option value="C">Casado</option>
          </select>

          <select v-model="filtroFormacion">
            <option value="">Toda la formación</option>
            <option value="S">Con formación universitaria</option>
            <option value="N">Sin formación universitaria</option>
          </select>
        </div>
      </div>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>NIF</th>
              <th>Nombre</th>
              <th>Primer apellido</th>
              <th>Segundo apellido</th>
              <th>Fecha nacimiento</th>
              <th>Teléfono</th>
              <th>Email</th>
              <th>Estado civil</th>
              <th>Formación</th>
              <th class="acciones">Acciones</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="empleado in empleadosPaginados" :key="empleado.id">
              <td>{{ empleado.nif }}</td>
              <td>{{ empleado.nombre }}</td>
              <td>{{ empleado.apellido1 }}</td>
              <td>{{ empleado.apellido2 }}</td>
              <td>{{ formatearFecha(empleado.fechaNacimiento) }}</td>
              <td>{{ empleado.telefono1 }}</td>
              <td>{{ empleado.email }}</td>

              <td>
                {{ empleado.estadoCivil === "S" ? "Soltero" : "Casado" }}
              </td>

              <td>
                {{ empleado.formacionUniversitaria === "S" ? "Sí" : "No" }}
              </td>

              <td class="acciones">
                <button
                  class="btn-danger"
                  @click="borrarEmpleado(empleado.id)"
                  title="Dar de baja empleado"
                >
                  ✕
                </button>
              </td>
            </tr>

            <tr v-if="store.empleados.length === 0">
              <td colspan="10" class="sin-datos">
                No hay empleados activos registrados.
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
import { useEmpleadoStore } from "@/stores/empleadoStore";
import { eliminarEmpleado } from "@/services/empleadoService";
import { confirmarBajaEmpleado, mostrarExito, mostrarError } from "@/utils/alerts";
import { obtenerMensajeError } from "@/utils/obtenerMensajeError";

const store = useEmpleadoStore();
const route = useRoute();

// Estado de la vista
const busqueda = ref("");
const filtroEstadoCivil = ref("");
const filtroFormacion = ref("");

// Configuración de paginación
const paginaActual = ref(1);
const empleadosPorPagina = 8;

// Carga inicial de empleados y muestra un mensaje temporal
// si se viene desde la pantalla de alta correctamente.
onMounted(async () => {
  await store.cargarEmpleados();

  if (route.query.creado === "ok") {
    await mostrarExito("Empleado creado correctamente");
  }
});

// Formatea las fechas al formato español.
function formatearFecha(fecha) {
  if (!fecha) return "";

  return new Date(fecha).toLocaleDateString("es-ES");
}

// Da de baja un empleado. Si el backend devuelve una restricción
// (por ejemplo, empleado asignado a proyectos), se muestra al usuario.
async function borrarEmpleado(id) {
  const resultado = await confirmarBajaEmpleado();

  if (!resultado.isConfirmed) return;

  try {
    await eliminarEmpleado(id);
    await store.refrescarEmpleados();

    await mostrarExito("Empleado dado de baja correctamente");
  } catch (error) {
    const mensaje = obtenerMensajeError(
      error,
      "No se ha podido dar de baja el empleado."
    );

    await mostrarError(mensaje);
  }
}

// Lista de empleados filtrada por texto, estado civil y formación.
const empleadosFiltrados = computed(() => {
  return store.empleados.filter(empleado => {
    const texto = busqueda.value.toLowerCase();

    const coincideBusqueda =
      empleado.nombre?.toLowerCase().includes(texto) ||
      empleado.apellido1?.toLowerCase().includes(texto) ||
      empleado.apellido2?.toLowerCase().includes(texto) ||
      empleado.nif?.toLowerCase().includes(texto) ||
      empleado.email?.toLowerCase().includes(texto);

    const coincideEstado =
      !filtroEstadoCivil.value ||
      empleado.estadoCivil === filtroEstadoCivil.value;

    const coincideFormacion =
      !filtroFormacion.value ||
      empleado.formacionUniversitaria === filtroFormacion.value;

    return coincideBusqueda && coincideEstado && coincideFormacion;
  });
});

// Número total de páginas a partir de los resultados filtrados.
const totalPaginas = computed(() => {
  return Math.ceil(empleadosFiltrados.value.length / empleadosPorPagina);
});

// Empleados visibles en la página actual.
const empleadosPaginados = computed(() => {
  const inicio = (paginaActual.value - 1) * empleadosPorPagina;
  const fin = inicio + empleadosPorPagina;

  return empleadosFiltrados.value.slice(inicio, fin);
});

// Si cambia cualquier criterio de búsqueda o filtro,
// la paginación vuelve a la primera página.
watch([busqueda, filtroEstadoCivil, filtroFormacion], () => {
  paginaActual.value = 1;
});
</script>

<style>
.page-container {
  max-width: 1550px;
  margin: 0 auto;
  padding: 32px;
}

.card {
  background: #ffffff;
  border-radius: 20px;
  padding: 32px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.08);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 24px;
  margin-bottom: 28px;
}

.page-title h1 {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
  color: #172554;
}

.page-title p {
  margin: 8px 0 0;
  color: #64748b;
  font-size: 15px;
}

.employee-count {
  margin-top: 14px;
  font-weight: 600;
  color: #334155;
}

.btn-primary {
  border: none;
  background: linear-gradient(135deg, #2563eb, #1d4ed8);
  color: white;
  padding: 13px 22px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  white-space: nowrap;
  box-shadow: 0 8px 20px rgba(37, 99, 235, 0.25);
  transition: all 0.2s ease;
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 12px 24px rgba(37, 99, 235, 0.32);
}

.alert-success {
  margin-bottom: 22px;
  padding: 14px 18px;
  border-radius: 12px;
  background: #dcfce7;
  color: #166534;
  border: 1px solid #86efac;
  font-weight: 600;
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.search-box {
  position: relative;
  flex: 1;
  min-width: 320px;
  max-width: 520px;
}

.search-box input {
  width: 100%;
  padding: 13px 16px 13px 44px;
  border: 1px solid #dbe3ec;
  border-radius: 12px;
  background: #ffffff;
  font-size: 14px;
  color: #334155;
  outline: none;
  transition: all 0.2s ease;
}

.search-box input::placeholder {
  color: #94a3b8;
}

.search-box input:focus {
  border-color: #60a5fa;
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.12);
}

.search-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 15px;
  color: #64748b;
}

.filters {
  display: flex;
  gap: 12px;
}

.filters select {
  min-width: 200px;
  padding: 13px 14px;
  border: 1px solid #dbe3ec;
  border-radius: 12px;
  background: #ffffff;
  font-size: 14px;
  color: #334155;
  outline: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.filters select:focus {
  border-color: #60a5fa;
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.12);
}

.table-container {
  overflow-x: auto;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  background: #ffffff;
}

.table-container::-webkit-scrollbar {
  height: 10px;
}

.table-container::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 999px;
}

.table-container::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 999px;
}

.table-container::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.table-container table {
  width: 100%;
  min-width: 1100px;
  border-collapse: separate;
  border-spacing: 0;
}

.table-container thead th {
  position: sticky;
  top: 0;
  z-index: 2;
  background: #23408e;
  color: white;
  font-size: 14px;
  font-weight: 600;
  padding: 16px 18px;
  text-align: left;
  border-bottom: 1px solid #dbe3f1;
}

.table-container thead th:first-child {
  border-top-left-radius: 14px;
}

.table-container thead th:last-child {
  border-top-right-radius: 14px;
}

.table-container tbody td {
  padding: 18px;
  font-size: 14px;
  color: #334155;
  border-bottom: 1px solid #eef2f7;
}

.table-container tbody tr:nth-child(even) {
  background: #fafbfd;
}

.table-container tbody tr:hover {
  background: #eef4ff;
  transition: background 0.18s ease;
}

.acciones {
  width: 90px;
  text-align: center;
}

.btn-danger {
  width: 38px;
  height: 38px;
  border: none;
  border-radius: 12px;
  background: #ef4444;
  color: white;
  font-size: 18px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-danger:hover {
  background: #dc2626;
  transform: scale(1.05);
}

.sin-datos {
  text-align: center;
  padding: 42px;
  color: #64748b;
  font-style: italic;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 18px;
  margin-top: 28px;
}

.pagination-pages {
  display: flex;
  gap: 10px;
}

.pagination-btn,
.pagination-number {
  min-width: 42px;
  height: 42px;
  padding: 0 16px;
  border: 1px solid #e2e8f0;
  border-radius: 999px;
  background: #ffffff;
  color: #475569;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.pagination-btn:hover,
.pagination-number:hover {
  border-color: #93c5fd;
  background: #eff6ff;
  color: #1d4ed8;
}

.pagination-number.active {
  background: #2563eb;
  border-color: #2563eb;
  color: white;
  box-shadow: 0 6px 16px rgba(37, 99, 235, 0.22);
}

.pagination-btn:disabled {
  opacity: 0.45;
  cursor: not-allowed;
  background: #f8fafc;
}

@media (max-width: 900px) {
  .page-container {
    padding: 18px;
  }

  .card {
    padding: 22px;
  }

  .page-header {
    flex-direction: column;
    align-items: stretch;
  }

  .btn-primary {
    width: 100%;
  }

  .table-toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .search-box,
  .filters,
  .filters select {
    width: 100%;
    max-width: none;
  }

  .filters {
    flex-direction: column;
  }

  .pagination {
    flex-wrap: wrap;
  }
}
</style>

