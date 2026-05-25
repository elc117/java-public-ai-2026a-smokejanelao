package com.recomendacao.estrategia;

import com.recomendacao.model.Disponibilidade;
import com.recomendacao.model.Pessoa;

import java.util.*;

/**
 * Estratégia DISPONIBILIDADE: tenta agrupar pessoas com horários compatíveis.
 *
 * Compatibilidade = compartilham pelo menos um (DiaSemana + Turno).
 *
 * Algoritmo greedy:
 * 1. Pega a primeira pessoa não alocada como âncora de uma nova equipe.
 * 2. Ordena as restantes pela quantidade de horários em comum com a âncora.
 * 3. Preenche a equipe com as mais compatíveis.
 * 4. Repete até todas serem alocadas.
 *
 * Os enums DiaSemana e Turno garantem que os slots são comparados de forma
 * consistente (sem ambiguidade de casing ou grafias diferentes).
 */
public class EstrategiaAgruparDisponibilidade implements EstrategiaRecomendacao {

    @Override
    public List<List<Pessoa>> recomendar(List<Pessoa> pessoas, int tamanhoEquipe) {
        List<List<Pessoa>> equipes = new ArrayList<>();
        List<Pessoa> naoAlocadas = new ArrayList<>(pessoas);

        while (!naoAlocadas.isEmpty()) {
            List<Pessoa> equipe = new ArrayList<>();
            Pessoa ancora = naoAlocadas.remove(0);
            equipe.add(ancora);

            // Ordena pela compatibilidade com a âncora
            naoAlocadas.sort((a, b) -> {
                int compA = calcularCompatibilidade(ancora, a);
                int compB = calcularCompatibilidade(ancora, b);
                return Integer.compare(compB, compA);
            });

            while (equipe.size() < tamanhoEquipe && !naoAlocadas.isEmpty()) {
                equipe.add(naoAlocadas.remove(0));
            }

            equipes.add(equipe);
        }

        return equipes;
    }

    /** Conta quantos slots (DiaSemana + Turno) são comuns entre duas pessoas. */
    private int calcularCompatibilidade(Pessoa ancora, Pessoa candidato) {
        if (ancora.getDisponibilidades() == null || candidato.getDisponibilidades() == null) return 0;

        // Chave: "SEGUNDA_MANHA", "TERCA_TARDE", etc. — usa .name() dos enums
        Set<String> slotsAncora = new HashSet<>();
        for (Disponibilidade d : ancora.getDisponibilidades()) {
            if (d.getDiaSemana() != null && d.getTurno() != null) {
                slotsAncora.add(d.getDiaSemana().name() + "_" + d.getTurno().name());
            }
        }

        int contagem = 0;
        for (Disponibilidade d : candidato.getDisponibilidades()) {
            if (d.getDiaSemana() != null && d.getTurno() != null) {
                if (slotsAncora.contains(d.getDiaSemana().name() + "_" + d.getTurno().name())) {
                    contagem++;
                }
            }
        }
        return contagem;
    }
}
