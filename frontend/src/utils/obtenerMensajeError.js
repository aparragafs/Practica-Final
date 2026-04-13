/**
 * Extrae y normaliza el mensaje de error devuelto por el backend
 * para poder mostrarlo de forma consistente en la interfaz.
 */
export function obtenerMensajeError(error, mensajePorDefecto) {

  // Si el backend devuelve un texto simple, se utiliza directamente.
  if (typeof error.response?.data === "string") {
    return error.response.data;
  }

  // Si el backend devuelve varias validaciones en formato objeto,
  // se unen en un único mensaje separado por barras.
  if (typeof error.response?.data === "object") {
    return Object.values(error.response.data).join(" | ");
  }

  // Si existe una propiedad "message", se utiliza como mensaje principal.
  if (error.response?.data?.message) {
    return error.response.data.message;
  }

  // Si no se reconoce el formato del error, se usa el mensaje por defecto.
  return mensajePorDefecto;
}