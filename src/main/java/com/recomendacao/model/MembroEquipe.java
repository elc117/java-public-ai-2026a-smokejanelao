package com.recomendacao.model;

/** Vínculo entre uma pessoa e uma equipe, com função opcional (ex: "Líder"). */
public class MembroEquipe {

    private Long id;
    private Long equipeId;
    private Long pessoaId;
    private String funcao;

    // Nome da pessoa, preenchido pelo service para facilitar exibição no frontend
    private String nomePessoa;

    public MembroEquipe() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEquipeId() { return equipeId; }
    public void setEquipeId(Long equipeId) { this.equipeId = equipeId; }

    public Long getPessoaId() { return pessoaId; }
    public void setPessoaId(Long pessoaId) { this.pessoaId = pessoaId; }

    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }

    public String getNomePessoa() { return nomePessoa; }
    public void setNomePessoa(String nomePessoa) { this.nomePessoa = nomePessoa; }
}
