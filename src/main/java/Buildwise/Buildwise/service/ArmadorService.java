package Buildwise.Buildwise.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Buildwise.Buildwise.model.Componente;
import Buildwise.Buildwise.repository.ComponenteRepository;

@Service
public class ArmadorService {

    private final ComponenteRepository componenteRepository;

    public ArmadorService(ComponenteRepository componenteRepository) {
        this.componenteRepository = componenteRepository;
    }

    public Map<String, Componente> generarRecomendacion(BigDecimal presupuesto, String tipoUso) {
        Map<String, Componente> recomendacion = new LinkedHashMap<>();

        if (presupuesto == null) {
            presupuesto = BigDecimal.ZERO;
        }

        if (tipoUso == null) {
            tipoUso = "ESTUDIO";
        }

        tipoUso = tipoUso.toUpperCase();

        if (tipoUso.equals("GAMING")) {
            generarGaming(recomendacion, presupuesto);
        } else if (tipoUso.equals("DISEÑO") || tipoUso.equals("DISENO")) {
            generarDiseno(recomendacion, presupuesto);
        } else if (tipoUso.equals("OFICINA")) {
            generarOficina(recomendacion, presupuesto);
        } else {
            generarEstudio(recomendacion, presupuesto);
        }

        return recomendacion;
    }

    private void generarGaming(Map<String, Componente> recomendacion, BigDecimal presupuesto) {
        recomendacion.put("Procesador", obtenerComponentePorPresupuesto("procesador", porcentaje(presupuesto, 18)));
        recomendacion.put("Tarjeta Madre", obtenerComponentePorPresupuesto("tarjeta madre", porcentaje(presupuesto, 14)));
        recomendacion.put("Memoria RAM", obtenerComponentePorPresupuesto("memoria", porcentaje(presupuesto, 10)));
        recomendacion.put("Almacenamiento", obtenerComponentePorPresupuesto("almacenamiento", porcentaje(presupuesto, 10)));
        recomendacion.put("Fuente de Poder", obtenerComponentePorPresupuesto("fuente", porcentaje(presupuesto, 10)));
        recomendacion.put("Tarjeta Gráfica", obtenerComponentePorPresupuesto("gráfica", porcentaje(presupuesto, 30)));
        recomendacion.put("Gabinete", obtenerComponentePorPresupuesto("gabinete", porcentaje(presupuesto, 8)));
    }

    private void generarDiseno(Map<String, Componente> recomendacion, BigDecimal presupuesto) {
        recomendacion.put("Procesador", obtenerComponentePorPresupuesto("procesador", porcentaje(presupuesto, 22)));
        recomendacion.put("Tarjeta Madre", obtenerComponentePorPresupuesto("tarjeta madre", porcentaje(presupuesto, 13)));
        recomendacion.put("Memoria RAM", obtenerComponentePorPresupuesto("memoria", porcentaje(presupuesto, 15)));
        recomendacion.put("Almacenamiento", obtenerComponentePorPresupuesto("almacenamiento", porcentaje(presupuesto, 12)));
        recomendacion.put("Fuente de Poder", obtenerComponentePorPresupuesto("fuente", porcentaje(presupuesto, 10)));
        recomendacion.put("Tarjeta Gráfica", obtenerComponentePorPresupuesto("gráfica", porcentaje(presupuesto, 20)));
        recomendacion.put("Gabinete", obtenerComponentePorPresupuesto("gabinete", porcentaje(presupuesto, 8)));
    }

    private void generarOficina(Map<String, Componente> recomendacion, BigDecimal presupuesto) {
        recomendacion.put("Procesador", obtenerComponentePorPresupuesto("procesador", porcentaje(presupuesto, 25)));
        recomendacion.put("Tarjeta Madre", obtenerComponentePorPresupuesto("tarjeta madre", porcentaje(presupuesto, 20)));
        recomendacion.put("Memoria RAM", obtenerComponentePorPresupuesto("memoria", porcentaje(presupuesto, 15)));
        recomendacion.put("Almacenamiento", obtenerComponentePorPresupuesto("almacenamiento", porcentaje(presupuesto, 15)));
        recomendacion.put("Fuente de Poder", obtenerComponentePorPresupuesto("fuente", porcentaje(presupuesto, 15)));
        recomendacion.put("Gabinete", obtenerComponentePorPresupuesto("gabinete", porcentaje(presupuesto, 10)));
    }

    private void generarEstudio(Map<String, Componente> recomendacion, BigDecimal presupuesto) {
        recomendacion.put("Procesador", obtenerComponentePorPresupuesto("procesador", porcentaje(presupuesto, 25)));
        recomendacion.put("Tarjeta Madre", obtenerComponentePorPresupuesto("tarjeta madre", porcentaje(presupuesto, 20)));
        recomendacion.put("Memoria RAM", obtenerComponentePorPresupuesto("memoria", porcentaje(presupuesto, 15)));
        recomendacion.put("Almacenamiento", obtenerComponentePorPresupuesto("almacenamiento", porcentaje(presupuesto, 15)));
        recomendacion.put("Fuente de Poder", obtenerComponentePorPresupuesto("fuente", porcentaje(presupuesto, 15)));
        recomendacion.put("Gabinete", obtenerComponentePorPresupuesto("gabinete", porcentaje(presupuesto, 10)));
    }

    private BigDecimal porcentaje(BigDecimal presupuesto, int porcentaje) {
        return presupuesto.multiply(BigDecimal.valueOf(porcentaje))
                .divide(BigDecimal.valueOf(100));
    }

    private Componente obtenerComponentePorPresupuesto(String categoria, BigDecimal presupuestoCategoria) {
        List<Componente> componentes =
                componenteRepository.findByEstadoAndCategoriaNombreContainingIgnoreCaseOrderByPrecioAsc(
                        "ACTIVO",
                        categoria
                );

        if (componentes.isEmpty()) {
            return null;
        }

        Componente mejorOpcion = componentes.get(0);

        for (Componente componente : componentes) {
            if (componente.getPrecio() != null &&
                    componente.getPrecio().compareTo(presupuestoCategoria) <= 0) {
                mejorOpcion = componente;
            }
        }

        return mejorOpcion;
    }

    public BigDecimal calcularTotal(Map<String, Componente> recomendacion) {
        BigDecimal total = BigDecimal.ZERO;

        for (Componente componente : recomendacion.values()) {
            if (componente != null && componente.getPrecio() != null) {
                total = total.add(componente.getPrecio());
            }
        }

        return total;
    }

    public BigDecimal calcularDiferencia(BigDecimal presupuesto, BigDecimal total) {
        if (presupuesto == null) {
            presupuesto = BigDecimal.ZERO;
        }

        if (total == null) {
            total = BigDecimal.ZERO;
        }

        return presupuesto.subtract(total).abs();
    }

    public boolean estaDentroDelPresupuesto(BigDecimal presupuesto, BigDecimal total) {
        if (presupuesto == null) {
            presupuesto = BigDecimal.ZERO;
        }

        if (total == null) {
            total = BigDecimal.ZERO;
        }

        return presupuesto.compareTo(total) >= 0;
    }
}