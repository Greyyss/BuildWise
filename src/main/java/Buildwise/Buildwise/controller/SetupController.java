package Buildwise.Buildwise.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Buildwise.Buildwise.model.Setup;
import Buildwise.Buildwise.service.ComponenteService;
import Buildwise.Buildwise.service.SetupComponenteService;
import Buildwise.Buildwise.service.SetupService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/setups")
public class SetupController {

    private final SetupService setupService;
    private final SetupComponenteService setupComponenteService;
    private final ComponenteService componenteService;

    public SetupController(
            SetupService setupService,
            SetupComponenteService setupComponenteService,
            ComponenteService componenteService) {

        this.setupService = setupService;
        this.setupComponenteService = setupComponenteService;
        this.componenteService = componenteService;
    }

    private boolean noEstaLogueado(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null;
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

        model.addAttribute("setups", setupService.listarActivos());
        model.addAttribute("rol", session.getAttribute("rol"));

        return "setups/listar";
    }

    @GetMapping("/crear")
    public String crear(Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("setup", new Setup());
        return "setups/crear";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Setup setup, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        setupService.guardar(setup);
        return "redirect:/setups";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        Setup setup = setupService.buscarPorId(id);

        if (setup == null) {
            return "redirect:/setups";
        }

        model.addAttribute("setup", setup);
        return "setups/editar";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model, HttpSession session) {
        if (noEstaLogueado(session)) {
            return "redirect:/login";
        }

        Setup setup = setupService.buscarPorId(id);

        if (setup == null) {
            return "redirect:/setups";
        }

        BigDecimal totalReal = setupComponenteService.calcularTotalSetup(id);
        BigDecimal presupuesto = setup.getPresupuesto() != null ? setup.getPresupuesto() : BigDecimal.ZERO;
        BigDecimal diferencia = presupuesto.subtract(totalReal);

        boolean dentroPresupuesto = diferencia.compareTo(BigDecimal.ZERO) >= 0;

        model.addAttribute("setup", setup);
        model.addAttribute("componentesSetup", setupComponenteService.listarPorSetup(id));
        model.addAttribute("componentesDisponibles", componenteService.listarActivos());
        model.addAttribute("totalReal", totalReal);
        model.addAttribute("diferencia", diferencia.abs());
        model.addAttribute("dentroPresupuesto", dentroPresupuesto);
        model.addAttribute("rol", session.getAttribute("rol"));

        return "setups/detalle";
    }

    @PostMapping("/agregar-componente")
    public String agregarComponente(
            @RequestParam Long setupId,
            @RequestParam Long componenteId,
            @RequestParam Integer cantidad,
            HttpSession session) {

        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        setupComponenteService.agregarComponente(setupId, componenteId, cantidad);

        return "redirect:/setups/detalle/" + setupId;
    }

    @GetMapping("/eliminar-componente/{id}/{setupId}")
    public String eliminarComponente(
            @PathVariable Long id,
            @PathVariable Long setupId,
            HttpSession session) {

        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        setupComponenteService.eliminar(id);

        return "redirect:/setups/detalle/" + setupId;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        setupService.desactivar(id);
        return "redirect:/setups";
    }
}