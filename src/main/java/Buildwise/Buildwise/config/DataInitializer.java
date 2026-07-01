package Buildwise.Buildwise.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import Buildwise.Buildwise.model.Rol;
import Buildwise.Buildwise.model.Usuario;
import Buildwise.Buildwise.repository.RolRepository;
import Buildwise.Buildwise.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {

        Rol adminRol = rolRepository.findByNombre("ADMIN");
        if (adminRol == null) {
            adminRol = rolRepository.save(new Rol(null, "ADMIN"));
        }

        Rol usuarioRol = rolRepository.findByNombre("USUARIO");
        if (usuarioRol == null) {
            usuarioRol = rolRepository.save(new Rol(null, "USUARIO"));
        }

        if (usuarioRepository.findByCorreo("admin@buildwise.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setCorreo("admin@buildwise.com");
            admin.setPassword("admin123");
            admin.setEstado("ACTIVO");
            admin.setRol(adminRol);

            usuarioRepository.save(admin);
        }
    }
}
