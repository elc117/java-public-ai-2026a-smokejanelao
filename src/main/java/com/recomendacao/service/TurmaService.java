package com.recomendacao.service;

import com.recomendacao.model.Turma;
import com.recomendacao.repository.TurmaRepository;

import java.util.List;

/**
 * Regras de negócio para turmas.
 * Valida os dados antes de delegar ao repository.
 */
public class TurmaService {

    private final TurmaRepository turmaRepository;

    public TurmaService(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    public Turma criarTurma(Turma turma) {
        if (turma.getNome() == null || turma.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da turma é obrigatório.");
        }
        return turmaRepository.salvar(turma);
    }

    public Turma buscarTurma(Long id) {
        Turma turma = turmaRepository.buscarPorId(id);
        if (turma == null) {
            throw new IllegalArgumentException("Turma não encontrada: id=" + id);
        }
        return turma;
    }

    public List<Turma> listarTurmas() {
        return turmaRepository.listarTodos();
    }
}
