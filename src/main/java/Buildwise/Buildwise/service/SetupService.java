package Buildwise.Buildwise.service;

import java.util.List;

import org.springframework.stereotype.Service;

import Buildwise.Buildwise.model.Setup;
import Buildwise.Buildwise.repository.SetupRepository;

@Service
public class SetupService {

    private final SetupRepository setupRepository;

    public SetupService(SetupRepository setupRepository) {
        this.setupRepository = setupRepository;
    }

    public List<Setup> listarActivos() {
        return setupRepository.findByEstado("ACTIVO");
    }

    public Setup buscarPorId(Long id) {
        return setupRepository.findById(id).orElse(null);
    }

    public void guardar(Setup setup) {
        setup.setEstado("ACTIVO");
        setupRepository.save(setup);
    }

    public void desactivar(Long id) {
        Setup setup = buscarPorId(id);

        if (setup != null) {
            setup.setEstado("INACTIVO");
            setupRepository.save(setup);
        }
    }
}