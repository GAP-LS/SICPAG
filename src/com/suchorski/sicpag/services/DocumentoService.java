package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Documento;

public class DocumentoService extends AbstratoService {

	public static int incluir(Connection c, String descricao, int idModalidade, int idUsuario, int idSecao) throws DBInfo, SQLException {
		String sql = "INSERT INTO DOCUMENTO (DESCRICAO, ID_MODALIDADE, ID_USUARIO) VALUES (?, ?, ?)";
		try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setString(1, descricao);
			ps.setInt(2, idModalidade);
			ps.setInt(3, idUsuario);
			ps.executeUpdate();
		}
		int lastId = lastId(c);
		RastrearService.incluir(c, lastId, idSecao, idUsuario);
		return lastId;
	}
	
	public static void atualizar(Connection c, Documento d) throws DBInfo, DBException {
		String sql = "UPDATE DOCUMENTO SET DESCRICAO = ?, ID_MODALIDADE = ? WHERE ID = ?";
		try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setString(1, d.getDescricao());
			ps.setInt(2, d.getModalidade().getId());
			ps.setInt(3, d.getId());
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Nenhum documento foi atualizado");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static void excluir(int id) throws DBInfo, DBException {
		String sql = "DELETE FROM DOCUMENTO WHERE ID = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, id);
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Nenhum documento foi excluído");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

}
