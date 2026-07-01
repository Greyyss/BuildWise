package Buildwise.Buildwise.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import Buildwise.Buildwise.model.Usuario;
import Buildwise.Buildwise.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public boolean validarLogin(String correo, String password) {

        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);

        return usuario.isPresent()
                && usuario.get().getPassword().equals(password)
                && usuario.get().getEstado().equals("ACTIVO");
    }

    public Usuario obtenerUsuario(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }
}