package com.recomendacao.repository;

import com.recomendacao.database.DatabaseManager;
import com.recomendacao.model.FormacaoEquipe;

import java.sql.*;

/** Acesso ao banco de dados para a entidade FormacaoEquipe. */
public class FormacaoRepository {

    private final Connection con;

    public FormacaoRepository(DatabaseManager db) {
        this.con = db.getConexao();
    }

    /** Insere uma nova formação e retorna o objeto com id gerado. */
    public FormacaoEquipe salvar(FormacaoEquipe formacao) {
        String sql = """
            INSERT INTO formacoes (turma_id, nome, tamanho_equipe, numero_equipes, estrategia, data_criacao)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, formacao.getTurmaId());
            ps.setString(2, formacao.getNome());
            // setObject permite passar null quando o campo não foi informado
            ps.setObject(3, formacao.getTamanhoEquipe());
            ps.setObject(4, formacao.getNumeroEquipes());
            ps.setString(5, formacao.getEstrategia());
            ps.setString(6, formacao.getDataCriacao());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) formacao.setId(rs.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar formação: " + e.getMessage(), e);
        }
        return formacao;
    }

    /** Busca uma formação pelo id. */
    public FormacaoEquipe buscarPorId(Long id) {
        String sql = "SELECT * FROM formacoes WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                FormacaoEquipe f = new FormacaoEquipe();
                f.setId(rs.getLong("id"));
                f.setTurmaId(rs.getLong("turma_id"));
                f.setNome(rs.getString("nome"));
                // getObject retorna null se o campo for NULL no banco
                f.setTamanhoEquipe((Integer) rs.getObject("tamanho_equipe"));
                f.setNumeroEquipes((Integer) rs.getObject("numero_equipes"));
                f.setEstrategia(rs.getString("estrategia"));
                f.setDataCriacao(rs.getString("data_criacao"));
                return f;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar formação: " + e.getMessage(), e);
        }
        return null;
    }
}
