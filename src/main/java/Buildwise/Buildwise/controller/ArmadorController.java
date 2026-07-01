package Buildwise.Buildwise.controller;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Buildwise.Buildwise.model.Componente;
import Buildwise.Buildwise.model.Setup;
import Buildwise.Buildwise.repository.ComponenteRepository;
import Buildwise.Buildwise.service.ArmadorService;
import Buildwise.Buildwise.service.SetupComponenteService;
import Buildwise.Buildwise.service.SetupService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/armador")
public class ArmadorController {

    private final ArmadorService armadorService;
    private final SetupService setupService;
    private final SetupComponenteService setupComponenteService;
    private final ComponenteRepository componenteRepository;

    public ArmadorController(
            ArmadorService armadorService,
            SetupService setupService,
            SetupComponenteService setupComponenteService,
            ComponenteRepository componenteRepository) {

        this.armadorService = armadorService;
        this.setupService = setupService;
        this.setupComponenteService = setupComponenteService;
        this.componenteRepository = componenteRepository;
    }

    private boolean noEstaLogueado(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null;
    }

    private boolean noEsAdmin(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null
                || !"ADMIN".equals(session.getAttribute("rol"));
    }

    @GetMapping
    public String index(HttpSession session, Model model) {
        if (noEstaLogueado(session)) {
            return "redirect:/login";
        }

        model.addAttribute("rol", session.getAttribute("rol"));

        return "armador/index";
    }

    @PostMapping("/recomendar")
    public String recomendar(
            @RequestParam BigDecimal presupuesto,
            @RequestParam String tipoUso,
            Model model,
            HttpSession session) {

        if (noEstaLogueado(session)) {
            return "redirect:/login";
        }

        Map<String, Componente> recomendacion =
                armadorService.generarRecomendacion(presupuesto, tipoUso);

        BigDecimal total = armadorService.calcularTotal(recomendacion);
        BigDecimal diferencia = armadorService.calcularDiferencia(presupuesto, total);
        boolean dentroPresupuesto = armadorService.estaDentroDelPresupuesto(presupuesto, total);

        model.addAttribute("recomendacion", recomendacion);
        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("tipoUso", tipoUso);
        model.addAttribute("total", total);
        model.addAttribute("diferencia", diferencia);
        model.addAttribute("dentroPresupuesto", dentroPresupuesto);
        model.addAttribute("rol", session.getAttribute("rol"));

        return "armador/resultado";
    }

    @GetMapping("/manual")
    public String manual(Model model, HttpSession session) {
        if (noEstaLogueado(session)) {
            return "redirect:/login";
        }

        cargarComponentesParaManual(model);
        model.addAttribute("rol", session.getAttribute("rol"));

        return "armador/manual";
    }

    @PostMapping("/manual/calcular")
    public String calcularManual(
            @RequestParam BigDecimal presupuesto,
            @RequestParam Long procesadorId,
            @RequestParam Long tarjetaMadreId,
            @RequestParam Long memoriaId,
            @RequestParam Long almacenamientoId,
            @RequestParam Long fuenteId,
            @RequestParam(required = false) Long graficaId,
            @RequestParam(required = false) Long gabineteId,
            Model model,
            HttpSession session) {

        if (noEstaLogueado(session)) {
            return "redirect:/login";
        }

        Map<String, Componente> seleccion = new LinkedHashMap<>();

        seleccion.put("Procesador", buscarComponente(procesadorId));
        seleccion.put("Tarjeta Madre", buscarComponente(tarjetaMadreId));
        seleccion.put("Memoria RAM", buscarComponente(memoriaId));
        seleccion.put("Almacenamiento", buscarComponente(almacenamientoId));
        seleccion.put("Fuente de Poder", buscarComponente(fuenteId));

        if (graficaId != null) {
            seleccion.put("Tarjeta Gráfica", buscarComponente(graficaId));
        }

        if (gabineteId != null) {
            seleccion.put("Gabinete", buscarComponente(gabineteId));
        }

        BigDecimal total = armadorService.calcularTotal(seleccion);
        BigDecimal diferencia = armadorService.calcularDiferencia(presupuesto, total);
        boolean dentroPresupuesto = armadorService.estaDentroDelPresupuesto(presupuesto, total);

        model.addAttribute("seleccion", seleccion);
        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("total", total);
        model.addAttribute("diferencia", diferencia);
        model.addAttribute("dentroPresupuesto", dentroPresupuesto);
        model.addAttribute("rol", session.getAttribute("rol"));

        return "armador/manual-resultado";
    }

    private Componente buscarComponente(Long id) {
        if (id == null) {
            return null;
        }

        return componenteRepository.findById(id).orElse(null);
    }

    private void cargarComponentesParaManual(Model model) {
        model.addAttribute("procesadores",
                componenteRepository.findByEstadoAndCategoriaNombreContainingIgnoreCaseOrderByPrecioAsc("ACTIVO", "procesador"));

        model.addAttribute("tarjetasMadre",
                componenteRepository.findByEstadoAndCategoriaNombreContainingIgnoreCaseOrderByPrecioAsc("ACTIVO", "tarjeta madre"));

        model.addAttribute("memorias",
                componenteRepository.findByEstadoAndCategoriaNombreContainingIgnoreCaseOrderByPrecioAsc("ACTIVO", "memoria"));

        model.addAttribute("almacenamientos",
                componenteRepository.findByEstadoAndCategoriaNombreContainingIgnoreCaseOrderByPrecioAsc("ACTIVO", "almacenamiento"));

        model.addAttribute("fuentes",
                componenteRepository.findByEstadoAndCategoriaNombreContainingIgnoreCaseOrderByPrecioAsc("ACTIVO", "fuente"));

        model.addAttribute("graficas",
                componenteRepository.findByEstadoAndCategoriaNombreContainingIgnoreCaseOrderByPrecioAsc("ACTIVO", "gráfica"));

        model.addAttribute("gabinetes",
                componenteRepository.findByEstadoAndCategoriaNombreContainingIgnoreCaseOrderByPrecioAsc("ACTIVO", "gabinete"));
    }

    @PostMapping("/guardar-setup")
    public String guardarComoSetup(
            @RequestParam String nombre,
            @RequestParam String tipoUso,
            @RequestParam BigDecimal presupuesto,
            HttpSession session) {

        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        Map<String, Componente> recomendacion =
                armadorService.generarRecomendacion(presupuesto, tipoUso);

        Setup setup = new Setup();
        setup.setNombre(nombre);
        setup.setTipoUso(tipoUso);
        setup.setPresupuesto(presupuesto);
        setup.setDescripcion("Setup generado automáticamente desde el armador de PC.");
        setup.setEstado("ACTIVO");

        setupService.guardar(setup);

        for (Componente componente : recomendacion.values()) {
            if (componente != null) {
                setupComponenteService.agregarComponente(setup.getId(), componente.getId(), 1);
            }
        }

        return "redirect:/setups/detalle/" + setup.getId();
    }
}