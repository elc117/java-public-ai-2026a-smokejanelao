package com.recomendacao.service;

import com.recomendacao.model.*;
import com.recomendacao.repository.*;

import java.util.List;

/**
 * Regras de negócio para pessoas e seus atributos.
 *
 * Coordena os repositórios de pessoas, habilidades, interesses e disponibilidades.
 */
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final HabilidadeRepository habilidadeRepository;
    private final InteresseRepository interesseRepository;
    private final DisponibilidadeRepository disponibilidadeRepository;

    public PessoaService(PessoaRepository pessoaRepository,
                         HabilidadeRepository habilidadeRepository,
                         InteresseRepository interesseRepository,
                         DisponibilidadeRepository disponibilidadeRepository) {
        this.pessoaRepository = pessoaRepository;
        this.habilidadeRepository = habilidadeRepository;
        this.interesseRepository = interesseRepository;
        this.disponibilidadeRepository = disponibilidadeRepository;
    }

    public Pessoa criarPessoa(Pessoa pessoa) {
        if (pessoa.getNome() == null || pessoa.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da pessoa é obrigatório.");
        }
        if (pessoa.getTurmaId() == null) {
            throw new IllegalArgumentException("O id da turma é obrigatório.");
        }
        return pessoaRepository.salvar(pessoa);
    }

    public Pessoa atualizarPessoa(Long id, Pessoa dados) {
        Pessoa existente = pessoaRepository.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Pessoa não encontrada: id=" + id);
        }
        dados.setId(id);
        dados.setTurmaId(existente.getTurmaId()); // não altera a turma pelo PUT
        pessoaRepository.atualizar(dados);
        return dados;
    }

    public void deletarPessoa(Long id) {
        // Remove também os atributos vinculados à pessoa (cascata manual)
        habilidadeRepository.deletarPorPessoa(id);
        interesseRepository.deletarPorPessoa(id);
        disponibilidadeRepository.deletarPorPessoa(id);
        pessoaRepository.deletar(id);
    }

    /** Retorna todas as pessoas de uma turma (sem atributos detalhados). */
    public List<Pessoa> listarPessoasDaTurma(Long turmaId) {
        return pessoaRepository.listarPorTurma(turmaId);
    }

    /**
     * Retorna uma pessoa com todas as suas listas carregadas.
     * Usado pelas estratégias que precisam de habilidades/interesses/disponibilidades.
     */
    public Pessoa buscarPessoaCompleta(Long id) {
        Pessoa pessoa = pessoaRepository.buscarPorId(id);
        if (pessoa == null) return null;
        pessoa.setHabilidades(habilidadeRepository.listarPorPessoa(id));
        pessoa.setInteresses(interesseRepository.listarPorPessoa(id));
        pessoa.setDisponibilidades(disponibilidadeRepository.listarPorPessoa(id));
        return pessoa;
    }

    // ---- Atributos ----

    public Habilidade adicionarHabilidade(Long pessoaId, Habilidade habilidade) {
        habilidade.setPessoaId(pessoaId);
        return habilidadeRepository.salvar(habilidade);
    }

    public Interesse adicionarInteresse(Long pessoaId, Interesse interesse) {
        interesse.setPessoaId(pessoaId);
        return interesseRepository.salvar(interesse);
    }

    public Disponibilidade adicionarDisponibilidade(Long pessoaId, Disponibilidade disp) {
        disp.setPessoaId(pessoaId);
        return disponibilidadeRepository.salvar(disp);
    }
}
