package com.recomendacao.service;

import com.recomendacao.estrategia.*;

/**
 * Factory de estratégias de recomendação.
 *
 * Centraliza a criação das estratégias: o service de formação não precisa
 * conhecer as classes concretas, apenas passar o nome da estratégia.
 *
 * Para adicionar uma nova estratégia:
 * 1. Crie a classe em com.recomendacao.estrategia
 * 2. Adicione um case aqui no switch
 */
public class RecomendadorEquipes {

    public static EstrategiaRecomendacao obterEstrategia(String nome) {
        if (nome == null) {
            throw new IllegalArgumentException("Nome da estratégia não pode ser nulo.");
        }
        return switch (nome.toUpperCase().trim()) {
            case "ALEATORIA"      -> new EstrategiaAleatoria();
            case "NIVEL"          -> new EstrategiaBalancearNivel();
            case "HABILIDADES"    -> new EstrategiaBalancearHabilidades();
            case "INTERESSES"     -> new EstrategiaAgruparInteresses();
            case "DISPONIBILIDADE"-> new EstrategiaAgruparDisponibilidade();
            default -> throw new IllegalArgumentException(
                "Estratégia desconhecida: '" + nome + "'. " +
                "Use: ALEATORIA, NIVEL, HABILIDADES, INTERESSES ou DISPONIBILIDADE."
            );
        };
    }
}
