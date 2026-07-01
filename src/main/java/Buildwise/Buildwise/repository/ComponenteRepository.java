package Buildwise.Buildwise.repository;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import Buildwise.Buildwise.model.Componente;

public interface ComponenteRepository extends JpaRepository<Componente, Long> {

    List<Componente> findByEstado(String estado);
    List<Componente> findByEstadoAndCategoriaNombreContainingIgnoreCaseOrderByPrecioAsc(String estado, String categoria);
    List<Componente> findByEstadoAndStockLessThanEqual(String estado, Integer stock);

    long countByEstado(String estado);

    long countByEstadoAndStockLessThanEqual(String estado, Integer stock);

    @Query("SELECT COALESCE(SUM(c.precio * c.stock), 0) FROM Componente c WHERE c.estado = 'ACTIVO'")
    BigDecimal calcularValorInventario();

}