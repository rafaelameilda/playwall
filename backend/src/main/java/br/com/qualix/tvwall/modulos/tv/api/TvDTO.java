package br.com.qualix.tvwall.modulos.tv.api;

import java.time.LocalDateTime;

public class TvDTO {

    private String id;
    private Long idTenant;
    private Long idAmbiente;
    private String nmAmbiente;
    private Long idGrupo;
    private String nmGrupo;
    private Integer nrPosicaoGrupo;
    private Long idPlaylist;
    private String nmPlaylist;
    private String nmTv;
    private String stStatus;
    private LocalDateTime dhCadastro;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getIdTenant() { return idTenant; }
    public void setIdTenant(Long idTenant) { this.idTenant = idTenant; }

    public Long getIdAmbiente() { return idAmbiente; }
    public void setIdAmbiente(Long idAmbiente) { this.idAmbiente = idAmbiente; }

    public String getNmAmbiente() { return nmAmbiente; }
    public void setNmAmbiente(String nmAmbiente) { this.nmAmbiente = nmAmbiente; }

    public Long getIdGrupo() { return idGrupo; }
    public void setIdGrupo(Long idGrupo) { this.idGrupo = idGrupo; }

    public String getNmGrupo() { return nmGrupo; }
    public void setNmGrupo(String nmGrupo) { this.nmGrupo = nmGrupo; }

    public Integer getNrPosicaoGrupo() { return nrPosicaoGrupo; }
    public void setNrPosicaoGrupo(Integer nrPosicaoGrupo) { this.nrPosicaoGrupo = nrPosicaoGrupo; }

    public Long getIdPlaylist() { return idPlaylist; }
    public void setIdPlaylist(Long idPlaylist) { this.idPlaylist = idPlaylist; }

    public String getNmPlaylist() { return nmPlaylist; }
    public void setNmPlaylist(String nmPlaylist) { this.nmPlaylist = nmPlaylist; }

    public String getNmTv() { return nmTv; }
    public void setNmTv(String nmTv) { this.nmTv = nmTv; }

    public String getStStatus() { return stStatus; }
    public void setStStatus(String stStatus) { this.stStatus = stStatus; }

    public LocalDateTime getDhCadastro() { return dhCadastro; }
    public void setDhCadastro(LocalDateTime dhCadastro) { this.dhCadastro = dhCadastro; }
}
