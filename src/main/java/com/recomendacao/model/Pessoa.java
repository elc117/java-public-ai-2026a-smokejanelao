package com.recomendacao.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma pessoa dentro de uma turma.
 *
 * As listas de habilidades, interesses e disponibilidades são preenchidas
 * pelo PessoaService ao buscar uma pessoa completa (não vêm do banco direto).
 */
public class Pessoa {

    private Long id;
    private String nome;
    private String email;
    private NivelExperiencia nivelExperiencia;
    private Long turmaId;

    // Listas carregadas sob demanda pelo service (não mapeadas diretamente no banco)
    private List<Habilidade> habilidades = new ArrayList<>();
    private List<Interesse> interesses = new ArrayList<>();
    private List<Disponibilidade> disponibilidades = new ArrayList<>();

    public Pessoa() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public NivelExperiencia getNivelExperiencia() { return nivelExperiencia; }
    public void setNivelExperiencia(NivelExperiencia nivelExperiencia) { this.nivelExperiencia = nivelExperiencia; }

    public Long getTurmaId() { return turmaId; }
    public void setTurmaId(Long turmaId) { this.turmaId = turmaId; }

    public List<Habilidade> getHabilidades() { return habilidades; }
    public void setHabilidades(List<Habilidade> habilidades) { this.habilidades = habilidades; }

    public List<Interesse> getInteresses() { return interesses; }
    public void setInteresses(List<Interesse> interesses) { this.interesses = interesses; }

    public List<Disponibilidade> getDisponibilidades() { return disponibilidades; }
    public void setDisponibilidades(List<Disponibilidade> disponibilidades) { this.disponibilidades = disponibilidades; }
}
