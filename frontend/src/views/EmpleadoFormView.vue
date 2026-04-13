<template>
  <div class="page-container">
    <div class="card form-card">
      <div class="page-title">
        <h1>Alta de empleado</h1>
        <p>Introduce los datos del nuevo empleado.</p>
      </div>

      <div v-if="mensajeError" class="alert-error">
        {{ mensajeError }}
      </div>

      <form @submit.prevent="guardarEmpleado">
        <div class="form-grid">
          <div class="form-group">
            <label>ID</label>
            <input v-model="empleado.id" type="number" />
          </div>

          <div class="form-group">
            <label>NIF</label>
            <input v-model="empleado.nif" type="text" />
          </div>

          <div class="form-group">
            <label>Nombre</label>
            <input v-model="empleado.nombre" type="text" />
          </div>

          <div class="form-group">
            <label>Primer apellido</label>
            <input v-model="empleado.apellido1" type="text" />
          </div>

          <div class="form-group">
            <label>Segundo apellido</label>
            <input v-model="empleado.apellido2" type="text" />
          </div>

          <div class="form-group">
            <label>Fecha nacimiento</label>
            <input v-model="empleado.fechaNacimiento" type="date" />
          </div>

          <div class="form-group">
            <label>Fecha alta</label>
            <input v-model="empleado.fechaAlta" type="date" />
          </div>

          <div class="form-group">
            <label>Teléfono 1</label>
            <input v-model="empleado.telefono1" type="text" />
          </div>

          <div class="form-group">
            <label>Teléfono 2</label>
            <input v-model="empleado.telefono2" type="text" />
          </div>

          <div class="form-group">
            <label>Email</label>
            <input v-model="empleado.email" type="email" />
          </div>

          <div class="form-group">
            <label>Estado civil</label>
            <select v-model="empleado.estadoCivil">
              <option value="">Selecciona una opción</option>
              <option value="S">Soltero</option>
              <option value="C">Casado</option>
            </select>
          </div>

          <div class="form-group">
            <label>Formación universitaria</label>
            <select v-model="empleado.formacionUniversitaria">
              <option value="">Selecciona una opción</option>
              <option value="S">Sí</option>
              <option value="N">No</option>
            </select>
          </div>
        </div>

        <div class="actions">
          <button type="button" class="btn-secondary" @click="$router.back()">
            Cancelar
          </button>

          <button type="submit" class="btn-primary">
            Guardar empleado
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { crearEmpleado } from "@/services/empleadoService";
import { useEmpleadoStore } from "@/stores/empleadoStore";
import { obtenerMensajeError } from "@/utils/obtenerMensajeError";

const router = useRouter();
const store = useEmpleadoStore();

// Modelo reactivo asociado al formulario de alta.
// Contiene todos los campos obligatorios del empleado.
const empleado = reactive({
  id: "",
  nif: "",
  nombre: "",
  apellido1: "",
  apellido2: "",
  fechaNacimiento: "",
  fechaAlta: "",
  telefono1: "",
  telefono2: "",
  email: "",
  estadoCivil: "",
  formacionUniversitaria: ""
});

// Mensaje de error mostrado en la parte superior del formulario
// cuando alguna validación falla.
const mensajeError = ref("");

// Guarda un nuevo empleado tras validar los datos introducidos.
async function guardarEmpleado() {
  mensajeError.value = "";

  const campos = Object.values(empleado);

  // Todos los campos del formulario son obligatorios.
  // Si alguno está vacío, se cancela el alta.
  if (campos.some(valor => valor === "" || valor === null)) {
    mensajeError.value =
      "Es obligatorio introducir todos los datos para dar de alta un nuevo empleado";
    return;
  }

  // El NIF debe tener el formato 8 números y una letra.
  const nifRegex = /^\d{8}[A-Za-z]$/;
  empleado.nif = empleado.nif.toUpperCase();

  if (!nifRegex.test(empleado.nif)) {
    mensajeError.value =
      "El NIF debe tener 8 números seguidos de una letra";
    return;
  }

  const telefonoRegex = /^\d{9}$/;

  if (!telefonoRegex.test(empleado.telefono1)) {
    mensajeError.value =
      "El teléfono 1 debe tener 9 dígitos";
    return;
  }

  if (!telefonoRegex.test(empleado.telefono2)) {
    mensajeError.value =
      "El teléfono 2 debe tener 9 dígitos";
    return;
  }

  const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

  if (!emailRegex.test(empleado.email)) {
    mensajeError.value =
      "El email debe tener un formato válido";
    return;
  }

  if (new Date(empleado.fechaNacimiento) > new Date()) {
    mensajeError.value =
      "La fecha de nacimiento no puede ser posterior a hoy";
    return;
  }

  if (
    new Date(empleado.fechaAlta) <
    new Date(empleado.fechaNacimiento)
  ) {
    mensajeError.value =
      "La fecha de alta no puede ser anterior a la fecha de nacimiento";
    return;
  }

  try {
    // Inserta el nuevo empleado en backend
    await crearEmpleado(empleado);

    // Refresca la lista global para que el nuevo registro aparezca
    await store.refrescarEmpleados();

    // Vuelve a la pantalla principal indicando que el alta ha sido correcta
    router.push({
      path: "/empleados",
      query: { creado: "ok" }
    });

  } catch (error) {
    mensajeError.value = obtenerMensajeError(
      error,
      "No se ha podido crear el empleado. Revisa los datos introducidos."
    );
  }
}
</script>

<style>
.form-card {
  max-width: 1100px;
  margin: 0 auto;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 22px;
  margin-top: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #374151;
}

.form-group input,
.form-group select {
  border: 1px solid #d1d5db;
  border-radius: 10px;
  padding: 12px 14px;
  font-size: 14px;
  background: white;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 14px;
  margin-top: 32px;
}

.btn-secondary {
  border: 1px solid #d1d5db;
  background: white;
  color: #374151;
  padding: 12px 18px;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
}

.alert-error {
  margin-top: 20px;
  padding: 14px 16px;
  border-radius: 10px;
  background: #fee2e2;
  color: #991b1b;
  border: 1px solid #fecaca;
}

@media (max-width: 900px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .actions {
    flex-direction: column;
  }
}
</style>