package Buildwise.Buildwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import Buildwise.Buildwise.model.Categoria;
import Buildwise.Buildwise.service.CategoriaService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
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

        model.addAttribute("categorias", categoriaService.listarActivas());
        return "categorias/listar";
    }

    @GetMapping("/crear")
    public String crear(Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("categoria", new Categoria());
        return "categorias/crear";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        categoriaService.guardar(categoria);
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("categoria", categoriaService.buscarPorId(id));
        return "categorias/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        categoriaService.desactivar(id);
        return "redirect:/categorias";
    }
}