package br.com.qualix.tvwall.modulos.tv.repository;

import br.com.qualix.tvwall.modulos.tv.model.TvPlaylistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TvPlaylistItemRepository extends JpaRepository<TvPlaylistItem, Long> {
    List<TvPlaylistItem> findByIdPlaylistOrderByNrOrdemAsc(Long idPlaylist);
    void deleteByIdPlaylist(Long idPlaylist);
}
