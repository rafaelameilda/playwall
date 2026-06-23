package br.com.qualix.tvwall.modulos.tv.controller;

import br.com.qualix.tvwall.api.ErrorResponse;
import br.com.qualix.tvwall.modulos.tv.api.TvGrupoDTO;
import br.com.qualix.tvwall.modulos.tv.model.TvGrupo;
import br.com.qualix.tvwall.modulos.tv.repository.TvGrupoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tv/grupos")
public class TvGrupoController {

    private static final Long TENANT_ID = 0L;

    private final TvGrupoRepository grupoRepository;

    public TvGrupoController(TvGrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        List<TvGrupoDTO> list = grupoRepository.findByIdTenant(TENANT_ID)
                .stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TvGrupoDTO dto) {
        TvGrupo grupo = new TvGrupo();
        grupo.setIdTenant(TENANT_ID);
        grupo.setNmGrupo(dto.getNmGrupo());
        grupo.setNrColunas(dto.getNrColunas() != null ? dto.getNrColunas() : 1);
        grupo.setNrLinhas(dto.getNrLinhas() != null ? dto.getNrLinhas() : 1);
        grupo.setStStatus("A");
        grupo.setDhCadastro(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(grupoRepository.save(grupo)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id,
                                        @RequestBody TvGrupoDTO dto,
                                        HttpServletRequest request) {
        Optional<TvGrupo> opt = grupoRepository.findById(id);
        if (opt.isEmpty() || !TENANT_ID.equals(opt.get().getIdTenant())) {
            return notFound(request.getRequestURI(), "Grupo não encontrado.");
        }

        TvGrupo grupo = opt.get();
        if (dto.getNmGrupo() != null) grupo.setNmGrupo(dto.getNmGrupo());
        if (dto.getNrColunas() != null) grupo.setNrColunas(dto.getNrColunas());
        if (dto.getNrLinhas() != null) grupo.setNrLinhas(dto.getNrLinhas());
        if (dto.getStStatus() != null) grupo.setStStatus(dto.getStStatus());
        return ResponseEntity.ok(toDTO(grupoRepository.save(grupo)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id,
                                      HttpServletRequest request) {
        Optional<TvGrupo> opt = grupoRepository.findById(id);
        if (opt.isEmpty() || !TENANT_ID.equals(opt.get().getIdTenant())) {
            return notFound(request.getRequestURI(), "Grupo não encontrado.");
        }

        TvGrupo grupo = opt.get();
        grupo.setStStatus("I");
        grupoRepository.save(grupo);
        return ResponseEntity.noContent().build();
    }

    private TvGrupoDTO toDTO(TvGrupo g) {
        TvGrupoDTO dto = new TvGrupoDTO();
        dto.setId(g.getId());
        dto.setIdTenant(g.getIdTenant());
        dto.setNmGrupo(g.getNmGrupo());
        dto.setNrColunas(g.getNrColunas());
        dto.setNrLinhas(g.getNrLinhas());
        dto.setStStatus(g.getStStatus());
        dto.setDhCadastro(g.getDhCadastro());
        return dto;
    }

    private ResponseEntity<ErrorResponse> notFound(String uri, String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(Instant.now(), 404, "Not Found", msg, uri));
    }
}
