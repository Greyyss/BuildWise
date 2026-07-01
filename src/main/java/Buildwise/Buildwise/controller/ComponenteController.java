package Buildwise.Buildwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import Buildwise.Buildwise.model.Componente;
import Buildwise.Buildwise.service.CategoriaService;
import Buildwise.Buildwise.service.ComponenteService;
import Buildwise.Buildwise.service.ProveedorService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/componentes")
public class ComponenteController {

    private final ComponenteService componenteService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;

    public ComponenteController(
            ComponenteService componenteService,
            CategoriaService categoriaService,
            ProveedorService proveedorService) {

        this.componenteService = componenteService;
        this.categoriaService = categoriaService;
        this.proveedorService = proveedorService;
    }

    private boolean noEsAdmin(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null
                || !"ADMIN".equals(session.getAttribute("rol"));
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("componentes", componenteService.listarActivos());
        return "componentes/listar";
    }

    @GetMapping("/crear")
    public String crear(Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("componente", new Componente());
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("proveedores", proveedorService.listarActivos());

        return "componentes/crear";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Componente componente, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        componenteService.guardar(componente);
        return "redirect:/componentes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("componente", componenteService.buscarPorId(id));
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("proveedores", proveedorService.listarActivos());

        return "componentes/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        componenteService.desactivar(id);
        return "redirect:/componentes";
    }
}