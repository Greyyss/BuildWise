package Buildwise.Buildwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Buildwise.Buildwise.model.Solicitud;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

}