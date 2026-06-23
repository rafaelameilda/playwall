package br.com.qualix.tvwall.modulos.tv.api;

public class TvPlaylistItemDTO {

    private Long id;
    private Long idPlaylist;
    private Long idConteudo;
    private String nmConteudo;
    private String tpConteudo;
    private Integer nrOrdem;
    private Integer qtSegundos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdPlaylist() { return idPlaylist; }
    public void setIdPlaylist(Long idPlaylist) { this.idPlaylist = idPlaylist; }

    public Long getIdConteudo() { return idConteudo; }
    public void setIdConteudo(Long idConteudo) { this.idConteudo = idConteudo; }

    public String getNmConteudo() { return nmConteudo; }
    public void setNmConteudo(String nmConteudo) { this.nmConteudo = nmConteudo; }

    public String getTpConteudo() { return tpConteudo; }
    public void setTpConteudo(String tpConteudo) { this.tpConteudo = tpConteudo; }

    public Integer getNrOrdem() { return nrOrdem; }
    public void setNrOrdem(Integer nrOrdem) { this.nrOrdem = nrOrdem; }

    public Integer getQtSegundos() { return qtSegundos; }
    public void setQtSegundos(Integer qtSegundos) { this.qtSegundos = qtSegundos; }
}
