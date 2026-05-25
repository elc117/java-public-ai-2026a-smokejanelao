package com.recomendacao.estrategia;

import com.recomendacao.model.Pessoa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Estratégia ALEATORIA: embaralha as pessoas e divide sequencialmente.
 *
 * Se o total não for divisível pelo tamanho, as pessoas restantes
 * são distribuídas nas primeiras equipes (uma extra por equipe).
 */
public class EstrategiaAleatoria implements EstrategiaRecomendacao {

    @Override
    public List<List<Pessoa>> recomendar(List<Pessoa> pessoas, int tamanhoEquipe) {
        // Cria uma cópia para não alterar a lista original
        List<Pessoa> embaralhadas = new ArrayList<>(pessoas);
        Collections.shuffle(embaralhadas);

        return dividirEmGrupos(embaralhadas, tamanhoEquipe);
    }

    /**
     * Divide uma lista em sublistas de tamanho máximo informado.
     * O último grupo pode ter menos pessoas que o tamanho definido.
     */
    static List<List<Pessoa>> dividirEmGrupos(List<Pessoa> pessoas, int tamanhoEquipe) {
        List<List<Pessoa>> equipes = new ArrayList<>();
        int total = pessoas.size();

        for (int i = 0; i < total; i += tamanhoEquipe) {
            int fim = Math.min(i + tamanhoEquipe, total);
            equipes.add(new ArrayList<>(pessoas.subList(i, fim)));
        }

        return equipes;
    }
}
