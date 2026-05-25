package com.recomendacao.repository;

import com.recomendacao.database.DatabaseManager;
import com.recomendacao.model.NivelExperiencia;
import com.recomendacao.model.Pessoa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso ao banco de dados para a entidade Pessoa. */
public class PessoaRepository {

    private final Connection con;

    public PessoaRepository(DatabaseManager db) {
        this.con = db.getConexao();
    }

    /** Insere uma nova pessoa e retorna o objeto com o id gerado. */
    public Pessoa salvar(Pessoa pessoa) {
        String sql = "INSERT INTO pessoas (nome, email, nivel_experiencia, turma_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getEmail());
            // Enum → String para o banco
            ps.setString(3, pessoa.getNivelExperiencia() != null ? pessoa.getNivelExperiencia().name() : null);
            ps.setLong(4, pessoa.getTurmaId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                pessoa.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar pessoa: " + e.getMessage(), e);
        }
        return pessoa;
    }

    /** Busca uma pessoa pelo id. */
    public Pessoa buscarPorId(Long id) {
        String sql = "SELECT * FROM pessoas WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pessoa: " + e.getMessage(), e);
        }
        return null;
    }

    /** Lista todas as pessoas de uma turma específica. */
    public List<Pessoa> listarPorTurma(Long turmaId) {
        String sql = "SELECT * FROM pessoas WHERE turma_id = ? ORDER BY nome";
        List<Pessoa> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, turmaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pessoas: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Atualiza os dados de uma pessoa existente. */
    public void atualizar(Pessoa pessoa) {
        String sql = "UPDATE pessoas SET nome=?, email=?, nivel_experiencia=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getEmail());
            ps.setString(3, pessoa.getNivelExperiencia() != null ? pessoa.getNivelExperiencia().name() : null);
            ps.setLong(4, pessoa.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pessoa: " + e.getMessage(), e);
        }
    }

    /** Remove uma pessoa do banco. */
    public void deletar(Long id) {
        String sql = "DELETE FROM pessoas WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pessoa: " + e.getMessage(), e);
        }
    }

    private Pessoa mapear(ResultSet rs) throws SQLException {
        Pessoa p = new Pessoa();
        p.setId(rs.getLong("id"));
        p.setNome(rs.getString("nome"));
        p.setEmail(rs.getString("email"));
        // String do banco → Enum (null-safe)
        String nivel = rs.getString("nivel_experiencia");
        p.setNivelExperiencia(nivel != null ? NivelExperiencia.valueOf(nivel) : null);
        p.setTurmaId(rs.getLong("turma_id"));
        return p;
    }
}
