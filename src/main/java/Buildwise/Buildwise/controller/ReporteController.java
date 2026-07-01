package Buildwise.Buildwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import Buildwise.Buildwise.repository.CategoriaRepository;
import Buildwise.Buildwise.repository.ComponenteRepository;
import Buildwise.Buildwise.repository.CotizacionRepository;
import Buildwise.Buildwise.repository.ProveedorRepository;
import Buildwise.Buildwise.repository.SetupRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReporteController {

    private final ComponenteRepository componenteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final SetupRepository setupRepository;
    private final CotizacionRepository cotizacionRepository;

    public ReporteController(
            ComponenteRepository componenteRepository,
            CategoriaRepository categoriaRepository,
            ProveedorRepository proveedorRepository,
            SetupRepository setupRepository,
            CotizacionRepository cotizacionRepository) {

        this.componenteRepository = componenteRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
        this.setupRepository = setupRepository;
        this.cotizacionRepository = cotizacionRepository;
    }

    private boolean noEsAdmin(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null
                || !"ADMIN".equals(session.getAttribute("rol"));
    }

    @GetMapping("/reportes")
    public String reportes(Model model, HttpSession session) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("totalComponentes", componenteRepository.countByEstado("ACTIVO"));
        model.addAttribute("totalCategorias", categoriaRepository.countByEstado("ACTIVO"));
        model.addAttribute("totalProveedores", proveedorRepository.countByEstado("ACTIVO"));
        model.addAttribute("totalSetups", setupRepository.countByEstado("ACTIVO"));
        model.addAttribute("totalCotizaciones", cotizacionRepository.countByEstado("ACTIVA"));

        model.addAttribute("valorInventario", componenteRepository.calcularValorInventario());
        model.addAttribute("cantidadStockBajo", componenteRepository.countByEstadoAndStockLessThanEqual("ACTIVO", 5));

        model.addAttribute("componentesStockBajo", componenteRepository.findByEstadoAndStockLessThanEqual("ACTIVO", 5));
        model.addAttribute("ultimasCotizaciones", cotizacionRepository.findByEstado("ACTIVA"));

        return "reportes/index";
    }
}