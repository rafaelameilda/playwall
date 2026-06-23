package br.com.qualix.tvwall.modulos.tv.repository;

import br.com.qualix.tvwall.modulos.tv.model.TvConteudo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TvConteudoRepository extends JpaRepository<TvConteudo, Long> {
    List<TvConteudo> findByIdTenant(Long idTenant);
}
