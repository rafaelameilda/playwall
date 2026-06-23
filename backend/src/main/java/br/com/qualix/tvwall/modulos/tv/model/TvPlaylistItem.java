package br.com.qualix.tvwall.modulos.tv.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tv_playlist_item")
public class TvPlaylistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idPlaylist;

    private Long idConteudo;

    private Integer nrOrdem;

    private Integer qtSegundos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdPlaylist() { return idPlaylist; }
    public void setIdPlaylist(Long idPlaylist) { this.idPlaylist = idPlaylist; }

    public Long getIdConteudo() { return idConteudo; }
    public void setIdConteudo(Long idConteudo) { this.idConteudo = idConteudo; }

    public Integer getNrOrdem() { return nrOrdem; }
    public void setNrOrdem(Integer nrOrdem) { this.nrOrdem = nrOrdem; }

    public Integer getQtSegundos() { return qtSegundos; }
    public void setQtSegundos(Integer qtSegundos) { this.qtSegundos = qtSegundos; }
}
