package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Proyecto;
import com.example.demo.repository.EmpleadoProyectoRepository;
import com.example.demo.repository.ProyectoRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la lógica de negocio relacionada con los proyectos.
 *
 * Contiene las operaciones necesarias para consultar, crear y dar de baja
 * proyectos de la compañía.
 */
@Service
@RequiredArgsConstructor
public class ProyectoService {

    /**
     * Repositorio utilizado para acceder a los datos de los proyectos.
     */
    private final ProyectoRepository proyectoRepository;
    
    /**
     * Repositorio utilizado para acceder a las relaciones entre empleados
     * y proyectos.
     */
    private final EmpleadoProyectoRepository empleadoProyectoRepository;

    /**
     * Obtiene todos los proyectos activos de la compañía.
     *
     * Un proyecto se considera activo cuando no tiene informada la fecha de baja.
     *
     * @return lista de proyectos activos.
     */
    public List<Proyecto> obtenerProyectosActivos() {
        return proyectoRepository.findByFechaBajaIsNull();
    }

    /**
     * Crea un nuevo proyecto en la base de datos.
     *
     * @param proyecto proyecto a guardar.
     * @return proyecto guardado.
     */
    public Proyecto crearProyecto(Proyecto proyecto) {

        if (proyecto.getFechaFin().isBefore(proyecto.getFechaInicio())) {
            throw new IllegalArgumentException(
                    "La fecha de finalización no puede ser anterior a la fecha de inicio");
        }
        
        return proyectoRepository.save(proyecto);
    }

    /**
     * Da de baja un proyecto de la compañía.
     *
     * Si el proyecto no tiene empleados asignados, se informa su fecha
     * de baja con la fecha actual del sistema.
     *
     * Si el proyecto tiene uno o varios empleados asignados, se lanza una
     * excepción con el mensaje correspondiente y no se realiza la baja.
     *
     * @param idProyecto identificador del proyecto a dar de baja.
     * @throws IllegalArgumentException si el proyecto no existe o si tiene
     *                                  empleados asignados.
     */
    public void darDeBajaProyecto(Integer idProyecto) {

        Proyecto proyecto = proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new IllegalArgumentException("No existe el proyecto"));

        boolean tieneEmpleados = empleadoProyectoRepository.existsByProyecto_Id(idProyecto);

        if (tieneEmpleados) {
            throw new IllegalArgumentException(
                    "No se puede dar de baja el proyecto "
                            + proyecto.getDescripcion()
                            + " porque tiene asignado al menos un recurso"
            );
        }

        proyecto.setFechaBaja(LocalDate.now());
        proyectoRepository.save(proyecto);
    }
}
