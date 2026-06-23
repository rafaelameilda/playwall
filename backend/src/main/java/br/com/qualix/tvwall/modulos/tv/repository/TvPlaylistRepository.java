package br.com.qualix.tvwall.modulos.tv.repository;

import br.com.qualix.tvwall.modulos.tv.model.TvPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TvPlaylistRepository extends JpaRepository<TvPlaylist, Long> {
    List<TvPlaylist> findByIdTenant(Long idTenant);
    List<TvPlaylist> findByIdTenantAndStStatus(Long idTenant, String stStatus);
}
