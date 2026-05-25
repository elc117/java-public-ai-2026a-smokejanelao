package com.recomendacao.controller;

import com.recomendacao.model.FormacaoEquipe;
import com.recomendacao.service.FormacaoService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Controller de formações de equipes.
 *
 * Importante: este controller NUNCA decide como as equipes são formadas.
 * Ele apenas recebe a requisição e delega ao FormacaoService.
 */
public class FormacaoController {

    private final FormacaoService formacaoService;

    public FormacaoController(FormacaoService formacaoService) {
        this.formacaoService = formacaoService;
    }

    public void registrarRotas(Javalin app) {
        app.post("/formacoes",               this::criarFormacao);
        app.get("/formacoes/{id}",           this::buscarFormacao);
        app.post("/formacoes/{id}/gerar",    this::gerarEquipes);
        app.post("/formacoes/{id}/regenerar",this::regenerarEquipes);
        app.get("/formacoes/{id}/equipes",   this::listarEquipes);
    }

    // POST /formacoes
    private void criarFormacao(Context ctx) {
        FormacaoEquipe formacao = ctx.bodyAsClass(FormacaoEquipe.class);
        ctx.status(201).json(formacaoService.criarFormacao(formacao));
    }

    // GET /formacoes/{id}
    private void buscarFormacao(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(formacaoService.buscarFormacao(id));
    }

    // POST /formacoes/{id}/gerar
    private void gerarEquipes(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(formacaoService.gerarEquipes(id));
    }

    // POST /formacoes/{id}/regenerar
    private void regenerarEquipes(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(formacaoService.regenerarEquipes(id));
    }

    // GET /formacoes/{id}/equipes
    private void listarEquipes(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(formacaoService.listarEquipes(id));
    }
}
