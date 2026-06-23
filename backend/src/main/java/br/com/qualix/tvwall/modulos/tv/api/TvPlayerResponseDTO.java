package br.com.qualix.tvwall.modulos.tv.api;

import java.util.List;

public class TvPlayerResponseDTO {

    private String tvId;
    private String nmTv;
    private Long idGrupo;
    private Integer nrPosicaoGrupo;
    private Integer nrColunas;
    private Integer nrLinhas;
    private Long idPlaylist;
    private String nmPlaylist;
    private List<TvPlayerItemDTO> itens;
    private Long serverTimeMs;

    public TvPlayerResponseDTO() {}

    public String getTvId() { return tvId; }
    public void setTvId(String tvId) { this.tvId = tvId; }

    public String getNmTv() { return nmTv; }
    public void setNmTv(String nmTv) { this.nmTv = nmTv; }

    public Long getIdGrupo() { return idGrupo; }
    public void setIdGrupo(Long idGrupo) { this.idGrupo = idGrupo; }

    public Integer getNrPosicaoGrupo() { return nrPosicaoGrupo; }
    public void setNrPosicaoGrupo(Integer nrPosicaoGrupo) { this.nrPosicaoGrupo = nrPosicaoGrupo; }

    public Integer getNrColunas() { return nrColunas; }
    public void setNrColunas(Integer nrColunas) { this.nrColunas = nrColunas; }

    public Integer getNrLinhas() { return nrLinhas; }
    public void setNrLinhas(Integer nrLinhas) { this.nrLinhas = nrLinhas; }

    public Long getIdPlaylist() { return idPlaylist; }
    public void setIdPlaylist(Long idPlaylist) { this.idPlaylist = idPlaylist; }

    public String getNmPlaylist() { return nmPlaylist; }
    public void setNmPlaylist(String nmPlaylist) { this.nmPlaylist = nmPlaylist; }

    public List<TvPlayerItemDTO> getItens() { return itens; }
    public void setItens(List<TvPlayerItemDTO> itens) { this.itens = itens; }

    public Long getServerTimeMs() { return serverTimeMs; }
    public void setServerTimeMs(Long serverTimeMs) { this.serverTimeMs = serverTimeMs; }
}
