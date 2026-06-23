package br.com.qualix.tvwall.modulos.tv.api;

public class TvPlayerItemDTO {

    private Long idConteudo;
    private String nmConteudo;
    private String tpConteudo;
    private String dsUrlArquivo;
    private Integer qtSegundos;
    private Integer nrOrdem;

    public TvPlayerItemDTO() {}

    public TvPlayerItemDTO(Long idConteudo, String nmConteudo, String tpConteudo,
                           String dsUrlArquivo, Integer qtSegundos, Integer nrOrdem) {
        this.idConteudo = idConteudo;
        this.nmConteudo = nmConteudo;
        this.tpConteudo = tpConteudo;
        this.dsUrlArquivo = dsUrlArquivo;
        this.qtSegundos = qtSegundos;
        this.nrOrdem = nrOrdem;
    }

    public Long getIdConteudo() { return idConteudo; }
    public void setIdConteudo(Long idConteudo) { this.idConteudo = idConteudo; }

    public String getNmConteudo() { return nmConteudo; }
    public void setNmConteudo(String nmConteudo) { this.nmConteudo = nmConteudo; }

    public String getTpConteudo() { return tpConteudo; }
    public void setTpConteudo(String tpConteudo) { this.tpConteudo = tpConteudo; }

    public String getDsUrlArquivo() { return dsUrlArquivo; }
    public void setDsUrlArquivo(String dsUrlArquivo) { this.dsUrlArquivo = dsUrlArquivo; }

    public Integer getQtSegundos() { return qtSegundos; }
    public void setQtSegundos(Integer qtSegundos) { this.qtSegundos = qtSegundos; }

    public Integer getNrOrdem() { return nrOrdem; }
    public void setNrOrdem(Integer nrOrdem) { this.nrOrdem = nrOrdem; }
}
