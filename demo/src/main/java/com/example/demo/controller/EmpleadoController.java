package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Empleado;
import com.example.demo.service.EmpleadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con los empleados.
 *
 * Expone los endpoints del recurso /api/empleados.
 */
@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class EmpleadoController {

    /**
     * Servicio encargado de la lógica de negocio de empleados.
     */
    private final EmpleadoService empleadoService;

    /**
     * Obtiene la lista de empleados activos.
     *
     * Un empleado se considera activo cuando su fecha de baja es null.
     *
     * @return respuesta HTTP 200 con la lista de empleados activos.
     */
    @GetMapping
    public ResponseEntity<List<Empleado>> obtenerEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerEmpleadosActivos();
        return ResponseEntity.ok(empleados);
    }

     /**
     * Da de alta un nuevo empleado.
     *
     * @param empleado datos del empleado a crear.
     * @return respuesta HTTP 201 con el empleado creado.
     */
    @PostMapping
    public ResponseEntity<Empleado> crearEmpleado(
            @Valid @RequestBody Empleado empleado) {

        Empleado nuevoEmpleado = empleadoService.crearEmpleado(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEmpleado);
    }

    /**
     * Da de baja un empleado de la compañía.
     *
     * @param id identificador del empleado a dar de baja.
     * @return respuesta HTTP 200 si la baja se realiza correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBajaEmpleado(@PathVariable Integer id) {

        empleadoService.darDeBajaEmpleado(id);
        return ResponseEntity.ok().build();
    } 
}