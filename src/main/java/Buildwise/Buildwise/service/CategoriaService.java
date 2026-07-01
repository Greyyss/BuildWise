package Buildwise.Buildwise.service;

import java.util.List;

import org.springframework.stereotype.Service;

import Buildwise.Buildwise.model.Categoria;
import Buildwise.Buildwise.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarActivas() {
        return categoriaRepository.findByEstado("ACTIVO");
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public void guardar(Categoria categoria) {
        categoria.setEstado("ACTIVO");
        categoriaRepository.save(categoria);
    }

    public void desactivar(Long id) {
        Categoria categoria = buscarPorId(id);
        if (categoria != null) {
            categoria.setEstado("INACTIVO");
            categoriaRepository.save(categoria);
        }
    }
}