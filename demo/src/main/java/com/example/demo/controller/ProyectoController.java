package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Proyecto;
import com.example.demo.service.ProyectoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con los proyectos.
 *
 * Expone los endpoints del recurso /api/proyectos.
 */
@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ProyectoController {

    /**
     * Servicio encargado de la lógica de negocio de proyectos.
     */
    private final ProyectoService proyectoService;

    /**
     * Obtiene la lista de proyectos activos.
     *
     * Un proyecto se considera activo cuando su fecha de baja es null.
     *
     * @return respuesta HTTP 200 con la lista de proyectos activos.
     */
    @GetMapping
    public ResponseEntity<List<Proyecto>> obtenerProyectos() {
        List<Proyecto> proyectos = proyectoService.obtenerProyectosActivos();
        return ResponseEntity.ok(proyectos);
    }

   /**
     * Da de alta un nuevo proyecto.
     *
     * @param proyecto datos del proyecto a crear.
     * @return respuesta HTTP 201 con el proyecto creado.
     */
    @PostMapping
    public ResponseEntity<Proyecto> crearProyecto(
            @Valid @RequestBody Proyecto proyecto) {

        Proyecto nuevoProyecto = proyectoService.crearProyecto(proyecto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProyecto);
    }

    /**
     * Da de baja un proyecto de la compañía.
     *
     * @param id identificador del proyecto a dar de baja.
     * @return respuesta HTTP 200 si la baja se realiza correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBajaProyecto(@PathVariable Integer id) {

        proyectoService.darDeBajaProyecto(id);
        return ResponseEntity.ok().build();
    }

   
}