package Buildwise.Buildwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import Buildwise.Buildwise.model.Rol;
import Buildwise.Buildwise.model.Usuario;
import Buildwise.Buildwise.repository.RolRepository;
import Buildwise.Buildwise.repository.UsuarioRepository;

@Controller
public class RegistroController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public RegistroController(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {

        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            model.addAttribute("error", "Ya existe una cuenta registrada con ese correo.");
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        Rol rolUsuario = rolRepository.findByNombre("USUARIO");

        if (rolUsuario == null) {
            model.addAttribute("error", "No existe el rol USUARIO en la base de datos.");
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        usuario.setRol(rolUsuario);
        usuario.setEstado("ACTIVO");

        usuarioRepository.save(usuario);

        return "redirect:/login?registro=ok";
    }
}