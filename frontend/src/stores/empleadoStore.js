import { defineStore } from "pinia";
import { obtenerEmpleados } from "@/services/empleadoService";

export const useEmpleadoStore = defineStore("empleados", {
  state: () => ({
    empleados: [],
    cargado: false
  }),

  actions: {
    async cargarEmpleados() {
      if (this.cargado) return;

      this.empleados = await obtenerEmpleados();
      this.cargado = true;
    },

    async refrescarEmpleados() {
      this.empleados = await obtenerEmpleados();
      this.cargado = true;
    }
  }
});