package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.model.Empleado;
import com.example.demo.model.EmpleadoProyecto;
import com.example.demo.repository.EmpleadoProyectoRepository;
import com.example.demo.repository.EmpleadoRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la lógica de negocio relacionada con los empleados.
 *
 * Contiene las operaciones necesarias para consultar, crear y dar de baja
 * empleados de la compañía.
 */
@Service
@RequiredArgsConstructor
public class EmpleadoService {

    /**
     * Repositorio utilizado para acceder a los datos de los empleados.
     */
    private final EmpleadoRepository empleadoRepository;

   /**
     * Repositorio utilizado para acceder a las relaciones entre empleados
     * y proyectos.
     */
    private final EmpleadoProyectoRepository empleadoProyectoRepository;

    /**
     * Obtiene todos los empleados activos de la compañía.
     *
     * Un empleado se considera activo cuando no tiene informada la fecha de baja.
     *
     * @return lista de empleados activos.
     */
    public List<Empleado> obtenerEmpleadosActivos() {
        return empleadoRepository.findByFechaBajaIsNull();
    }

    /**
     * Crea un nuevo empleado en la base de datos.
     *
     * Antes de guardar el empleado, se valida que no exista ya otro
     * empleado con el mismo identificador, NIF o dirección de correo
     * electrónico.
     *
     * @param empleado empleado a guardar.
     * @return empleado guardado.
     * @throws IllegalArgumentException si ya existe un empleado con el mismo
     *                                  identificador, NIF o email.
     */
    public Empleado crearEmpleado(Empleado empleado) {

        if (empleadoRepository.existsById(empleado.getId())) {
                throw new IllegalArgumentException(
                        "Ya existe un empleado con el identificador " + empleado.getId());
        }

        if (empleadoRepository.existsByNif(empleado.getNif())) {
                throw new IllegalArgumentException(
                        "Ya existe un empleado con el NIF " + empleado.getNif());
        }

        if (empleadoRepository.existsByEmail(empleado.getEmail())) {
                throw new IllegalArgumentException(
                        "Ya existe un empleado con el email " + empleado.getEmail());
        }
        
        if (empleado.getFechaNacimiento().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "La fecha de nacimiento no puede ser futura");
        }

        if (empleado.getFechaAlta().isBefore(empleado.getFechaNacimiento())) {
                throw new IllegalArgumentException(
                        "La fecha de alta no puede ser anterior a la fecha de nacimiento");
        }

        if (empleado.getFechaNacimiento().plusYears(18).isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "El empleado debe ser mayor de edad");
        }

                return empleadoRepository.save(empleado);
    }

    /**
     * Da de baja un empleado de la compañía.
     *
     * Si el empleado no está asignado a ningún proyecto, se informa su fecha
     * de baja con la fecha actual del sistema.
     *
     * Si el empleado está asignado a uno o varios proyectos, se lanza una
     * excepción con el mensaje correspondiente y no se realiza la baja.
     *
     * @param idEmpleado identificador del empleado a dar de baja.
     * @throws IllegalArgumentException si el empleado no existe o si está
     *                                  asignado a uno o varios proyectos.
     */
    public void darDeBajaEmpleado(Integer idEmpleado) {

        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new IllegalArgumentException("No existe el empleado"));

        List<EmpleadoProyecto> relaciones =
                empleadoProyectoRepository.findByEmpleado_Id(idEmpleado);

        if (!relaciones.isEmpty()) {

            String proyectos = relaciones.stream()
                    .map(relacion -> relacion.getProyecto().getDescripcion())
                    .collect(Collectors.joining(", "));

            throw new IllegalArgumentException(
                    "No se puede dar de baja al empleado "
                            + empleado.getNombre() + " "
                            + empleado.getApellido1() + " "
                            + empleado.getApellido2()
                            + " porque está asignado a el/los proyecto/s "
                            + proyectos
            );
        }

        empleado.setFechaBaja(LocalDate.now());
        empleadoRepository.save(empleado);
    }
}