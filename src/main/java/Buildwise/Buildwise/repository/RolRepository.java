package Buildwise.Buildwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Buildwise.Buildwise.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Rol findByNombre(String nombre);

}
