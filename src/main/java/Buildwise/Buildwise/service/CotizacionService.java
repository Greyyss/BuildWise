package Buildwise.Buildwise.service;

import java.util.List;

import org.springframework.stereotype.Service;

import Buildwise.Buildwise.model.Cotizacion;
import Buildwise.Buildwise.repository.CotizacionRepository;

@Service
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;

    public CotizacionService(CotizacionRepository cotizacionRepository) {
        this.cotizacionRepository = cotizacionRepository;
    }

    public List<Cotizacion> listarActivas() {
        return cotizacionRepository.findByEstado("ACTIVA");
    }

    public void guardar(Cotizacion cotizacion) {
        cotizacion.setEstado("ACTIVA");
        cotizacionRepository.save(cotizacion);
    }

    public Cotizacion buscarPorId(Long id) {
        return cotizacionRepository.findById(id).orElse(null);
    }

    public void desactivar(Long id) {
        Cotizacion cotizacion = buscarPorId(id);

        if (cotizacion != null) {
            cotizacion.setEstado("INACTIVA");
            cotizacionRepository.save(cotizacion);
        }
    }
}