package br.com.qualix.tvwall.modulos.tv.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tv")
public class Tv {

    @Id
    private String id;

    private Long idTenant;

    private Long idAmbiente;

    private Long idGrupo;

    private Integer nrPosicaoGrupo;

    private Long idPlaylist;

    private String nmTv;

    private String stStatus;

    private LocalDateTime dhCadastro;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getIdTenant() { return idTenant; }
    public void setIdTenant(Long idTenant) { this.idTenant = idTenant; }

    public Long getIdAmbiente() { return idAmbiente; }
    public void setIdAmbiente(Long idAmbiente) { this.idAmbiente = idAmbiente; }

    public Long getIdGrupo() { return idGrupo; }
    public void setIdGrupo(Long idGrupo) { this.idGrupo = idGrupo; }

    public Integer getNrPosicaoGrupo() { return nrPosicaoGrupo; }
    public void setNrPosicaoGrupo(Integer nrPosicaoGrupo) { this.nrPosicaoGrupo = nrPosicaoGrupo; }

    public Long getIdPlaylist() { return idPlaylist; }
    public void setIdPlaylist(Long idPlaylist) { this.idPlaylist = idPlaylist; }

    public String getNmTv() { return nmTv; }
    public void setNmTv(String nmTv) { this.nmTv = nmTv; }

    public String getStStatus() { return stStatus; }
    public void setStStatus(String stStatus) { this.stStatus = stStatus; }

    public LocalDateTime getDhCadastro() { return dhCadastro; }
    public void setDhCadastro(LocalDateTime dhCadastro) { this.dhCadastro = dhCadastro; }
}
