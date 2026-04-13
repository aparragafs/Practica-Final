/**
 * Servicios encargados de realizar las peticiones HTTP relacionadas
 * con la gestión de empleados.
 */
import api from "./api";

// Obtiene la lista de empleados activos del backend
export async function obtenerEmpleados() {
  const response = await api.get("/empleados");
  return response.data;
}

// Envía al backend los datos de un nuevo empleado
export async function crearEmpleado(empleado) {
  const response = await api.post("/empleados", empleado);
  return response.data;
}

// Da de baja un empleado a partir de su identificador
export async function eliminarEmpleado(id) {
  await api.delete(`/empleados/${id}`);
}