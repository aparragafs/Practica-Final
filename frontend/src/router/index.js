import { createRouter, createWebHistory } from 'vue-router'
import EmpleadosView from "../views/EmpleadosView.vue";
import ProyectosView from "../views/ProyectosView.vue";
import AsignacionesView from "../views/AsignacionesView.vue";
import EmpleadoFormView from "@/views/EmpleadoFormView.vue";
import ProyectoFormView from "@/views/ProyectoFormView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: "/", redirect: "/empleados" },
    { path: "/empleados", component: EmpleadosView },
    { path: "/proyectos", component: ProyectosView },
    { path: "/asignaciones", component: AsignacionesView },
    { path: "/empleados/nuevo", component: EmpleadoFormView },
    { path: "/proyectos/nuevo", component: ProyectoFormView }
  ],
})

export default router
