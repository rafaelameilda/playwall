package br.com.qualix.tvwall.modulos.tv.repository;

import br.com.qualix.tvwall.modulos.tv.model.TvGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TvGrupoRepository extends JpaRepository<TvGrupo, Long> {
    List<TvGrupo> findByIdTenant(Long idTenant);
    List<TvGrupo> findByIdTenantAndStStatus(Long idTenant, String stStatus);
}
