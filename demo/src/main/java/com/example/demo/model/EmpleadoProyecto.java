package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa la asignación de un empleado a un proyecto.
 *
 * Se corresponde con la tabla PR_EMPLEADOS_PROYECTO de la base de datos.
 * Esta entidad modela la relación muchos a muchos entre empleados y proyectos,
 * almacenando además la fecha en la que el empleado fue asignado al proyecto.
 */
@Entity
@Table(name = "PR_EMPLEADOS_PROYECTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoProyecto {

    /**
     * Clave compuesta formada por el identificador del proyecto
     * y el identificador del empleado.
     */
    @EmbeddedId
    private EmpleadoProyectoId id;

    /**
     * Proyecto al que está asignado el empleado.
     */
    @ManyToOne
    @MapsId("idProyecto")
    @JoinColumn(name = "ID_PROYECTO")
    private Proyecto proyecto;

    /**
     * Empleado asignado al proyecto.
     */
    @ManyToOne
    @MapsId("idEmpleado")
    @JoinColumn(name = "ID_EMPLEADO")
    private Empleado empleado;

    /**
     * Fecha en la que el empleado fue asignado al proyecto.
     */
    @Column(name = "F_ALTA")
    private LocalDate fechaAlta;
}