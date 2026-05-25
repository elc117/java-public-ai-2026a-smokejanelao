package com.recomendacao.estrategia;

import com.recomendacao.model.Pessoa;

import java.util.List;

/**
 * Interface que define o contrato para todas as estratégias de recomendação.
 *
 * Padrão Strategy: cada implementação encapsula um algoritmo de divisão de equipes.
 * Para adicionar uma nova estratégia, basta criar uma nova classe que implemente
 * esta interface — sem modificar código existente.
 *
 * @param pessoas       lista de pessoas a serem divididas
 * @param tamanhoEquipe tamanho desejado de cada equipe
 * @return lista de grupos, onde cada grupo é uma lista de pessoas (uma equipe)
 */
public interface EstrategiaRecomendacao {
    List<List<Pessoa>> recomendar(List<Pessoa> pessoas, int tamanhoEquipe);
}
