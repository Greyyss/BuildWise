package Buildwise.Buildwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Buildwise.Buildwise.model.Usuario;
import Buildwise.Buildwise.repository.CategoriaRepository;
import Buildwise.Buildwise.repository.ComponenteRepository;
import Buildwise.Buildwise.repository.CotizacionRepository;
import Buildwise.Buildwise.repository.ProveedorRepository;
import Buildwise.Buildwise.repository.SetupRepository;
import Buildwise.Buildwise.repository.SolicitudRepository;
import Buildwise.Buildwise.service.SetupService;
import Buildwise.Buildwise.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private final UsuarioService usuarioService;
    private final ComponenteRepository componenteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final SolicitudRepository solicitudRepository;
    private final SetupService setupService;
    private final SetupRepository setupRepository;
    private final CotizacionRepository cotizacionRepository;

    public HomeController(
            UsuarioService usuarioService,
            ComponenteRepository componenteRepository,
            CategoriaRepository categoriaRepository,
            ProveedorRepository proveedorRepository,
            SolicitudRepository solicitudRepository,
            SetupService setupService,
            SetupRepository setupRepository,
            CotizacionRepository cotizacionRepository) {

        this.usuarioService = usuarioService;
        this.componenteRepository = componenteRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
        this.solicitudRepository = solicitudRepository;
        this.setupService = setupService;
        this.setupRepository = setupRepository;
        this.cotizacionRepository = cotizacionRepository;
    }

    private boolean noEsAdmin(HttpSession session) {
        return session.getAttribute("usuarioLogueado") == null
                || !"ADMIN".equals(session.getAttribute("rol"));
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("setups", setupService.listarActivos());
        model.addAttribute("usuarioLogueado", session.getAttribute("usuarioLogueado"));
        model.addAttribute("rol", session.getAttribute("rol"));
        return "publico/index";
    }

    @GetMapping("/publico")
    public String publico(Model model, HttpSession session) {
        model.addAttribute("setups", setupService.listarActivos());
        model.addAttribute("usuarioLogueado", session.getAttribute("usuarioLogueado"));
        model.addAttribute("rol", session.getAttribute("rol"));
        return "publico/index";
    }

    @GetMapping("/stock")
    public String stock(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        model.addAttribute("componentes", componenteRepository.findByEstado("ACTIVO"));
        model.addAttribute("usuarioLogueado", session.getAttribute("usuarioLogueado"));
        model.addAttribute("rol", session.getAttribute("rol"));

        return "publico/stock";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String correo,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        if (usuarioService.validarLogin(correo, password)) {
            Usuario usuario = usuarioService.obtenerUsuario(correo);

            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("rol", usuario.getRol().getNombre());

            if ("ADMIN".equals(usuario.getRol().getNombre())) {
                return "redirect:/dashboard";
            }

            return "redirect:/publico";
        }

        model.addAttribute("error", "Correo o contraseña incorrectos");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (noEsAdmin(session)) {
            return "redirect:/publico";
        }

        model.addAttribute("totalComponentes", componenteRepository.countByEstado("ACTIVO"));
        model.addAttribute("totalCategorias", categoriaRepository.countByEstado("ACTIVO"));
        model.addAttribute("totalProveedores", proveedorRepository.countByEstado("ACTIVO"));
        model.addAttribute("totalSolicitudes", solicitudRepository.count());
        model.addAttribute("totalSetups", setupRepository.countByEstado("ACTIVO"));
        model.addAttribute("totalCotizaciones", cotizacionRepository.countByEstado("ACTIVA"));
        model.addAttribute("ultimasCotizaciones", cotizacionRepository.findByEstado("ACTIVA"));
        model.addAttribute("valorInventario", componenteRepository.calcularValorInventario());
        model.addAttribute("stockBajo", componenteRepository.countByEstadoAndStockLessThanEqual("ACTIVO", 5));

        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}