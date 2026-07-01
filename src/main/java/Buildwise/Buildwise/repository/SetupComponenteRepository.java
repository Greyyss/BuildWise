package Buildwise.Buildwise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Buildwise.Buildwise.model.SetupComponente;

public interface SetupComponenteRepository extends JpaRepository<SetupComponente, Long> {

    List<SetupComponente> findBySetupId(Long setupId);

}