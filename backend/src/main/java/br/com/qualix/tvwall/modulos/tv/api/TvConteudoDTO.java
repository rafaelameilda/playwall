package br.com.qualix.tvwall.modulos.tv.api;

import java.time.LocalDateTime;

public class TvConteudoDTO {

    private Long id;
    private Long idTenant;
    private String nmConteudo;
    private String tpConteudo;
    private String dsUrlArquivo;
    private LocalDateTime dhCadastro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdTenant() { return idTenant; }
    public void setIdTenant(Long idTenant) { this.idTenant = idTenant; }

    public String getNmConteudo() { return nmConteudo; }
    public void setNmConteudo(String nmConteudo) { this.nmConteudo = nmConteudo; }

    public String getTpConteudo() { return tpConteudo; }
    public void setTpConteudo(String tpConteudo) { this.tpConteudo = tpConteudo; }

    public String getDsUrlArquivo() { return dsUrlArquivo; }
    public void setDsUrlArquivo(String dsUrlArquivo) { this.dsUrlArquivo = dsUrlArquivo; }

    public LocalDateTime getDhCadastro() { return dhCadastro; }
    public void setDhCadastro(LocalDateTime dhCadastro) { this.dhCadastro = dhCadastro; }
}
