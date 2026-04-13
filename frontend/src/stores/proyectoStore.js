import { defineStore } from "pinia";
import { obtenerProyectos } from "@/services/proyectoService";

export const useProyectoStore = defineStore("proyectos", {
  state: () => ({
    proyectos: [],
    cargado: false
  }),

  actions: {
    async cargarProyectos() {
      if (this.cargado) return;

      this.proyectos = await obtenerProyectos();
      this.cargado = true;
    },

    async refrescarProyectos() {
      this.proyectos = await obtenerProyectos();
      this.cargado = true;
    },

  }
});