package com.recomendacao.estrategia;

import com.recomendacao.model.Pessoa;

import java.util.*;

/**
 * Estratégia HABILIDADES: tenta distribuir pessoas com habilidades diversas
 * entre as equipes, para que cada equipe tenha o maior leque de habilidades possível.
 *
 * Algoritmo: ordena pessoas pela quantidade de habilidades (mais → menos),
 * depois distribui em round-robin para espalhar a diversidade.
 */
public class EstrategiaBalancearHabilidades implements EstrategiaRecomendacao {

    @Override
    public List<List<Pessoa>> recomendar(List<Pessoa> pessoas, int tamanhoEquipe) {
        int numEquipes = (int) Math.ceil((double) pessoas.size() / tamanhoEquipe);

        List<List<Pessoa>> equipes = new ArrayList<>();
        for (int i = 0; i < numEquipes; i++) {
            equipes.add(new ArrayList<>());
        }

        // Ordena pelo número de habilidades distintas (mais habilidades primeiro)
        List<Pessoa> ordenadas = new ArrayList<>(pessoas);
        ordenadas.sort((a, b) -> {
            int qtdA = a.getHabilidades() != null ? a.getHabilidades().size() : 0;
            int qtdB = b.getHabilidades() != null ? b.getHabilidades().size() : 0;
            return Integer.compare(qtdB, qtdA);
        });

        // Round-robin para espalhar as habilidades
        for (int i = 0; i < ordenadas.size(); i++) {
            equipes.get(i % numEquipes).add(ordenadas.get(i));
        }

        equipes.removeIf(List::isEmpty);
        return equipes;
    }
}
