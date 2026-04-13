/**
 * Servicios encargados de realizar las peticiones HTTP relacionadas
 * con la gestión de proyectos.
 */
import api from "./api";

// Obtiene la lista de proyectos activos del backend
export async function obtenerProyectos() {
  const response = await api.get("/proyectos");
  return response.data;
}

// Envía al backend los datos de un nuevo proyecto
export async function crearProyecto(proyecto) {
  const response = await api.post("/proyectos", proyecto);
  return response.data;
}

// Da de baja un proyecto a partir de su identificador
export async function eliminarProyecto(id) {
  await api.delete(`/proyectos/${id}`);
}