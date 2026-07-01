package Buildwise.Buildwise.service;

import java.util.List;

import org.springframework.stereotype.Service;

import Buildwise.Buildwise.model.Componente;
import Buildwise.Buildwise.repository.ComponenteRepository;

@Service
public class ComponenteService {

    private final ComponenteRepository componenteRepository;

    public ComponenteService(ComponenteRepository componenteRepository) {
        this.componenteRepository = componenteRepository;
    }

    public List<Componente> listarActivos() {
        return componenteRepository.findByEstado("ACTIVO");
    }

    public Componente buscarPorId(Long id) {
        return componenteRepository.findById(id).orElse(null);
    }

    public void guardar(Componente componente) {
        componente.setEstado("ACTIVO");
        componenteRepository.save(componente);
    }

    public void desactivar(Long id) {
        Componente componente = buscarPorId(id);
        if (componente != null) {
            componente.setEstado("INACTIVO");
            componenteRepository.save(componente);
        }
    }
}