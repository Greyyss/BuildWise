package Buildwise.Buildwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Buildwise.Buildwise.model.Cotizacion;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

    List<Cotizacion> findByEstado(String estado);

    long countByEstado(String estado);
}
