package br.com.qualix.tvwall.modulos.tv.controller;

import br.com.qualix.tvwall.api.ErrorResponse;
import br.com.qualix.tvwall.modulos.tv.api.TvPlaylistDTO;
import br.com.qualix.tvwall.modulos.tv.api.TvPlaylistItemDTO;
import br.com.qualix.tvwall.modulos.tv.model.TvConteudo;
import br.com.qualix.tvwall.modulos.tv.model.TvPlaylist;
import br.com.qualix.tvwall.modulos.tv.model.TvPlaylistItem;
import br.com.qualix.tvwall.modulos.tv.repository.TvConteudoRepository;
import br.com.qualix.tvwall.modulos.tv.repository.TvPlaylistItemRepository;
import br.com.qualix.tvwall.modulos.tv.repository.TvPlaylistRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tv/playlists")
public class TvPlaylistController {

    private static final Long TENANT_ID = 0L;

    private final TvPlaylistRepository playlistRepository;
    private final TvPlaylistItemRepository itemRepository;
    private final TvConteudoRepository conteudoRepository;

    public TvPlaylistController(TvPlaylistRepository playlistRepository,
                                 TvPlaylistItemRepository itemRepository,
                                 TvConteudoRepository conteudoRepository) {
        this.playlistRepository = playlistRepository;
        this.itemRepository = itemRepository;
        this.conteudoRepository = conteudoRepository;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        List<TvPlaylistDTO> list = playlistRepository.findByIdTenant(TENANT_ID)
                .stream().map(p -> toDTO(p, false)).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable("id") Long id,
                                     HttpServletRequest request) {
        Optional<TvPlaylist> opt = playlistRepository.findById(id);
        if (opt.isEmpty() || !TENANT_ID.equals(opt.get().getIdTenant())) {
            return notFound(request.getRequestURI(), "Playlist não encontrada.");
        }
        return ResponseEntity.ok(toDTO(opt.get(), true));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TvPlaylistDTO dto) {
        TvPlaylist p = new TvPlaylist();
        p.setIdTenant(TENANT_ID);
        p.setNmPlaylist(dto.getNmPlaylist());
        p.setStStatus("A");
        p.setDhCadastro(LocalDateTime.now());
        TvPlaylist saved = playlistRepository.save(p);

        if (dto.getItens() != null) {
            salvarItens(saved.getId(), dto.getItens());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved, true));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id,
                                        @RequestBody TvPlaylistDTO dto,
                                        HttpServletRequest request) {
        Optional<TvPlaylist> opt = playlistRepository.findById(id);
        if (opt.isEmpty() || !TENANT_ID.equals(opt.get().getIdTenant())) {
            return notFound(request.getRequestURI(), "Playlist não encontrada.");
        }

        TvPlaylist p = opt.get();
        if (dto.getNmPlaylist() != null) p.setNmPlaylist(dto.getNmPlaylist());
        if (dto.getStStatus() != null) p.setStStatus(dto.getStStatus());
        playlistRepository.save(p);

        if (dto.getItens() != null) {
            itemRepository.deleteByIdPlaylist(id);
            salvarItens(id, dto.getItens());
        }

        return ResponseEntity.ok(toDTO(p, true));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable("id") Long id,
                                      HttpServletRequest request) {
        Optional<TvPlaylist> opt = playlistRepository.findById(id);
        if (opt.isEmpty() || !TENANT_ID.equals(opt.get().getIdTenant())) {
            return notFound(request.getRequestURI(), "Playlist não encontrada.");
        }

        itemRepository.deleteByIdPlaylist(id);
        playlistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void salvarItens(Long idPlaylist, List<TvPlaylistItemDTO> itens) {
        int ordem = 0;
        for (TvPlaylistItemDTO itemDTO : itens) {
            TvPlaylistItem item = new TvPlaylistItem();
            item.setIdPlaylist(idPlaylist);
            item.setIdConteudo(itemDTO.getIdConteudo());
            item.setNrOrdem(itemDTO.getNrOrdem() != null ? itemDTO.getNrOrdem() : ordem);
            item.setQtSegundos(itemDTO.getQtSegundos() != null ? itemDTO.getQtSegundos() : 10);
            itemRepository.save(item);
            ordem++;
        }
    }

    private TvPlaylistDTO toDTO(TvPlaylist p, boolean comItens) {
        TvPlaylistDTO dto = new TvPlaylistDTO();
        dto.setId(p.getId());
        dto.setIdTenant(p.getIdTenant());
        dto.setNmPlaylist(p.getNmPlaylist());
        dto.setStStatus(p.getStStatus());
        dto.setDhCadastro(p.getDhCadastro());

        if (comItens) {
            List<TvPlaylistItem> rawItens = itemRepository.findByIdPlaylistOrderByNrOrdemAsc(p.getId());
            List<TvPlaylistItemDTO> itensDTOs = new ArrayList<>();
            for (TvPlaylistItem item : rawItens) {
                TvPlaylistItemDTO idto = new TvPlaylistItemDTO();
                idto.setId(item.getId());
                idto.setIdPlaylist(item.getIdPlaylist());
                idto.setIdConteudo(item.getIdConteudo());
                idto.setNrOrdem(item.getNrOrdem());
                idto.setQtSegundos(item.getQtSegundos());
                conteudoRepository.findById(item.getIdConteudo()).ifPresent(c -> {
                    idto.setNmConteudo(c.getNmConteudo());
                    idto.setTpConteudo(c.getTpConteudo());
                });
                itensDTOs.add(idto);
            }
            dto.setItens(itensDTOs);
        }

        return dto;
    }

    private ResponseEntity<ErrorResponse> notFound(String uri, String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(Instant.now(), 404, "Not Found", msg, uri));
    }
}
