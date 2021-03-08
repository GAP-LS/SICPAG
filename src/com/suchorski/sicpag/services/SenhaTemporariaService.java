package com.suchorski.sicpag.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.SenhaTemporaria;

public class SenhaTemporariaService extends AbstratoService {

	public static void criar(int idUsuario) throws DBException {
		String senha = new BigInteger(60, new SecureRandom()).toString(32);
		String sql = "INSERT INTO SENHATEMPORARIA (SENHA, ID_USUARIO) VALUES (?, ?)";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setString(1, senha);
			ps.setInt(2, idUsuario);
			ps.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			criar(idUsuario);
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static List<SenhaTemporaria> listar(int idUsuario) throws DBInfo, DBException {
		String sql = "SELECT * FROM SENHATEMPORARIA WHERE ID_USUARIO = ? ORDER BY DATA_CRIACAO DESC";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, idUsuario);
			try (ResultSet rs = ps.executeQuery()) {
				List<SenhaTemporaria> senhas = new ArrayList<SenhaTemporaria>();
				while (rs.next()) {
					senhas.add(new SenhaTemporaria(rs.getInt("ID"), rs.getString("SENHA"), rs.getTimestamp("DATA_CRIACAO")));
				}
				if (senhas.isEmpty()) {
					throw new DBInfo("Nenhuma senha temporária foi gerada");
				}
				return senhas;
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static void excluir(int id, int idUsuario) throws DBException {
		String sql = "DELETE FROM SENHATEMPORARIA WHERE ID = ? AND ID_USUARIO = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.setInt(2, idUsuario);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static void usar(Connection c, String senha, int idDocumento) throws DBInfo, SQLException {
		String sql = "DELETE FROM SENHATEMPORARIA WHERE ID = ?";
		int id = getId(c, senha, idDocumento);
		try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, id);
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Erro ao usar a senha temporária");
			}
		}
	}
	
	public static int getId(Connection c, String senha, int idDocumento) throws DBInfo, SQLException {
		String sql = "SELECT ST.ID FROM SENHATEMPORARIA ST INNER JOIN (SELECT *, MAX(DATA_MOVIMENTACAO) FROM RASTREIO GROUP BY ID_DOCUMENTO DESC) R ON R.ID_USUARIO = ST.ID_USUARIO WHERE R.ID_TIPO = 2 AND ST.SENHA = ? AND R.ID_DOCUMENTO = ?";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, senha);
			ps.setInt(2, idDocumento);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("ST.ID");
				}
				throw new DBInfo("Senha inválida ou expirada");
			}
		}
	}
	
}
