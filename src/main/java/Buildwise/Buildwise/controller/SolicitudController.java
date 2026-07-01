package Buildwise.Buildwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import Buildwise.Buildwise.model.Solicitud;
import Buildwise.Buildwise.service.SolicitudService;
import jakarta.servlet.http.HttpSession;

@Controller
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    private boolean noEsAdmin(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null
                || !"ADMIN".equals(session.getAttribute("rol"));
    }

    @PostMapping("/solicitud/guardar")
    public String guardarSolicitudPublica(@ModelAttribute Solicitud solicitud) {
        solicitud.setEstado("PENDIENTE");
        solicitudService.guardar(solicitud);

        return "redirect:/publico?solicitud=ok";
    }

    @GetMapping("/solicitudes")
    public String listar(Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("solicitudes", solicitudService.listarTodas());
        return "solicitudes/listar";
    }

    @GetMapping("/solicitudes/atender/{id}")
    public String atender(@PathVariable Long id, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        solicitudService.marcarAtendida(id);
        return "redirect:/solicitudes";
    }

    @GetMapping("/solicitudes/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        solicitudService.eliminar(id);
        return "redirect:/solicitudes";
    }
}