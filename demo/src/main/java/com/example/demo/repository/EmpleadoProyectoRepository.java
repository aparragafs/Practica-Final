package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.EmpleadoProyecto;
import com.example.demo.model.EmpleadoProyectoId;

/**
 * Repositorio encargado de acceder a los datos de la entidad EmpleadoProyecto.
 *
 * Gestiona la relación entre empleados y proyectos, permitiendo consultar
 * las asignaciones existentes entre ambas entidades.
 */
@Repository
public interface EmpleadoProyectoRepository
        extends JpaRepository<EmpleadoProyecto, EmpleadoProyectoId> {

    /**
     * Obtiene todas las asignaciones de un empleado concreto.
     *
     * @param idEmpleado identificador del empleado.
     * @return lista de relaciones entre el empleado y sus proyectos.
     */
    List<EmpleadoProyecto> findByEmpleado_Id(Integer idEmpleado);

    /**
     * Obtiene todas las asignaciones de un proyecto concreto.
     *
     * @param idProyecto identificador del proyecto.
     * @return lista de relaciones entre el proyecto y sus empleados.
     */
    List<EmpleadoProyecto> findByProyecto_Id(Integer idProyecto);

    /**
     * Comprueba si un empleado está asignado a al menos un proyecto.
     *
     * @param idEmpleado identificador del empleado.
     * @return true si el empleado tiene proyectos asignados, false en caso contrario.
     */
    boolean existsByEmpleado_Id(Integer idEmpleado);

    /**
     * Comprueba si un proyecto tiene al menos un empleado asignado.
     *
     * @param idProyecto identificador del proyecto.
     * @return true si el proyecto tiene empleados asignados, false en caso contrario.
     */
    boolean existsByProyecto_Id(Integer idProyecto);
}