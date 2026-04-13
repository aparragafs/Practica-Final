/**
 * Servicios encargados de realizar las peticiones HTTP relacionadas
 * con la gestión de la relacion empleados-proyectos.
 */
import api from "./api";

// Obtiene los ids de los empleados asignados a un proyecto
export async function obtenerEmpleadosAsignados(idProyecto) {
  const response = await api.get(`/empleado-proyecto/${idProyecto}`);
  return response.data;
}

// Asigna un empleado a un proyecto
export async function asignarEmpleadoAProyecto(idProyecto, idEmpleado) {
  await api.post(`/empleado-proyecto/${idProyecto}/${idEmpleado}`);
}

// Elimina la asignación de un empleado a un proyecto
export async function desasignarEmpleadoDeProyecto(idProyecto, idEmpleado) {
  await api.delete(`/empleado-proyecto/${idProyecto}/${idEmpleado}`);
}

