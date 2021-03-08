package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.models.Modalidade;

public class ModalidadeService extends AbstratoService {
	
	public static List<Modalidade> listar() throws Exception, DBException {
		String sql = "SELECT * FROM MODALIDADE M ORDER BY TIPO ASC";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			List<Modalidade> tipos = new ArrayList<Modalidade>();
			while (rs.next()) {
				tipos.add(new Modalidade(rs.getInt("M.ID"), rs.getString("M.TIPO")));
			}
			if (tipos.isEmpty()) {
				throw new Exception("Nenhuma modalidade encontrada");
			}
			return tipos;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

}
