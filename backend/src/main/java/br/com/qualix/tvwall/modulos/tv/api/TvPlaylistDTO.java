package br.com.qualix.tvwall.modulos.tv.api;

import java.time.LocalDateTime;
import java.util.List;

public class TvPlaylistDTO {

    private Long id;
    private Long idTenant;
    private String nmPlaylist;
    private String stStatus;
    private LocalDateTime dhCadastro;
    private List<TvPlaylistItemDTO> itens;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdTenant() { return idTenant; }
    public void setIdTenant(Long idTenant) { this.idTenant = idTenant; }

    public String getNmPlaylist() { return nmPlaylist; }
    public void setNmPlaylist(String nmPlaylist) { this.nmPlaylist = nmPlaylist; }

    public String getStStatus() { return stStatus; }
    public void setStStatus(String stStatus) { this.stStatus = stStatus; }

    public LocalDateTime getDhCadastro() { return dhCadastro; }
    public void setDhCadastro(LocalDateTime dhCadastro) { this.dhCadastro = dhCadastro; }

    public List<TvPlaylistItemDTO> getItens() { return itens; }
    public void setItens(List<TvPlaylistItemDTO> itens) { this.itens = itens; }
}
