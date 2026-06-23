package br.com.qualix.tvwall.modulos.tv.controller;

import br.com.qualix.tvwall.api.ErrorResponse;
import br.com.qualix.tvwall.modulos.tv.api.TvConteudoDTO;
import br.com.qualix.tvwall.modulos.tv.model.TvConteudo;
import br.com.qualix.tvwall.modulos.tv.repository.TvConteudoRepository;
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
@RequestMapping("/tv/conteudos")
public class TvConteudoController {

    private static final Long TENANT_ID = 0L;

    private final TvConteudoRepository conteudoRepository;

    public TvConteudoController(TvConteudoRepository conteudoRepository) {
        this.conteudoRepository = conteudoRepository;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        List<TvConteudoDTO> list = conteudoRepository.findByIdTenant(TENANT_ID)
                .stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TvConteudoDTO dto) {
        TvConteudo c = new TvConteudo();
        c.setIdTenant(TENANT_ID);
        c.setNmConteudo(dto.getNmConteudo());
        c.setTpConteudo(dto.getTpConteudo());
        c.setDsUrlArquivo(dto.getDsUrlArquivo());
        c.setDhCadastro(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(conteudoRepository.save(c)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id,
                                        @RequestBody TvConteudoDTO dto,
                                        HttpServletRequest request) {
        Optional<TvConteudo> opt = conteudoRepository.findById(id);
        if (opt.isEmpty() || !TENANT_ID.equals(opt.get().getIdTenant())) {
            return notFound(request.getRequestURI(), "Conteúdo não encontrado.");
        }

        TvConteudo c = opt.get();
        if (dto.getNmConteudo() != null) c.setNmConteudo(dto.getNmConteudo());
        if (dto.getTpConteudo() != null) c.setTpConteudo(dto.getTpConteudo());
        if (dto.getDsUrlArquivo() != null) c.setDsUrlArquivo(dto.getDsUrlArquivo());
        return ResponseEntity.ok(toDTO(conteudoRepository.save(c)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id,
                                      HttpServletRequest request) {
        Optional<TvConteudo> opt = conteudoRepository.findById(id);
        if (opt.isEmpty() || !TENANT_ID.equals(opt.get().getIdTenant())) {
            return notFound(request.getRequestURI(), "Conteúdo não encontrado.");
        }

        conteudoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private TvConteudoDTO toDTO(TvConteudo c) {
        TvConteudoDTO dto = new TvConteudoDTO();
        dto.setId(c.getId());
        dto.setIdTenant(c.getIdTenant());
        dto.setNmConteudo(c.getNmConteudo());
        dto.setTpConteudo(c.getTpConteudo());
        dto.setDsUrlArquivo(c.getDsUrlArquivo());
        dto.setDhCadastro(c.getDhCadastro());
        return dto;
    }

    private ResponseEntity<ErrorResponse> notFound(String uri, String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(Instant.now(), 404, "Not Found", msg, uri));
    }
}
