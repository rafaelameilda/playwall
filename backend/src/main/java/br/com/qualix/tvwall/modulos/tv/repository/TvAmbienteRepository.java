package br.com.qualix.tvwall.modulos.tv.repository;

import br.com.qualix.tvwall.modulos.tv.model.TvAmbiente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TvAmbienteRepository extends JpaRepository<TvAmbiente, Long> {
    List<TvAmbiente> findByIdTenantAndStStatus(Long idTenant, String stStatus);
    List<TvAmbiente> findByIdTenant(Long idTenant);
}
