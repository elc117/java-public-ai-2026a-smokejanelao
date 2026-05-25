package com.recomendacao;

import com.recomendacao.controller.FormacaoController;
import com.recomendacao.controller.PessoaController;
import com.recomendacao.controller.TurmaController;
import com.recomendacao.database.DatabaseManager;
import com.recomendacao.repository.*;
import com.recomendacao.service.FormacaoService;
import com.recomendacao.service.PessoaService;
import com.recomendacao.service.TurmaService;
import io.javalin.Javalin;

/**
 * Ponto de entrada da aplicação.
 *
 * Responsabilidade: montar todas as dependências (injeção manual) e
 * iniciar o servidor Javalin na porta 7070.
 *
 * Ordem de montagem:
 *   DatabaseManager → Repositories → Services → Controllers → Rotas → Start
 */
public class Main {

    public static void main(String[] args) {

        // 1. Banco de dados: cria conexão e tabelas
        DatabaseManager db = DatabaseManager.getInstancia();
        db.inicializar();

        // 2. Repositories: acesso ao SQLite
        TurmaRepository turmaRepo              = new TurmaRepository(db);
        PessoaRepository pessoaRepo            = new PessoaRepository(db);
        HabilidadeRepository habilidadeRepo    = new HabilidadeRepository(db);
        InteresseRepository interesseRepo      = new InteresseRepository(db);
        DisponibilidadeRepository dispRepo     = new DisponibilidadeRepository(db);
        FormacaoRepository formacaoRepo        = new FormacaoRepository(db);
        EquipeRepository equipeRepo            = new EquipeRepository(db);
        MembroEquipeRepository membroRepo      = new MembroEquipeRepository(db);

        // 3. Services: regras de negócio
        TurmaService turmaService = new TurmaService(turmaRepo);

        PessoaService pessoaService = new PessoaService(
            pessoaRepo, habilidadeRepo, interesseRepo, dispRepo
        );

        FormacaoService formacaoService = new FormacaoService(
            formacaoRepo, equipeRepo, membroRepo, pessoaService
        );

        // 4. Controllers: handlers HTTP
        TurmaController turmaController       = new TurmaController(turmaService, pessoaService);
        PessoaController pessoaController     = new PessoaController(pessoaService);
        FormacaoController formacaoController = new FormacaoController(formacaoService);

        // 5. Javalin: configura e sobe o servidor
        Javalin app = Javalin.create(config -> {
            // CORS aberto: necessário para o frontend servido como arquivo local (file://)
            config.bundledPlugins.enableCors(cors ->
                cors.addRule(rule -> {
                    rule.anyHost();
                })
            );
        });

        // 6. Tratamento global de erros: retorna JSON com a mensagem
        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400).json(new ErroResposta(e.getMessage()));
        });
        app.exception(IllegalStateException.class, (e, ctx) -> {
            ctx.status(422).json(new ErroResposta(e.getMessage()));
        });
        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(500).json(new ErroResposta("Erro interno: " + e.getMessage()));
        });

        // 7. Registro das rotas
        turmaController.registrarRotas(app);
        pessoaController.registrarRotas(app);
        formacaoController.registrarRotas(app);

        // 8. Inicia na porta 8080
        app.start(8080);
        System.out.println("Servidor iniciado em http://localhost:8080");
    }

    /** Classe auxiliar para retornar erros em formato JSON. */
    public static class ErroResposta {
        public String erro;
        public ErroResposta(String erro) { this.erro = erro; }
    }
}
