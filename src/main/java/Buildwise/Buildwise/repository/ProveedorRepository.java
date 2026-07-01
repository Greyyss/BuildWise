package Buildwise.Buildwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Buildwise.Buildwise.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByEstado(String estado);

    long countByEstado(String estado);

}