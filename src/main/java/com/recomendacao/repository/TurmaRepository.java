package com.recomendacao.repository;

import com.recomendacao.database.DatabaseManager;
import com.recomendacao.model.Turma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso ao banco de dados para a entidade Turma. */
public class TurmaRepository {

    private final Connection con;

    public TurmaRepository(DatabaseManager db) {
        this.con = db.getConexao();
    }

    /** Insere uma nova turma e retorna o objeto com o id gerado. */
    public Turma salvar(Turma turma) {
        String sql = "INSERT INTO turmas (nome, descricao) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, turma.getNome());
            ps.setString(2, turma.getDescricao());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                turma.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar turma: " + e.getMessage(), e);
        }
        return turma;
    }

    /** Busca uma turma pelo id. Retorna null se não encontrar. */
    public Turma buscarPorId(Long id) {
        String sql = "SELECT * FROM turmas WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar turma: " + e.getMessage(), e);
        }
        return null;
    }

    /** Lista todas as turmas cadastradas. */
    public List<Turma> listarTodos() {
        String sql = "SELECT * FROM turmas ORDER BY id";
        List<Turma> lista = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar turmas: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Converte uma linha do ResultSet em objeto Turma. */
    private Turma mapear(ResultSet rs) throws SQLException {
        Turma t = new Turma();
        t.setId(rs.getLong("id"));
        t.setNome(rs.getString("nome"));
        t.setDescricao(rs.getString("descricao"));
        return t;
    }
}
