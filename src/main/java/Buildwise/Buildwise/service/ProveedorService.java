package Buildwise.Buildwise.service;

import java.util.List;

import org.springframework.stereotype.Service;

import Buildwise.Buildwise.model.Proveedor;
import Buildwise.Buildwise.repository.ProveedorRepository;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> listarActivos() {
        return proveedorRepository.findByEstado("ACTIVO");
    }

    public Proveedor buscarPorId(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    public void guardar(Proveedor proveedor) {
        proveedor.setEstado("ACTIVO");
        proveedorRepository.save(proveedor);
    }

    public void desactivar(Long id) {
        Proveedor proveedor = buscarPorId(id);
        if (proveedor != null) {
            proveedor.setEstado("INACTIVO");
            proveedorRepository.save(proveedor);
        }
    }
}
