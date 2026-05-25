package com.recomendacao.model;

/** Área de interesse predefinida de uma pessoa. */
public class Interesse {

    private Long id;
    private Long pessoaId;
    private TipoInteresse tipo;

    public Interesse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPessoaId() { return pessoaId; }
    public void setPessoaId(Long pessoaId) { this.pessoaId = pessoaId; }

    public TipoInteresse getTipo() { return tipo; }
    public void setTipo(TipoInteresse tipo) { this.tipo = tipo; }
}
