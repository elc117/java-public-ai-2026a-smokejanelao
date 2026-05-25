package com.recomendacao.model;

/** Disponibilidade horária de uma pessoa (dia da semana + turno). */
public class Disponibilidade {

    private Long id;
    private Long pessoaId;
    private DiaSemana diaSemana;
    private Turno turno;

    public Disponibilidade() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPessoaId() { return pessoaId; }
    public void setPessoaId(Long pessoaId) { this.pessoaId = pessoaId; }

    public DiaSemana getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DiaSemana diaSemana) { this.diaSemana = diaSemana; }

    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }
}
