package com.example.demo.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

/**
 * Clase que representa la clave primaria compuesta de la entidad EmpleadoProyecto.
 *
 * Está formada por:
 * - El identificador del proyecto.
 * - El identificador del empleado.
 *
 * Se utiliza como clave embebida en la tabla PR_EMPLEADOS_PROYECTO.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmpleadoProyectoId implements Serializable {

    /**
     * Identificador del proyecto.
     */
    @Column(name = "ID_PROYECTO")
    private Integer idProyecto;

    /**
     * Identificador del empleado.
     */
    @Column(name = "ID_EMPLEADO")
    private Integer idEmpleado;
}