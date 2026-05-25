package com.recomendacao.controller;

import com.recomendacao.model.Disponibilidade;
import com.recomendacao.model.Habilidade;
import com.recomendacao.model.Interesse;
import com.recomendacao.model.Pessoa;
import com.recomendacao.service.PessoaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Controller de pessoas: gerencia atualização, exclusão e atributos.
 */
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    public void registrarRotas(Javalin app) {
        app.put("/pessoas/{id}",                    this::atualizarPessoa);
        app.delete("/pessoas/{id}",                 this::deletarPessoa);
        app.get("/pessoas/{id}",                    this::buscarPessoa);
        app.post("/pessoas/{id}/habilidades",       this::adicionarHabilidade);
        app.post("/pessoas/{id}/interesses",        this::adicionarInteresse);
        app.post("/pessoas/{id}/disponibilidades",  this::adicionarDisponibilidade);
    }

    // PUT /pessoas/{id}
    private void atualizarPessoa(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Pessoa dados = ctx.bodyAsClass(Pessoa.class);
        ctx.json(pessoaService.atualizarPessoa(id, dados));
    }

    // DELETE /pessoas/{id}
    private void deletarPessoa(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        pessoaService.deletarPessoa(id);
        ctx.status(204);
    }

    // GET /pessoas/{id}
    private void buscarPessoa(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Pessoa pessoa = pessoaService.buscarPessoaCompleta(id);
        if (pessoa == null) {
            ctx.status(404).result("Pessoa não encontrada.");
        } else {
            ctx.json(pessoa);
        }
    }

    // POST /pessoas/{id}/habilidades
    private void adicionarHabilidade(Context ctx) {
        Long pessoaId = Long.parseLong(ctx.pathParam("id"));
        Habilidade h = ctx.bodyAsClass(Habilidade.class);
        ctx.status(201).json(pessoaService.adicionarHabilidade(pessoaId, h));
    }

    // POST /pessoas/{id}/interesses
    private void adicionarInteresse(Context ctx) {
        Long pessoaId = Long.parseLong(ctx.pathParam("id"));
        Interesse i = ctx.bodyAsClass(Interesse.class);
        ctx.status(201).json(pessoaService.adicionarInteresse(pessoaId, i));
    }

    // POST /pessoas/{id}/disponibilidades
    private void adicionarDisponibilidade(Context ctx) {
        Long pessoaId = Long.parseLong(ctx.pathParam("id"));
        Disponibilidade d = ctx.bodyAsClass(Disponibilidade.class);
        ctx.status(201).json(pessoaService.adicionarDisponibilidade(pessoaId, d));
    }
}
