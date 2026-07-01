package Buildwise.Buildwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Buildwise.Buildwise.model.Cotizacion;
import Buildwise.Buildwise.model.Solicitud;
import Buildwise.Buildwise.service.CotizacionService;
import Buildwise.Buildwise.service.SolicitudService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cotizaciones")
public class CotizacionController {

    private final CotizacionService cotizacionService;
    private final SolicitudService solicitudService;

    public CotizacionController(
            CotizacionService cotizacionService,
            SolicitudService solicitudService) {

        this.cotizacionService = cotizacionService;
        this.solicitudService = solicitudService;
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

        model.addAttribute("cotizaciones", cotizacionService.listarActivas());
        return "cotizaciones/listar";
    }

    @GetMapping("/crear")
    public String crear(Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("cotizacion", new Cotizacion());
        return "cotizaciones/crear";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cotizacion cotizacion, HttpSession session) {
        if (noEstaLogueado(session)) {
            return "redirect:/login";
        }

        cotizacionService.guardar(cotizacion);
        return "redirect:/cotizaciones";
    }

    @GetMapping("/crear-desde-solicitud/{id}")
    public String crearDesdeSolicitud(@PathVariable Long id, Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        Solicitud solicitud = solicitudService.buscarPorId(id);

        if (solicitud == null) {
            return "redirect:/solicitudes";
        }

        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setCliente(solicitud.getNombreCliente());
        cotizacion.setTipoUso(solicitud.getTipoUso());
        cotizacion.setPresupuesto(solicitud.getPresupuesto());
        cotizacion.setTotal(solicitud.getPresupuesto());

        model.addAttribute("cotizacion", cotizacion);
        model.addAttribute("solicitud", solicitud);

        return "cotizaciones/crear-desde-solicitud";
    }

    @PostMapping("/guardar-desde-solicitud")
    public String guardarDesdeSolicitud(
            @ModelAttribute Cotizacion cotizacion,
            @RequestParam Long solicitudId,
            HttpSession session) {

        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        cotizacion.setEstado("ACTIVA");
        cotizacionService.guardar(cotizacion);

        solicitudService.marcarAtendida(solicitudId);

        return "redirect:/cotizaciones";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        cotizacionService.desactivar(id);
        return "redirect:/cotizaciones";
    }
}