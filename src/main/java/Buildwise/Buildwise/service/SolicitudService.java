package Buildwise.Buildwise.service;

import java.util.List;

import org.springframework.stereotype.Service;

import Buildwise.Buildwise.model.Solicitud;
import Buildwise.Buildwise.repository.SolicitudRepository;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;

    public SolicitudService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public List<Solicitud> listarTodas() {
        return solicitudRepository.findAll();
    }

    public Solicitud buscarPorId(Long id) {
        return solicitudRepository.findById(id).orElse(null);
    }

    public void guardar(Solicitud solicitud) {
        if (solicitud.getEstado() == null || solicitud.getEstado().isBlank()) {
            solicitud.setEstado("PENDIENTE");
        }

        solicitudRepository.save(solicitud);
    }

    public void marcarAtendida(Long id) {
        Solicitud solicitud = buscarPorId(id);

        if (solicitud != null) {
            solicitud.setEstado("ATENDIDA");
            solicitudRepository.save(solicitud);
        }
    }

    public void eliminar(Long id) {
        solicitudRepository.deleteById(id);
    }
}