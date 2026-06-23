package br.com.qualix.tvwall.modulos.tv.controller;

import br.com.qualix.tvwall.api.ErrorResponse;
import br.com.qualix.tvwall.modulos.tv.api.TvAmbienteDTO;
import br.com.qualix.tvwall.modulos.tv.model.TvAmbiente;
import br.com.qualix.tvwall.modulos.tv.repository.TvAmbienteRepository;
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
@RequestMapping("/tv/ambientes")
public class TvAmbienteController {

    private static final Long TENANT_ID = 0L;

    private final TvAmbienteRepository ambienteRepository;

    public TvAmbienteController(TvAmbienteRepository ambienteRepository) {
        this.ambienteRepository = ambienteRepository;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        List<TvAmbienteDTO> list = ambienteRepository.findByIdTenant(TENANT_ID)
                .stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TvAmbienteDTO dto) {
        TvAmbiente amb = new TvAmbiente();
        amb.setIdTenant(TENANT_ID);
        amb.setNmAmbiente(dto.getNmAmbiente());
        amb.setStStatus("A");
        amb.setDhCadastro(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(ambienteRepository.save(amb)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id,
                                        @RequestBody TvAmbienteDTO dto,
                                        HttpServletRequest request) {
        Optional<TvAmbiente> opt = ambienteRepository.findById(id);
        if (opt.isEmpty() || !TENANT_ID.equals(opt.get().getIdTenant())) {
            return notFound(request.getRequestURI(), "Ambiente não encontrado.");
        }

        TvAmbiente amb = opt.get();
        if (dto.getNmAmbiente() != null) amb.setNmAmbiente(dto.getNmAmbiente());
        if (dto.getStStatus() != null) amb.setStStatus(dto.getStStatus());
        return ResponseEntity.ok(toDTO(ambienteRepository.save(amb)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id,
                                      HttpServletRequest request) {
        Optional<TvAmbiente> opt = ambienteRepository.findById(id);
        if (opt.isEmpty() || !TENANT_ID.equals(opt.get().getIdTenant())) {
            return notFound(request.getRequestURI(), "Ambiente não encontrado.");
        }

        TvAmbiente amb = opt.get();
        amb.setStStatus("I");
        ambienteRepository.save(amb);
        return ResponseEntity.noContent().build();
    }

    private TvAmbienteDTO toDTO(TvAmbiente a) {
        TvAmbienteDTO dto = new TvAmbienteDTO();
        dto.setId(a.getId());
        dto.setIdTenant(a.getIdTenant());
        dto.setNmAmbiente(a.getNmAmbiente());
        dto.setStStatus(a.getStStatus());
        dto.setDhCadastro(a.getDhCadastro());
        return dto;
    }

    private ResponseEntity<ErrorResponse> notFound(String uri, String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(Instant.now(), 404, "Not Found", msg, uri));
    }
}
