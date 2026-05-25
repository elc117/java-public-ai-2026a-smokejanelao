package com.recomendacao.repository;

import com.recomendacao.database.DatabaseManager;
import com.recomendacao.model.Equipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso ao banco de dados para a entidade Equipe. */
public class EquipeRepository {

    private final Connection con;

    public EquipeRepository(DatabaseManager db) {
        this.con = db.getConexao();
    }

    public Equipe salvar(Equipe equipe) {
        String sql = "INSERT INTO equipes (formacao_id, nome) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, equipe.getFormacaoId());
            ps.setString(2, equipe.getNome());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) equipe.setId(rs.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar equipe: " + e.getMessage(), e);
        }
        return equipe;
    }

    public List<Equipe> listarPorFormacao(Long formacaoId) {
        String sql = "SELECT * FROM equipes WHERE formacao_id = ? ORDER BY id";
        List<Equipe> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, formacaoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Equipe e = new Equipe();
                e.setId(rs.getLong("id"));
                e.setFormacaoId(rs.getLong("formacao_id"));
                e.setNome(rs.getString("nome"));
                lista.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar equipes: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Remove todas as equipes de uma formação (usado ao regenerar). */
    public void deletarPorFormacao(Long formacaoId) {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM equipes WHERE formacao_id = ?")) {
            ps.setLong(1, formacaoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar equipes: " + e.getMessage(), e);
        }
    }
}
