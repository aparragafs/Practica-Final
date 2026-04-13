package com.example.demo.model;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa a un empleado de la compañía.
 *
 * Se corresponde con la tabla EM_EMPLEADOS de la base de datos.
 * Contiene los datos personales, de contacto y laborales del empleado.
 */
@Entity
@Table(name = "EM_EMPLEADOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    /**
     * Identificador único del empleado.
     */
    @Id
    @Column(name = "ID_EMPLEADO")
    private Integer id;

    /**
     * NIF del empleado.
     */
    @NotBlank
    @Pattern(
        regexp = "^\\d{8}[A-Za-z]$",
        message = "El NIF debe tener 8 números seguidos de una letra"
    )
    @Column(name = "TX_NIF")
    private String nif;

    /**
     * Nombre del empleado.
     */
    @NotBlank
    @Size(max = 30)
    @Column(name = "TX_NOMBRE")
    private String nombre;

    /**
     * Primer apellido del empleado.
     */
    @NotBlank
    @Size(max = 40)
    @Column(name = "TX_APELLIDO1")
    private String apellido1;

    /**
     * Segundo apellido del empleado.
     */
    @NotBlank
    @Size(max = 40)
    @Column(name = "TX_APELLIDO2")
    private String apellido2;

    /**
     * Fecha de nacimiento del empleado.
     */
    @NotNull
    @Column(name = "F_NACIMIENTO")
    private LocalDate fechaNacimiento;

    /**
     * Primer teléfono de contacto del empleado.
     */
    @NotBlank
    @Pattern(
        regexp = "\\d{9}",
        message = "El teléfono 1 debe tener 9 dígitos"
    )
    @Column(name = "N_TELEFONO1")
    private String telefono1;

    /**
     * Segundo teléfono de contacto del empleado.
     */
    @NotBlank
    @Pattern(
        regexp = "\\d{9}",
        message = "El teléfono 2 debe tener 9 dígitos"
    )
    @Column(name = "N_TELEFONO2")
    private String telefono2;

    /**
     * Correo electrónico del empleado.
     */
    @NotBlank
    @Email(message = "El email no tiene un formato válido")
    @Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "El email debe incluir un dominio válido, por ejemplo usuario@gmail.com"
    )
    @Column(name = "TX_EMAIL")
    private String email;

    /**
     * Fecha de alta del empleado en la compañía.
     */
    @NotNull
    @Column(name = "F_ALTA")
    private LocalDate fechaAlta;

    /**
     * Fecha de baja del empleado.
     * Será null mientras el empleado siga activo.
     */
    @Column(name = "F_BAJA")
    private LocalDate fechaBaja;

    /**
     * Estado civil del empleado.
     * Puede tomar los valores:
     * - S: Soltero
     * - C: Casado
     */
    @NotBlank
    @Pattern(regexp = "S|C")
    @Size(min = 1, max = 1)
    @Column(name = "CX_EDOCIVIL")
    private String estadoCivil;

    /**
     * Indica si el empleado tiene formación universitaria.
     * Puede tomar los valores:
     * - S: Sí
     * - N: No
     */
    @NotBlank
    @Pattern(regexp = "S|N")
    @Size(min = 1, max = 1)
    @Column(name = "B_FORMACIONU")
    private String formacionUniversitaria;
}