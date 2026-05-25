package com.recomendacao.estrategia;

import com.recomendacao.model.Interesse;
import com.recomendacao.model.Pessoa;
import com.recomendacao.model.TipoInteresse;

import java.util.*;

/**
 * Estratégia INTERESSES: agrupa pessoas que compartilham interesses em comum.
 *
 * Algoritmo greedy:
 * 1. Para cada pessoa ainda não alocada, cria uma nova equipe com ela.
 * 2. Procura entre as não alocadas quem tem mais interesses em comum e adiciona
 *    até a equipe atingir tamanhoEquipe.
 * 3. Repete até todas serem alocadas.
 *
 * A comparação usa o enum TipoInteresse diretamente — não depende de casing.
 */
public class EstrategiaAgruparInteresses implements EstrategiaRecomendacao {

    @Override
    public List<List<Pessoa>> recomendar(List<Pessoa> pessoas, int tamanhoEquipe) {
        List<List<Pessoa>> equipes = new ArrayList<>();
        List<Pessoa> naoAlocadas = new ArrayList<>(pessoas);

        while (!naoAlocadas.isEmpty()) {
            List<Pessoa> equipe = new ArrayList<>();
            Pessoa ancora = naoAlocadas.remove(0);
            equipe.add(ancora);

            // Ordena as restantes pela semelhança de interesses com a âncora
            naoAlocadas.sort((a, b) -> {
                int simA = calcularSimilaridade(ancora, a);
                int simB = calcularSimilaridade(ancora, b);
                return Integer.compare(simB, simA); // maior similaridade primeiro
            });

            // Preenche a equipe com as pessoas mais similares
            while (equipe.size() < tamanhoEquipe && !naoAlocadas.isEmpty()) {
                equipe.add(naoAlocadas.remove(0));
            }

            equipes.add(equipe);
        }

        return equipes;
    }

    /** Conta quantos TipoInteresse a pessoa B tem em comum com a pessoa A (âncora). */
    private int calcularSimilaridade(Pessoa ancora, Pessoa candidato) {
        if (ancora.getInteresses() == null || candidato.getInteresses() == null) return 0;

        // Coleta os tipos de interesse da âncora em um Set para busca O(1)
        Set<TipoInteresse> interessesAncora = new HashSet<>();
        for (Interesse i : ancora.getInteresses()) {
            if (i.getTipo() != null) interessesAncora.add(i.getTipo());
        }

        int contagem = 0;
        for (Interesse i : candidato.getInteresses()) {
            if (i.getTipo() != null && interessesAncora.contains(i.getTipo())) {
                contagem++;
            }
        }
        return contagem;
    }
}
