package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Empleado;

/**
 * Repositorio encargado de acceder a los datos de la entidad Empleado.
 *
 * Proporciona las operaciones CRUD básicas gracias a JpaRepository
 * y métodos específicos para la gestión de empleados.
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    /**
     * Obtiene la lista de empleados activos.
     *
     * Un empleado se considera activo cuando su fecha de baja es null.
     *
     * @return lista de empleados que no han sido dados de baja.
     */
    List<Empleado> findByFechaBajaIsNull();
    
    /**
     * Comprueba si ya existe un empleado con el NIF indicado.
     *
     * @param nif NIF a comprobar.
     * @return true si ya existe un empleado con ese NIF, false en caso contrario.
     */
    boolean existsByNif(String nif);

    /**
     * Comprueba si ya existe un empleado con el email indicado.
     *
     * @param email dirección de correo electrónico a comprobar.
     * @return true si ya existe un empleado con ese email, false en caso contrario.
     */
    boolean existsByEmail(String email);

}