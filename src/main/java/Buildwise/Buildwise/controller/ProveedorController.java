package Buildwise.Buildwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import Buildwise.Buildwise.model.Proveedor;
import Buildwise.Buildwise.service.ProveedorService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
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

        model.addAttribute("proveedores", proveedorService.listarActivos());
        return "proveedores/listar";
    }

    @GetMapping("/crear")
    public String crear(Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("proveedor", new Proveedor());
        return "proveedores/crear";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Proveedor proveedor, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        proveedorService.guardar(proveedor);
        return "redirect:/proveedores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        Proveedor proveedor = proveedorService.buscarPorId(id);

        if (proveedor == null) {
            return "redirect:/proveedores";
        }

        model.addAttribute("proveedor", proveedor);
        return "proveedores/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        proveedorService.desactivar(id);
        return "redirect:/proveedores";
    }
}