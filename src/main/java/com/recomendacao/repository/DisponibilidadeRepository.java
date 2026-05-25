package com.recomendacao.repository;

import com.recomendacao.database.DatabaseManager;
import com.recomendacao.model.DiaSemana;
import com.recomendacao.model.Disponibilidade;
import com.recomendacao.model.Turno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acesso ao banco de dados para a entidade Disponibilidade. */
public class DisponibilidadeRepository {

    private final Connection con;

    public DisponibilidadeRepository(DatabaseManager db) {
        this.con = db.getConexao();
    }

    public Disponibilidade salvar(Disponibilidade disp) {
        String sql = "INSERT INTO disponibilidades (pessoa_id, dia_semana, turno) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, disp.getPessoaId());
            ps.setString(2, disp.getDiaSemana() != null ? disp.getDiaSemana().name() : null);
            ps.setString(3, disp.getTurno() != null ? disp.getTurno().name() : null);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) disp.setId(rs.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar disponibilidade: " + e.getMessage(), e);
        }
        return disp;
    }

    public List<Disponibilidade> listarPorPessoa(Long pessoaId) {
        String sql = "SELECT * FROM disponibilidades WHERE pessoa_id = ?";
        List<Disponibilidade> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, pessoaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Disponibilidade d = new Disponibilidade();
                d.setId(rs.getLong("id"));
                d.setPessoaId(rs.getLong("pessoa_id"));
                // String do banco → Enum (null-safe)
                String dia = rs.getString("dia_semana");
                d.setDiaSemana(dia != null ? DiaSemana.valueOf(dia) : null);
                String turno = rs.getString("turno");
                d.setTurno(turno != null ? Turno.valueOf(turno) : null);
                lista.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar disponibilidades: " + e.getMessage(), e);
        }
        return lista;
    }

    public void deletarPorPessoa(Long pessoaId) {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM disponibilidades WHERE pessoa_id = ?")) {
            ps.setLong(1, pessoaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar disponibilidades: " + e.getMessage(), e);
        }
    }
}
