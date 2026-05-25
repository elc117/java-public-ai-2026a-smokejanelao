package com.recomendacao.model;

import java.util.ArrayList;
import java.util.List;

/** Uma equipe gerada dentro de uma formação. */
public class Equipe {

    private Long id;
    private Long formacaoId;
    private String nome;

    // Membros carregados pelo service ao listar equipes
    private List<MembroEquipe> membros = new ArrayList<>();

    public Equipe() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFormacaoId() { return formacaoId; }
    public void setFormacaoId(Long formacaoId) { this.formacaoId = formacaoId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<MembroEquipe> getMembros() { return membros; }
    public void setMembros(List<MembroEquipe> membros) { this.membros = membros; }
}
