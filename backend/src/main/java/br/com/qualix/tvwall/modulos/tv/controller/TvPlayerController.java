package br.com.qualix.tvwall.modulos.tv.controller;

import br.com.qualix.tvwall.api.ErrorResponse;
import br.com.qualix.tvwall.modulos.tv.api.TvPlayerItemDTO;
import br.com.qualix.tvwall.modulos.tv.api.TvPlayerResponseDTO;
import br.com.qualix.tvwall.modulos.tv.model.Tv;
import br.com.qualix.tvwall.modulos.tv.model.TvConteudo;
import br.com.qualix.tvwall.modulos.tv.model.TvGrupo;
import br.com.qualix.tvwall.modulos.tv.model.TvPlaylist;
import br.com.qualix.tvwall.modulos.tv.model.TvPlaylistItem;
import br.com.qualix.tvwall.modulos.tv.repository.TvConteudoRepository;
import br.com.qualix.tvwall.modulos.tv.repository.TvGrupoRepository;
import br.com.qualix.tvwall.modulos.tv.repository.TvPlaylistItemRepository;
import br.com.qualix.tvwall.modulos.tv.repository.TvPlaylistRepository;
import br.com.qualix.tvwall.modulos.tv.repository.TvRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tv/player")
public class TvPlayerController {

    private final TvRepository tvRepository;
    private final TvPlaylistRepository tvPlaylistRepository;
    private final TvPlaylistItemRepository tvPlaylistItemRepository;
    private final TvConteudoRepository tvConteudoRepository;
    private final TvGrupoRepository tvGrupoRepository;

    public TvPlayerController(TvRepository tvRepository,
                               TvPlaylistRepository tvPlaylistRepository,
                               TvPlaylistItemRepository tvPlaylistItemRepository,
                               TvConteudoRepository tvConteudoRepository,
                               TvGrupoRepository tvGrupoRepository) {
        this.tvRepository = tvRepository;
        this.tvPlaylistRepository = tvPlaylistRepository;
        this.tvPlaylistItemRepository = tvPlaylistItemRepository;
        this.tvConteudoRepository = tvConteudoRepository;
        this.tvGrupoRepository = tvGrupoRepository;
    }

    @GetMapping("/{tvId}")
    public ResponseEntity<?> getPlayerData(@PathVariable("tvId") String tvId,
                                            HttpServletRequest request) {
        Optional<Tv> tvOpt = tvRepository.findById(tvId);
        if (tvOpt.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    Instant.now(),
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    "TV não encontrada: " + tvId,
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        Tv tv = tvOpt.get();

        if (!"A".equals(tv.getStStatus())) {
            ErrorResponse error = new ErrorResponse(
                    Instant.now(),
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    "TV inativa: " + tvId,
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        TvPlayerResponseDTO response = new TvPlayerResponseDTO();
        response.setServerTimeMs(System.currentTimeMillis());
        response.setTvId(tv.getId());
        response.setNmTv(tv.getNmTv());
        response.setNrPosicaoGrupo(tv.getNrPosicaoGrupo());

        if (tv.getIdGrupo() != null) {
            response.setIdGrupo(tv.getIdGrupo());
            tvGrupoRepository.findById(tv.getIdGrupo()).ifPresent(grupo -> {
                response.setNrColunas(grupo.getNrColunas());
                response.setNrLinhas(grupo.getNrLinhas());
            });
        }

        if (tv.getIdPlaylist() == null) {
            response.setItens(new ArrayList<>());
            return ResponseEntity.ok(response);
        }

        Optional<TvPlaylist> playlistOpt = tvPlaylistRepository.findById(tv.getIdPlaylist());
        if (playlistOpt.isEmpty()) {
            response.setItens(new ArrayList<>());
            return ResponseEntity.ok(response);
        }

        TvPlaylist playlist = playlistOpt.get();
        response.setIdPlaylist(playlist.getId());
        response.setNmPlaylist(playlist.getNmPlaylist());

        List<TvPlaylistItem> itensRaw = tvPlaylistItemRepository
                .findByIdPlaylistOrderByNrOrdemAsc(playlist.getId());

        List<TvPlayerItemDTO> itensDTOs = new ArrayList<>();
        for (TvPlaylistItem item : itensRaw) {
            Optional<TvConteudo> conteudoOpt = tvConteudoRepository.findById(item.getIdConteudo());
            if (conteudoOpt.isPresent()) {
                TvConteudo c = conteudoOpt.get();
                itensDTOs.add(new TvPlayerItemDTO(
                        c.getId(),
                        c.getNmConteudo(),
                        c.getTpConteudo(),
                        c.getDsUrlArquivo(),
                        item.getQtSegundos(),
                        item.getNrOrdem()
                ));
            }
        }

        response.setItens(itensDTOs);
        return ResponseEntity.ok(response);
    }
}
