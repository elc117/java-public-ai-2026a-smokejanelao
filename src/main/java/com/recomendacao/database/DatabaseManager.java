package com.recomendacao.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gerencia a conexão com o banco de dados SQLite.
 *
 * Padrão Singleton: apenas uma instância existe durante toda a execução,
 * o que garante que todas as partes do sistema compartilhem a mesma conexão.
 */
public class DatabaseManager {

    // Caminho do arquivo do banco de dados (criado na raiz do projeto)
    private static final String URL = "jdbc:sqlite:equipes.db";

    // Instância única (padrão Singleton)
    private static DatabaseManager instancia;

    // Conexão reutilizada em toda a aplicação
    private Connection conexao;

    // Construtor privado: ninguém pode instanciar de fora
    private DatabaseManager() {
        try {
            conexao = DriverManager.getConnection(URL);
            // Habilita suporte a chaves estrangeiras no SQLite (desativado por padrão)
            conexao.createStatement().execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    /** Retorna a instância única do DatabaseManager. */
    public static DatabaseManager getInstancia() {
        if (instancia == null) {
            instancia = new DatabaseManager();
        }
        return instancia;
    }

    /** Retorna a conexão com o banco de dados. */
    public Connection getConexao() {
        return conexao;
    }

    /**
     * Cria todas as tabelas do banco caso ainda não existam.
     * Chamado uma vez no início da aplicação (Main.java).
     */
    public void inicializar() {
        try (Statement stmt = conexao.createStatement()) {

            // Tabela de turmas/grupos de pessoas
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS turmas (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome       TEXT NOT NULL,
                    descricao  TEXT
                )
            """);

            // Tabela de pessoas, vinculadas a uma turma
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pessoas (
                    id                 INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome               TEXT NOT NULL,
                    email              TEXT,
                    nivel_experiencia  TEXT,
                    turma_id           INTEGER,
                    FOREIGN KEY (turma_id) REFERENCES turmas(id)
                )
            """);

            // Habilidades de cada pessoa (ex: "Java", "Python")
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS habilidades (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    pessoa_id  INTEGER NOT NULL,
                    nome       TEXT NOT NULL,
                    nivel      TEXT,
                    FOREIGN KEY (pessoa_id) REFERENCES pessoas(id)
                )
            """);

            // Interesses de cada pessoa (ex: "Machine Learning", "Games")
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS interesses (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    pessoa_id  INTEGER NOT NULL,
                    nome       TEXT NOT NULL,
                    FOREIGN KEY (pessoa_id) REFERENCES pessoas(id)
                )
            """);

            // Disponibilidade horária de cada pessoa
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS disponibilidades (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    pessoa_id   INTEGER NOT NULL,
                    dia_semana  TEXT NOT NULL,
                    turno       TEXT NOT NULL,
                    FOREIGN KEY (pessoa_id) REFERENCES pessoas(id)
                )
            """);

            // Registro de uma formação de equipes (configuração + estratégia escolhida)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS formacoes (
                    id             INTEGER PRIMARY KEY AUTOINCREMENT,
                    turma_id       INTEGER NOT NULL,
                    nome           TEXT NOT NULL,
                    tamanho_equipe INTEGER,
                    numero_equipes INTEGER,
                    estrategia     TEXT NOT NULL,
                    data_criacao   TEXT,
                    FOREIGN KEY (turma_id) REFERENCES turmas(id)
                )
            """);

            // Equipes geradas dentro de uma formação
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS equipes (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    formacao_id  INTEGER NOT NULL,
                    nome         TEXT NOT NULL,
                    FOREIGN KEY (formacao_id) REFERENCES formacoes(id)
                )
            """);

            // Membros de cada equipe (pessoa + função opcional)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS membros_equipe (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    equipe_id  INTEGER NOT NULL,
                    pessoa_id  INTEGER NOT NULL,
                    funcao     TEXT,
                    FOREIGN KEY (equipe_id) REFERENCES equipes(id),
                    FOREIGN KEY (pessoa_id) REFERENCES pessoas(id)
                )
            """);

            System.out.println("Banco de dados inicializado com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabelas: " + e.getMessage(), e);
        }
    }
}
