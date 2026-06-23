package br.com.qualix.tvwall.modulos.tv.repository;

import br.com.qualix.tvwall.modulos.tv.model.Tv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TvRepository extends JpaRepository<Tv, String> {
    List<Tv> findByIdTenant(Long idTenant);
    List<Tv> findByIdGrupo(Long idGrupo);
    Optional<Tv> findByIdAndIdTenant(String id, Long idTenant);
}
