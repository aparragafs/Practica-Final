/**
 * Utilidades basadas en SweetAlert2 para mostrar confirmaciones,
 * mensajes de éxito y errores en las distintas vistas de la aplicación.
 */
import Swal from "sweetalert2";

// Muestra una confirmación antes de dar de baja un empleado.
export function confirmarBajaEmpleado() {
  return Swal.fire({
    title: "¿Dar de baja al empleado?",
    text: "Esta acción no se puede deshacer.",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Sí, dar de baja",
    cancelButtonText: "Cancelar",
    confirmButtonColor: "#dc2626",
    cancelButtonColor: "#64748b"
  });
}

// Muestra una confirmación antes de dar de baja un proyecto.
export function confirmarBajaProyecto() {
  return Swal.fire({
    title: "¿Dar de baja el proyecto?",
    text: "Esta acción no se puede deshacer.",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Sí, dar de baja",
    cancelButtonText: "Cancelar",
    confirmButtonColor: "#dc2626",
    cancelButtonColor: "#64748b"
  });
}

// Muestra un mensaje temporal indicando que la operación fue correcta.
export function mostrarExito(mensaje) {
  return Swal.fire({
    icon: "success",
    title: "Operación realizada",
    text: mensaje,
    timer: 2200,
    showConfirmButton: false
  });
}

// Muestra un mensaje de error al usuario.
export function mostrarError(mensaje) {
  return Swal.fire({
    icon: "error",
    title: "Ha ocurrido un error",
    text: mensaje,
    confirmButtonText: "Aceptar"
  });
}

