package com.recomendacao.estrategia;

import com.recomendacao.model.NivelExperiencia;
import com.recomendacao.model.Pessoa;

import java.util.ArrayList;
import java.util.List;

/**
 * Estratégia NIVEL: distribui pessoas de diferentes níveis de experiência
 * de forma balanceada entre as equipes usando round-robin.
 *
 * Exemplo com 9 pessoas (3 Avançado, 3 Intermediário, 3 Iniciante)
 * e 3 equipes de 3: cada equipe recebe 1 de cada nível.
 *
 * Ordena: AVANCADO → INTERMEDIARIO → INICIANTE, depois distribui
 * ciclicamente: 1ª pessoa → equipe 1, 2ª → equipe 2, etc.
 */
public class EstrategiaBalancearNivel implements EstrategiaRecomendacao {

    @Override
    public List<List<Pessoa>> recomendar(List<Pessoa> pessoas, int tamanhoEquipe) {
        int numEquipes = (int) Math.ceil((double) pessoas.size() / tamanhoEquipe);

        // Inicializa as equipes vazias
        List<List<Pessoa>> equipes = new ArrayList<>();
        for (int i = 0; i < numEquipes; i++) {
            equipes.add(new ArrayList<>());
        }

        // Ordena do mais experiente ao menos experiente usando o ordinal do enum
        // (INICIANTE=0, INTERMEDIARIO=1, AVANCADO=2) → negado para ordem decrescente
        List<Pessoa> ordenadas = new ArrayList<>(pessoas);
        ordenadas.sort((a, b) -> {
            NivelExperiencia nA = a.getNivelExperiencia();
            NivelExperiencia nB = b.getNivelExperiencia();
            int valorA = nA != null ? nA.ordinal() : -1;
            int valorB = nB != null ? nB.ordinal() : -1;
            return Integer.compare(valorB, valorA); // decrescente
        });

        // Round-robin: distribui ciclicamente entre as equipes
        for (int i = 0; i < ordenadas.size(); i++) {
            equipes.get(i % numEquipes).add(ordenadas.get(i));
        }

        // Remove equipes vazias (caso o número de pessoas seja menor que numEquipes)
        equipes.removeIf(List::isEmpty);

        return equipes;
    }
}
