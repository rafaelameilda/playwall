package br.com.qualix.tvwall.modulos.tv.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tv_grupo")
public class TvGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idTenant;

    private String nmGrupo;

    private Integer nrColunas;

    private Integer nrLinhas;

    private String stStatus;

    private LocalDateTime dhCadastro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdTenant() { return idTenant; }
    public void setIdTenant(Long idTenant) { this.idTenant = idTenant; }

    public String getNmGrupo() { return nmGrupo; }
    public void setNmGrupo(String nmGrupo) { this.nmGrupo = nmGrupo; }

    public Integer getNrColunas() { return nrColunas; }
    public void setNrColunas(Integer nrColunas) { this.nrColunas = nrColunas; }

    public Integer getNrLinhas() { return nrLinhas; }
    public void setNrLinhas(Integer nrLinhas) { this.nrLinhas = nrLinhas; }

    public String getStStatus() { return stStatus; }
    public void setStStatus(String stStatus) { this.stStatus = stStatus; }

    public LocalDateTime getDhCadastro() { return dhCadastro; }
    public void setDhCadastro(LocalDateTime dhCadastro) { this.dhCadastro = dhCadastro; }
}
