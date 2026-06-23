package br.com.qualix.tvwall.modulos.tv.controller;

import br.com.qualix.tvwall.api.ErrorResponse;
import br.com.qualix.tvwall.modulos.tv.api.TvDTO;
import br.com.qualix.tvwall.modulos.tv.model.Tv;
import br.com.qualix.tvwall.modulos.tv.repository.TvAmbienteRepository;
import br.com.qualix.tvwall.modulos.tv.repository.TvGrupoRepository;
import br.com.qualix.tvwall.modulos.tv.repository.TvPlaylistRepository;
import br.com.qualix.tvwall.modulos.tv.repository.TvRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tv/tvs")
public class TvController {

    private static final Long TENANT_ID = 0L;

    private final TvRepository tvRepository;
    private final TvAmbienteRepository ambienteRepository;
    private final TvGrupoRepository grupoRepository;
    private final TvPlaylistRepository playlistRepository;

    public TvController(TvRepository tvRepository,
                         TvAmbienteRepository ambienteRepository,
                         TvGrupoRepository grupoRepository,
                         TvPlaylistRepository playlistRepository) {
        this.tvRepository = tvRepository;
        this.ambienteRepository = ambienteRepository;
        this.grupoRepository = grupoRepository;
        this.playlistRepository = playlistRepository;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        List<TvDTO> list = tvRepository.findByIdTenant(TENANT_ID)
                .stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TvDTO dto) {
        Tv tv = new Tv();
        tv.setId(UUID.randomUUID().toString());
        tv.setIdTenant(TENANT_ID);
        tv.setNmTv(dto.getNmTv());
        tv.setIdAmbiente(dto.getIdAmbiente());
        tv.setIdGrupo(dto.getIdGrupo());
        tv.setNrPosicaoGrupo(dto.getNrPosicaoGrupo());
        tv.setIdPlaylist(dto.getIdPlaylist());
        tv.setStStatus("A");
        tv.setDhCadastro(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(tvRepository.save(tv)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") String id,
                                        @RequestBody TvDTO dto,
                                        HttpServletRequest request) {
        Optional<Tv> opt = tvRepository.findByIdAndIdTenant(id, TENANT_ID);
        if (opt.isEmpty()) return notFound(request.getRequestURI(), "TV não encontrada.");

        Tv tv = opt.get();
        if (dto.getNmTv() != null) tv.setNmTv(dto.getNmTv());
        if (dto.getIdAmbiente() != null) tv.setIdAmbiente(dto.getIdAmbiente());
        if (dto.getIdGrupo() != null) tv.setIdGrupo(dto.getIdGrupo());
        if (dto.getNrPosicaoGrupo() != null) tv.setNrPosicaoGrupo(dto.getNrPosicaoGrupo());
        if (dto.getIdPlaylist() != null) tv.setIdPlaylist(dto.getIdPlaylist());
        if (dto.getStStatus() != null) tv.setStStatus(dto.getStStatus());
        return ResponseEntity.ok(toDTO(tvRepository.save(tv)));
    }

    @PutMapping("/{id}/playlist")
    public ResponseEntity<?> atribuirPlaylist(@PathVariable("id") String id,
                                               @RequestBody Map<String, Long> body,
                                               HttpServletRequest request) {
        Optional<Tv> opt = tvRepository.findByIdAndIdTenant(id, TENANT_ID);
        if (opt.isEmpty()) return notFound(request.getRequestURI(), "TV não encontrada.");

        Tv tv = opt.get();
        tv.setIdPlaylist(body.get("idPlaylist"));
        return ResponseEntity.ok(toDTO(tvRepository.save(tv)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") String id,
                                      HttpServletRequest request) {
        Optional<Tv> opt = tvRepository.findByIdAndIdTenant(id, TENANT_ID);
        if (opt.isEmpty()) return notFound(request.getRequestURI(), "TV não encontrada.");

        Tv tv = opt.get();
        tv.setStStatus("I");
        tvRepository.save(tv);
        return ResponseEntity.noContent().build();
    }

    private TvDTO toDTO(Tv t) {
        TvDTO dto = new TvDTO();
        dto.setId(t.getId());
        dto.setIdTenant(t.getIdTenant());
        dto.setNmTv(t.getNmTv());
        dto.setIdAmbiente(t.getIdAmbiente());
        dto.setIdGrupo(t.getIdGrupo());
        dto.setNrPosicaoGrupo(t.getNrPosicaoGrupo());
        dto.setIdPlaylist(t.getIdPlaylist());
        dto.setStStatus(t.getStStatus());
        dto.setDhCadastro(t.getDhCadastro());

        if (t.getIdAmbiente() != null) {
            ambienteRepository.findById(t.getIdAmbiente())
                    .ifPresent(a -> dto.setNmAmbiente(a.getNmAmbiente()));
        }
        if (t.getIdGrupo() != null) {
            grupoRepository.findById(t.getIdGrupo())
                    .ifPresent(g -> dto.setNmGrupo(g.getNmGrupo()));
        }
        if (t.getIdPlaylist() != null) {
            playlistRepository.findById(t.getIdPlaylist())
                    .ifPresent(p -> dto.setNmPlaylist(p.getNmPlaylist()));
        }
        return dto;
    }

    private ResponseEntity<ErrorResponse> notFound(String uri, String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(Instant.now(), 404, "Not Found", msg, uri));
    }
}
