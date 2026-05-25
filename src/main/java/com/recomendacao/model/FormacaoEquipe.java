package com.recomendacao.model;

/**
 * Configuração de uma formação de equipes.
 *
 * Pode especificar tamanhoEquipe OU numeroEquipes:
 * - tamanhoEquipe: quantas pessoas por equipe
 * - numeroEquipes: quantas equipes criar (o service calcula o tamanho automaticamente)
 *
 * estrategia: ALEATORIA, NIVEL, HABILIDADES, INTERESSES, DISPONIBILIDADE
 */
public class FormacaoEquipe {

    private Long id;
    private Long turmaId;
    private String nome;
    private Integer tamanhoEquipe;
    private Integer numeroEquipes;
    private String estrategia;
    private String dataCriacao;

    public FormacaoEquipe() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTurmaId() { return turmaId; }
    public void setTurmaId(Long turmaId) { this.turmaId = turmaId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getTamanhoEquipe() { return tamanhoEquipe; }
    public void setTamanhoEquipe(Integer tamanhoEquipe) { this.tamanhoEquipe = tamanhoEquipe; }

    public Integer getNumeroEquipes() { return numeroEquipes; }
    public void setNumeroEquipes(Integer numeroEquipes) { this.numeroEquipes = numeroEquipes; }

    public String getEstrategia() { return estrategia; }
    public void setEstrategia(String estrategia) { this.estrategia = estrategia; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }
}
