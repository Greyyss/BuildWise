package Buildwise.Buildwise.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import Buildwise.Buildwise.model.Componente;
import Buildwise.Buildwise.model.Setup;
import Buildwise.Buildwise.model.SetupComponente;
import Buildwise.Buildwise.repository.ComponenteRepository;
import Buildwise.Buildwise.repository.SetupComponenteRepository;
import Buildwise.Buildwise.repository.SetupRepository;

@Service
public class SetupComponenteService {

    private final SetupComponenteRepository setupComponenteRepository;
    private final SetupRepository setupRepository;
    private final ComponenteRepository componenteRepository;

    public SetupComponenteService(
            SetupComponenteRepository setupComponenteRepository,
            SetupRepository setupRepository,
            ComponenteRepository componenteRepository) {

        this.setupComponenteRepository = setupComponenteRepository;
        this.setupRepository = setupRepository;
        this.componenteRepository = componenteRepository;
    }

    public List<SetupComponente> listarPorSetup(Long setupId) {
        return setupComponenteRepository.findBySetupId(setupId);
    }

    public void agregarComponente(Long setupId, Long componenteId, Integer cantidad) {
        Setup setup = setupRepository.findById(setupId).orElse(null);
        Componente componente = componenteRepository.findById(componenteId).orElse(null);

        if (setup != null && componente != null) {
            SetupComponente setupComponente = new SetupComponente();
            setupComponente.setSetup(setup);
            setupComponente.setComponente(componente);
            setupComponente.setCantidad(cantidad);

            setupComponenteRepository.save(setupComponente);
        }
    }

    public BigDecimal calcularTotalSetup(Long setupId) {
    List<SetupComponente> componentes = listarPorSetup(setupId);

    BigDecimal total = BigDecimal.ZERO;

    for (SetupComponente item : componentes) {
        total = total.add(item.getSubtotal());
    }

    return total;
    }

    public void eliminar(Long id) {
        setupComponenteRepository.deleteById(id);
    }
}