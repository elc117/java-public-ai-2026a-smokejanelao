package com.recomendacao.repository;

import com.recomendacao.database.DatabaseManager;
import com.recomendacao.model.MembroEquipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso ao banco de dados para a entidade MembroEquipe. */
public class MembroEquipeRepository {

    private final Connection con;

    public MembroEquipeRepository(DatabaseManager db) {
        this.con = db.getConexao();
    }

    public MembroEquipe salvar(MembroEquipe membro) {
        String sql = "INSERT INTO membros_equipe (equipe_id, pessoa_id, funcao) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, membro.getEquipeId());
            ps.setLong(2, membro.getPessoaId());
            ps.setString(3, membro.getFuncao());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) membro.setId(rs.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar membro: " + e.getMessage(), e);
        }
        return membro;
    }

    /**
     * Lista membros de uma equipe com o nome da pessoa via JOIN.
     * Evita N+1 queries ao carregar as equipes.
     */
    public List<MembroEquipe> listarPorEquipe(Long equipeId) {
        String sql = """
            SELECT m.*, p.nome AS nome_pessoa
            FROM membros_equipe m
            JOIN pessoas p ON p.id = m.pessoa_id
            WHERE m.equipe_id = ?
            ORDER BY p.nome
        """;
        List<MembroEquipe> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, equipeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MembroEquipe m = new MembroEquipe();
                m.setId(rs.getLong("id"));
                m.setEquipeId(rs.getLong("equipe_id"));
                m.setPessoaId(rs.getLong("pessoa_id"));
                m.setFuncao(rs.getString("funcao"));
                m.setNomePessoa(rs.getString("nome_pessoa"));
                lista.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar membros: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Remove membros de equipes pertencentes a uma formação (ao regenerar). */
    public void deletarPorFormacao(Long formacaoId) {
        String sql = """
            DELETE FROM membros_equipe
            WHERE equipe_id IN (SELECT id FROM equipes WHERE formacao_id = ?)
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, formacaoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar membros: " + e.getMessage(), e);
        }
    }
}
