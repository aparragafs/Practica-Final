package com.example.demo.model;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un proyecto de la compañía.
 *
 * Se corresponde con la tabla PR_PROYECTOS de la base de datos.
 * Contiene la información general de cada proyecto.
 */
@Entity
@Table(name = "PR_PROYECTOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

    /**
     * Identificador único del proyecto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROYECTO")
    private Integer id;

    /**
     * Descripción o nombre del proyecto.
     */
    @NotBlank
    @Size(max = 125)
    @Column(name = "TX_DESCRIPCION")
    private String descripcion;

    /**
     * Fecha de inicio del proyecto.
     */
    @NotNull
    @Column(name = "F_INICIO")
    private LocalDate fechaInicio;

    /**
     * Fecha de finalización del proyecto.
     */
    @NotNull
    @Column(name = "F_FIN")
    private LocalDate fechaFin;

    /**
     * Fecha de baja del proyecto.
     * Será null mientras el proyecto permanezca activo.
     */
    @Column(name = "F_BAJA")
    private LocalDate fechaBaja;

    /**
     * Lugar donde se desarrolla el proyecto.
     */
    @Size(max = 30)
    @Column(name = "TX_LUGAR")
    private String lugar;

    /**
     * Observaciones o información adicional del proyecto.
     */
    @Size(max = 300)
    @Column(name = "TX_OBSERVACIONES")
    private String observaciones;
}