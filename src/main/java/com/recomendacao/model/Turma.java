package com.recomendacao.model;

/** Representa um grupo/turma de pessoas que serão divididas em equipes. */
public class Turma {

    private Long id;
    private String nome;
    private String descricao;

    public Turma() {}

    public Turma(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
