package Buildwise.Buildwise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Buildwise.Buildwise.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

}