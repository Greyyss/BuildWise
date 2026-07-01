package Buildwise.Buildwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Buildwise.Buildwise.model.Setup;

public interface SetupRepository extends JpaRepository<Setup, Long> {

    List<Setup> findByEstado(String estado);

    long countByEstado(String estado);
}
