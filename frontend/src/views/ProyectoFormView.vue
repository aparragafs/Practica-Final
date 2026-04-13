<template>
  <div class="page-container">
    <div class="card form-card">
      <div class="page-title">
        <h1>Alta de proyecto</h1>
        <p>Introduce los datos del nuevo proyecto.</p>
      </div>

      <div v-if="mensajeError" class="alert-error">
        {{ mensajeError }}
      </div>

      <form @submit.prevent="guardarProyecto">
        <div class="form-grid">
          <div class="form-group form-group-large">
            <label>Descripción *</label>
            <input
              v-model="proyecto.descripcion"
              type="text"
              placeholder="Ej. Implantación ERP corporativo"
            />
          </div>

          <div class="form-group">
            <label>Fecha de inicio *</label>
            <input v-model="proyecto.fechaInicio" type="date" />
          </div>

          <div class="form-group">
            <label>Fecha de finalización *</label>
            <input v-model="proyecto.fechaFin" type="date" />
          </div>

          <div class="form-group form-group-large">
            <label>Lugar *</label>
            <input
              v-model="proyecto.lugar"
              type="text"
              placeholder="Ej. Madrid"
            />
          </div>

          <div class="form-group form-group-large">
            <label>Observaciones *</label>
            <textarea
              v-model="proyecto.observaciones"
              rows="4"
              placeholder="Información adicional del proyecto"
            />
          </div>
        </div>

        <div class="actions">
          <button
            type="button"
            class="btn-secondary"
            @click="$router.back()"
          >
            Cancelar
          </button>

          <button type="submit" class="btn-primary">
            Guardar proyecto
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useProyectoStore } from "@/stores/proyectoStore";
import { crearProyecto } from "@/services/proyectoService";
import { obtenerMensajeError } from "@/utils/obtenerMensajeError";

const router = useRouter();
const store = useProyectoStore();

// Mensaje de error mostrado en el formulario cuando alguna
// validación falla o el backend devuelve una incidencia.
const mensajeError = ref("");

// Modelo reactivo asociado al formulario de alta de proyecto.
// El identificador no se informa porque es autoincremental.
const proyecto = reactive({
  descripcion: "",
  fechaInicio: "",
  fechaFin: "",
  lugar: "",
  observaciones: ""
});

// Guarda un nuevo proyecto tras validar los datos introducidos.
async function guardarProyecto() {
  mensajeError.value = "";

  // Todos los campos del formulario son obligatorios.
  if (
    !proyecto.descripcion ||
    !proyecto.fechaInicio ||
    !proyecto.fechaFin ||
    !proyecto.lugar ||
    !proyecto.observaciones
  ) {
    mensajeError.value =
      "Es obligatorio cumplimentar todos los campos del proyecto.";
    return;
  }

  // La fecha de finalización no puede ser anterior a la fecha de inicio.
  if (new Date(proyecto.fechaFin) < new Date(proyecto.fechaInicio)) {
    mensajeError.value =
      "La fecha de finalización no puede ser anterior a la fecha de inicio.";
    return;
  }

  try {
    // Inserta el nuevo proyecto en backend
    await crearProyecto(proyecto);

    // Refresca la lista global para que el nuevo registro aparezca
    await store.refrescarProyectos();

    // Vuelve a la pantalla principal indicando que el alta ha sido correcta
    router.push({
      path: "/proyectos",
      query: { creado: "ok" }
    });

  } catch (error) {
    mensajeError.value = obtenerMensajeError(
      error,
      "No se ha podido crear el proyecto. Revisa los datos introducidos."
    );
  }
}
</script>

<style>
.form-card {
  max-width: 900px;
  margin: 0 auto;
}

.alert-error {
  margin: 20px 0;
  padding: 14px 16px;
  border-radius: 12px;
  background: #fee2e2;
  color: #b91c1c;
  border: 1px solid #fecaca;
  font-weight: 500;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 22px;
  margin-top: 28px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group-large {
  grid-column: span 2;
}

.form-group label {
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #374151;
}

.form-group input {
  padding: 13px 14px;
  border: 1px solid #d1d5db;
  border-radius: 12px;
  font-size: 14px;
  outline: none;
  transition: all 0.2s ease;
}

.form-group input:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.12);
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
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

.btn-secondary:hover {
  background: #f9fafb;
}

@media (max-width: 700px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-group-large {
    grid-column: span 1;
  }

  .actions {
    flex-direction: column;
  }

  .btn-primary,
  .btn-secondary {
    width: 100%;
  }
}
</style>