package com.recomendacao.repository;

import com.recomendacao.database.DatabaseManager;
import com.recomendacao.model.CategoriaHabilidade;
import com.recomendacao.model.Habilidade;
import com.recomendacao.model.NivelHabilidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso ao banco de dados para a entidade Habilidade. */
public class HabilidadeRepository {

    private final Connection con;

    public HabilidadeRepository(DatabaseManager db) {
        this.con = db.getConexao();
    }

    public Habilidade salvar(Habilidade habilidade) {
        // A coluna "nome" no banco armazena o nome do enum CategoriaHabilidade
        String sql = "INSERT INTO habilidades (pessoa_id, nome, nivel) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, habilidade.getPessoaId());
            ps.setString(2, habilidade.getCategoria() != null ? habilidade.getCategoria().name() : null);
            ps.setString(3, habilidade.getNivel() != null ? habilidade.getNivel().name() : null);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) habilidade.setId(rs.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar habilidade: " + e.getMessage(), e);
        }
        return habilidade;
    }

    public List<Habilidade> listarPorPessoa(Long pessoaId) {
        String sql = "SELECT * FROM habilidades WHERE pessoa_id = ?";
        List<Habilidade> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, pessoaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Habilidade h = new Habilidade();
                h.setId(rs.getLong("id"));
                h.setPessoaId(rs.getLong("pessoa_id"));
                // String do banco → Enum (null-safe)
                String cat = rs.getString("nome");
                h.setCategoria(cat != null ? CategoriaHabilidade.valueOf(cat) : null);
                String niv = rs.getString("nivel");
                h.setNivel(niv != null ? NivelHabilidade.valueOf(niv) : null);
                lista.add(h);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar habilidades: " + e.getMessage(), e);
        }
        return lista;
    }

    public void deletarPorPessoa(Long pessoaId) {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM habilidades WHERE pessoa_id = ?")) {
            ps.setLong(1, pessoaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar habilidades: " + e.getMessage(), e);
        }
    }
}
