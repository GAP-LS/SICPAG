package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Secao;
import com.suchorski.sicpag.models.TipoSecao;

public class SecaoService extends AbstratoService {

	public static void incluir(String sigla, int pai, int tipo) throws DBInfo, DBException {
		String sql = "INSERT INTO SECAO (SIGLA, ID_PAI, ID_TIPO) VALUES (?, ?, ?)";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setString(1, sigla);
			if (pai > 0)
				ps.setInt(2, pai);
			else
				ps.setNull(2, Types.INTEGER);
			ps.setInt(3, tipo);
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Seçao não adicionada");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static Secao procurar(int id) throws DBInfo, DBException {
		String sql = "SELECT * FROM SECAO S JOIN TIPOSECAO TS ON S.ID_TIPO = TS.ID LEFT JOIN SECAO P ON S.ID_PAI = P.ID LEFT JOIN TIPOSECAO TP ON P.ID_TIPO = TP.ID WHERE S.ID = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Secao(
							rs.getInt("S.ID"),
							rs.getString("S.SIGLA"),
							new Secao(
									rs.getInt("P.ID"),
									rs.getString("P.SIGLA"),
									null,
									new TipoSecao(rs.getInt("TP.ID"), rs.getString("TP.TIPO"))
									),
							new TipoSecao(rs.getInt("TS.ID"), rs.getString("TS.TIPO"))
							);
				} else {
					throw new DBInfo("Seção não encontrada");
				}
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static Secao secaoUsuario(int id) throws DBInfo, DBException {
		try {
			return procurar(id);
		} catch (DBInfo e) {
			throw new DBInfo("Você precisa estar associado a alguma seção para acessar o sistema");
		}
	}

	public static List<Secao> procurarFilhas(int id) throws DBException {
		String sql = "SELECT * FROM SECAO S JOIN TIPOSECAO TS ON S.ID_TIPO = TS.ID LEFT JOIN SECAO P ON S.ID_PAI = P.ID LEFT JOIN TIPOSECAO TP ON P.ID_TIPO = TP.ID WHERE S.ID_PAI = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				List<Secao> secoes = new ArrayList<Secao>();
				while (rs.next()) {
					secoes.add(new Secao(
							rs.getInt("S.ID"),
							rs.getString("S.SIGLA"),
							new Secao(
									rs.getInt("P.ID"),
									rs.getString("P.SIGLA"),
									null,
									new TipoSecao(rs.getInt("TP.ID"), rs.getString("TP.TIPO"))
									),
							new TipoSecao(rs.getInt("TS.ID"), rs.getString("TS.TIPO"))
							));
				}
				return secoes;
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static List<Secao> listar() throws DBException {
		String sql = "SELECT * FROM SECAO S JOIN TIPOSECAO TS ON S.ID_TIPO = TS.ID";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			List<Secao> secoes = new ArrayList<Secao>();
			while (rs.next()) {
				secoes.add(new Secao(rs.getInt("S.ID"), rs.getString("S.SIGLA"), rs.getInt("S.ID_PAI"), rs.getInt("TS.ID")));
			}
			return secoes;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static void atualizar(Secao s) throws DBInfo, DBException {
		String sql = "UPDATE SECAO SET SIGLA = ?, ID_PAI = ?, ID_TIPO = ? WHERE ID = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setString(1, s.getSigla());
			if (s.getPai().getId() > 0)
				ps.setInt(2, s.getPai().getId());
			else
				ps.setNull(2, Types.INTEGER);
			ps.setInt(3, s.getTipo().getId());
			ps.setInt(4, s.getId());
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Nenhuma seção foi atualizada");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static void excluir(int id) throws DBInfo, DBException {
		String sql = "DELETE FROM SECAO WHERE ID = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, id);
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Nenhuma seção foi excluída");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

}
