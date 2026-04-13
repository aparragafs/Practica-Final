package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Empleado;
import com.example.demo.model.EmpleadoProyecto;
import com.example.demo.model.EmpleadoProyectoId;
import com.example.demo.model.Proyecto;
import com.example.demo.repository.EmpleadoProyectoRepository;
import com.example.demo.repository.EmpleadoRepository;
import com.example.demo.repository.ProyectoRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la lógica de negocio relacionada con la asignación
 * de empleados a proyectos.
 *
 * Contiene las operaciones necesarias para asignar y desasignar empleados
 * a los distintos proyectos de la compañía.
 */
@Service
@RequiredArgsConstructor
public class EmpleadoProyectoService {

    /**
     * Repositorio utilizado para acceder a las relaciones entre empleados
     * y proyectos.
     */
    private final EmpleadoProyectoRepository empleadoProyectoRepository;

    /**
     * Repositorio utilizado para acceder a los datos de los empleados.
     */
    private final EmpleadoRepository empleadoRepository;

    /**
     * Repositorio utilizado para acceder a los datos de los proyectos.
     */
    private final ProyectoRepository proyectoRepository;


    /**
     * Obtiene los identificadores de los empleados asignados a un proyecto.
     *
     * @param idProyecto identificador del proyecto.
     * @return lista de ids de empleados asignados al proyecto.
     */
    public List<Integer> obtenerEmpleadosAsignados(Integer idProyecto) {

        return empleadoProyectoRepository.findByProyecto_Id(idProyecto)
                .stream()
                .map(relacion -> relacion.getEmpleado().getId())
                .toList();
    }
    
    /**
     * Asigna un empleado a un proyecto.
     *
     * Si el proyecto o el empleado no existen, se lanza una excepción.
     *
     * Si la relación entre el empleado y el proyecto ya existe, no se realiza
     * ninguna acción.
     *
     * Cuando la asignación es válida, se crea un nuevo registro en la tabla
     * PR_EMPLEADOS_PROYECTO informando además la fecha de alta de la relación.
     *
     * @param idProyecto identificador del proyecto.
     * @param idEmpleado identificador del empleado.
     * @throws IllegalArgumentException si el proyecto o el empleado no existen.
     */
    public void asignarEmpleadoAProyecto(Integer idProyecto, Integer idEmpleado) {

        Proyecto proyecto = proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new IllegalArgumentException("No existe el proyecto"));

        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new IllegalArgumentException("No existe el empleado"));

        EmpleadoProyectoId id = new EmpleadoProyectoId(idProyecto, idEmpleado);

        if (empleadoProyectoRepository.existsById(id)) {
            return;
        }

        EmpleadoProyecto relacion = new EmpleadoProyecto();
        relacion.setId(id);
        relacion.setProyecto(proyecto);
        relacion.setEmpleado(empleado);
        relacion.setFechaAlta(LocalDate.now());

        empleadoProyectoRepository.save(relacion);
    }

    /**
     * Elimina la asignación de un empleado a un proyecto.
     *
     * Si la relación entre el empleado y el proyecto no existe, se lanza
     * una excepción.
     *
     * @param idProyecto identificador del proyecto.
     * @param idEmpleado identificador del empleado.
     * @throws IllegalArgumentException si la asignación no existe.
     */
    public void desasignarEmpleadoDeProyecto(Integer idProyecto, Integer idEmpleado) {

        EmpleadoProyectoId id = new EmpleadoProyectoId(idProyecto, idEmpleado);

        if (!empleadoProyectoRepository.existsById(id)) {
            throw new IllegalArgumentException("La asignación no existe");
        }

        empleadoProyectoRepository.deleteById(id);
    }
}
