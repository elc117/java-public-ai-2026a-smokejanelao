package com.recomendacao.repository;

import com.recomendacao.database.DatabaseManager;
import com.recomendacao.model.Interesse;
import com.recomendacao.model.TipoInteresse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso ao banco de dados para a entidade Interesse. */
public class InteresseRepository {

    private final Connection con;

    public InteresseRepository(DatabaseManager db) {
        this.con = db.getConexao();
    }

    public Interesse salvar(Interesse interesse) {
        // A coluna "nome" no banco armazena o nome do enum TipoInteresse
        String sql = "INSERT INTO interesses (pessoa_id, nome) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, interesse.getPessoaId());
            ps.setString(2, interesse.getTipo() != null ? interesse.getTipo().name() : null);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) interesse.setId(rs.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar interesse: " + e.getMessage(), e);
        }
        return interesse;
    }

    public List<Interesse> listarPorPessoa(Long pessoaId) {
        String sql = "SELECT * FROM interesses WHERE pessoa_id = ?";
        List<Interesse> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, pessoaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Interesse i = new Interesse();
                i.setId(rs.getLong("id"));
                i.setPessoaId(rs.getLong("pessoa_id"));
                // String do banco → Enum (null-safe)
                String tipo = rs.getString("nome");
                i.setTipo(tipo != null ? TipoInteresse.valueOf(tipo) : null);
                lista.add(i);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar interesses: " + e.getMessage(), e);
        }
        return lista;
    }

    public void deletarPorPessoa(Long pessoaId) {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM interesses WHERE pessoa_id = ?")) {
            ps.setLong(1, pessoaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar interesses: " + e.getMessage(), e);
        }
    }
}
