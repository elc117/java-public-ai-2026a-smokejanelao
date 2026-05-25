package com.recomendacao.model;

/** Habilidade técnica de uma pessoa com categoria e nível predefinidos. */
public class Habilidade {

    private Long id;
    private Long pessoaId;
    private CategoriaHabilidade categoria;
    private NivelHabilidade nivel;

    public Habilidade() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPessoaId() { return pessoaId; }
    public void setPessoaId(Long pessoaId) { this.pessoaId = pessoaId; }

    public CategoriaHabilidade getCategoria() { return categoria; }
    public void setCategoria(CategoriaHabilidade categoria) { this.categoria = categoria; }

    public NivelHabilidade getNivel() { return nivel; }
    public void setNivel(NivelHabilidade nivel) { this.nivel = nivel; }
}
