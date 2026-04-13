package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Proyecto;

/**
 * Repositorio encargado de acceder a los datos de la entidad Proyecto.
 *
 * Proporciona las operaciones CRUD básicas gracias a JpaRepository
 * y métodos específicos para la gestión de proyectos.
 */
@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {

    /**
     * Obtiene la lista de proyectos activos.
     *
     * Un proyecto se considera activo cuando su fecha de baja es null.
     *
     * @return lista de proyectos que no han sido dados de baja.
     */
    List<Proyecto> findByFechaBajaIsNull();

}
