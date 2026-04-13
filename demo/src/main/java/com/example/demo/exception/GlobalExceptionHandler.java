package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones de la aplicación.
 *
 * Centraliza la gestión de los errores producidos en los controladores
 * y servicios, devolviendo la respuesta HTTP adecuada en cada caso.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestiona las excepciones de tipo IllegalArgumentException.
     *
     * Se utiliza para errores de negocio como:
     * - Empleado o proyecto inexistente.
     * - Datos duplicados.
     * - Intentos de baja no permitidos.
     *
     * @param ex excepción lanzada durante la ejecución.
     * @return respuesta HTTP 400 con el mensaje de error correspondiente.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> manejarIllegalArgumentException(
            IllegalArgumentException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Gestiona los errores de validación de los objetos recibidos
     * en las peticiones REST.
     *
     * Se ejecuta cuando fallan las validaciones anotadas con:
     * - @NotBlank
     * - @NotNull
     * - @Email
     * - @Pattern
     * - etc.
     *
     * @param ex excepción que contiene los errores de validación.
     * @return respuesta HTTP 400 con un mapa campo - mensaje de error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errores);
    }

    /**
     * Gestiona cualquier excepción no controlada de forma específica.
     *
     * @param ex excepción producida.
     * @return respuesta HTTP 500 con un mensaje genérico.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarExcepcionGeneral(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ha ocurrido un error inesperado");
    }
}