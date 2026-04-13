package com.example.demo.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.EmpleadoProyectoService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con la asignación de empleados a proyectos.
 *
 * Expone los endpoints del recurso /api/empleado-proyecto.
 */
@RestController
@RequestMapping("/api/empleado-proyecto")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class EmpleadoProyectoController {

    /**
     * Servicio encargado de la lógica de negocio de la relación
     * entre empleados y proyectos.
     */
    private final EmpleadoProyectoService empleadoProyectoService;


    /**
     * Obtiene los identificadores de los empleados asignados a un proyecto.
     *
     * @param idProyecto identificador del proyecto.
     * @return respuesta HTTP 200 con la lista de ids de empleados asignados.
     */
    @GetMapping("/{idProyecto}")
    public ResponseEntity<List<Integer>> obtenerEmpleadosAsignados(
            @PathVariable Integer idProyecto) {

        List<Integer> empleados = empleadoProyectoService.obtenerEmpleadosAsignados(idProyecto);
        return ResponseEntity.ok(empleados);
    }
    
    /**
     * Asigna un empleado a un proyecto.
     *
     * @param idProyecto identificador del proyecto.
     * @param idEmpleado identificador del empleado.
     * @return respuesta HTTP 200 si la asignación se realiza correctamente.
     */
    @PostMapping("/{idProyecto}/{idEmpleado}")
    public ResponseEntity<Void> asignarEmpleado(
            @PathVariable Integer idProyecto,
            @PathVariable Integer idEmpleado) {

        empleadoProyectoService.asignarEmpleadoAProyecto(idProyecto, idEmpleado);
        return ResponseEntity.ok().build();
    }

    /**
     * Elimina la asignación de un empleado a un proyecto.
     *
     * @param idProyecto identificador del proyecto.
     * @param idEmpleado identificador del empleado.
     * @return respuesta HTTP 200 si la asignación se elimina correctamente.
     */
    @DeleteMapping("/{idProyecto}/{idEmpleado}")
    public ResponseEntity<Void> desasignarEmpleado(
            @PathVariable Integer idProyecto,
            @PathVariable Integer idEmpleado) {

        empleadoProyectoService.desasignarEmpleadoDeProyecto(idProyecto, idEmpleado);
        return ResponseEntity.ok().build();
    }
}
