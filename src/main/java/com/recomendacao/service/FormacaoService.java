package com.recomendacao.service;

import com.recomendacao.estrategia.EstrategiaRecomendacao;
import com.recomendacao.model.*;
import com.recomendacao.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Regras de negócio para formações de equipes.
 *
 * Este service é o coração do sistema: coordena a busca de pessoas,
 * a aplicação da estratégia escolhida e a persistência do resultado.
 */
public class FormacaoService {

    private final FormacaoRepository formacaoRepository;
    private final EquipeRepository equipeRepository;
    private final MembroEquipeRepository membroRepository;
    private final PessoaService pessoaService;

    public FormacaoService(FormacaoRepository formacaoRepository,
                           EquipeRepository equipeRepository,
                           MembroEquipeRepository membroRepository,
                           PessoaService pessoaService) {
        this.formacaoRepository = formacaoRepository;
        this.equipeRepository = equipeRepository;
        this.membroRepository = membroRepository;
        this.pessoaService = pessoaService;
    }

    /** Persiste a configuração da formação (sem gerar equipes ainda). */
    public FormacaoEquipe criarFormacao(FormacaoEquipe formacao) {
        if (formacao.getEstrategia() == null || formacao.getEstrategia().isBlank()) {
            throw new IllegalArgumentException("A estratégia é obrigatória.");
        }
        if (formacao.getTamanhoEquipe() == null && formacao.getNumeroEquipes() == null) {
            throw new IllegalArgumentException("Informe tamanhoEquipe OU numeroEquipes.");
        }
        // Valida se a estratégia existe antes de salvar
        RecomendadorEquipes.obterEstrategia(formacao.getEstrategia());

        formacao.setDataCriacao(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return formacaoRepository.salvar(formacao);
    }

    public FormacaoEquipe buscarFormacao(Long id) {
        FormacaoEquipe f = formacaoRepository.buscarPorId(id);
        if (f == null) throw new IllegalArgumentException("Formação não encontrada: id=" + id);
        return f;
    }

    /**
     * Gera as equipes para uma formação já cadastrada.
     *
     * Fluxo:
     * 1. Carrega a formação e todas as pessoas da turma (com atributos)
     * 2. Calcula o tamanho de equipe (se foi informado numeroEquipes, converte)
     * 3. Aplica a estratégia escolhida
     * 4. Persiste equipes e membros no banco
     * 5. Retorna as equipes populadas
     */
    public List<Equipe> gerarEquipes(Long formacaoId) {
        FormacaoEquipe formacao = buscarFormacao(formacaoId);

        // Carrega pessoas com todos os atributos (necessário para estratégias como INTERESSES)
        List<Pessoa> todasPessoas = pessoaService.listarPessoasDaTurma(formacao.getTurmaId());
        if (todasPessoas.isEmpty()) {
            throw new IllegalStateException("A turma não possui pessoas cadastradas.");
        }

        // Preenche atributos de cada pessoa
        List<Pessoa> pessoasCompletas = new ArrayList<>();
        for (Pessoa p : todasPessoas) {
            pessoasCompletas.add(pessoaService.buscarPessoaCompleta(p.getId()));
        }

        // Calcula o tamanho de equipe
        int tamanhoEquipe = calcularTamanhoEquipe(formacao, pessoasCompletas.size());

        // Aplica a estratégia de recomendação
        EstrategiaRecomendacao estrategia = RecomendadorEquipes.obterEstrategia(formacao.getEstrategia());
        List<List<Pessoa>> grupos = estrategia.recomendar(pessoasCompletas, tamanhoEquipe);

        // Persiste as equipes geradas
        List<Equipe> equipesGeradas = new ArrayList<>();
        for (int i = 0; i < grupos.size(); i++) {
            Equipe equipe = new Equipe();
            equipe.setFormacaoId(formacaoId);
            equipe.setNome("Equipe " + (i + 1));
            equipeRepository.salvar(equipe);

            List<MembroEquipe> membros = new ArrayList<>();
            for (Pessoa pessoa : grupos.get(i)) {
                MembroEquipe membro = new MembroEquipe();
                membro.setEquipeId(equipe.getId());
                membro.setPessoaId(pessoa.getId());
                membro.setNomePessoa(pessoa.getNome());
                membroRepository.salvar(membro);
                membros.add(membro);
            }

            equipe.setMembros(membros);
            equipesGeradas.add(equipe);
        }

        return equipesGeradas;
    }

    /**
     * Remove as equipes existentes e gera novas.
     * Útil quando o usuário quer um novo sorteio ou mudar a estratégia.
     */
    public List<Equipe> regenerarEquipes(Long formacaoId) {
        // Deleta membros antes das equipes (para respeitar FK)
        membroRepository.deletarPorFormacao(formacaoId);
        equipeRepository.deletarPorFormacao(formacaoId);
        return gerarEquipes(formacaoId);
    }

    /** Retorna as equipes de uma formação com todos os membros carregados. */
    public List<Equipe> listarEquipes(Long formacaoId) {
        List<Equipe> equipes = equipeRepository.listarPorFormacao(formacaoId);
        for (Equipe equipe : equipes) {
            equipe.setMembros(membroRepository.listarPorEquipe(equipe.getId()));
        }
        return equipes;
    }

    /**
     * Determina o tamanho de equipe a usar.
     *
     * Se tamanhoEquipe foi informado: usa ele.
     * Se numeroEquipes foi informado: calcula Math.ceil(total / numero).
     */
    private int calcularTamanhoEquipe(FormacaoEquipe formacao, int totalPessoas) {
        if (formacao.getTamanhoEquipe() != null && formacao.getTamanhoEquipe() > 0) {
            return formacao.getTamanhoEquipe();
        }
        if (formacao.getNumeroEquipes() != null && formacao.getNumeroEquipes() > 0) {
            return (int) Math.ceil((double) totalPessoas / formacao.getNumeroEquipes());
        }
        throw new IllegalArgumentException("Informe tamanhoEquipe OU numeroEquipes válidos.");
    }
}
