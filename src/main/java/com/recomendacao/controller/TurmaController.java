package com.recomendacao.controller;

import com.recomendacao.model.Pessoa;
import com.recomendacao.model.Turma;
import com.recomendacao.service.PessoaService;
import com.recomendacao.service.TurmaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Controller de turmas: recebe requisições HTTP, chama o service e devolve JSON.
 *
 * Responsabilidade única: traduzir HTTP ↔ service.
 * Nenhuma lógica de negócio aqui.
 */
public class TurmaController {

    private final TurmaService turmaService;
    private final PessoaService pessoaService;

    public TurmaController(TurmaService turmaService, PessoaService pessoaService) {
        this.turmaService = turmaService;
        this.pessoaService = pessoaService;
    }

    /** Registra todas as rotas de turmas no app Javalin. */
    public void registrarRotas(Javalin app) {
        app.post("/turmas",           this::criarTurma);
        app.get("/turmas",            this::listarTurmas);
        app.get("/turmas/{id}",       this::buscarTurma);
        app.post("/turmas/{id}/pessoas", this::adicionarPessoa);
        app.get("/turmas/{id}/pessoas",  this::listarPessoas);
    }

    // POST /turmas
    private void criarTurma(Context ctx) {
        Turma turma = ctx.bodyAsClass(Turma.class);
        Turma criada = turmaService.criarTurma(turma);
        ctx.status(201).json(criada);
    }

    // GET /turmas
    private void listarTurmas(Context ctx) {
        ctx.json(turmaService.listarTurmas());
    }

    // GET /turmas/{id}
    private void buscarTurma(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(turmaService.buscarTurma(id));
    }

    // POST /turmas/{id}/pessoas
    private void adicionarPessoa(Context ctx) {
        Long turmaId = Long.parseLong(ctx.pathParam("id"));
        Pessoa pessoa = ctx.bodyAsClass(Pessoa.class);
        pessoa.setTurmaId(turmaId);
        Pessoa criada = pessoaService.criarPessoa(pessoa);
        ctx.status(201).json(criada);
    }

    // GET /turmas/{id}/pessoas
    private void listarPessoas(Context ctx) {
        Long turmaId = Long.parseLong(ctx.pathParam("id"));
        ctx.json(pessoaService.listarPessoasDaTurma(turmaId));
    }
}
