package br.com.qualix.tvwall.modulos.tv.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tv_ambiente")
public class TvAmbiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idTenant;

    private String nmAmbiente;

    private String stStatus;

    private LocalDateTime dhCadastro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdTenant() { return idTenant; }
    public void setIdTenant(Long idTenant) { this.idTenant = idTenant; }

    public String getNmAmbiente() { return nmAmbiente; }
    public void setNmAmbiente(String nmAmbiente) { this.nmAmbiente = nmAmbiente; }

    public String getStStatus() { return stStatus; }
    public void setStStatus(String stStatus) { this.stStatus = stStatus; }

    public LocalDateTime getDhCadastro() { return dhCadastro; }
    public void setDhCadastro(LocalDateTime dhCadastro) { this.dhCadastro = dhCadastro; }
}
