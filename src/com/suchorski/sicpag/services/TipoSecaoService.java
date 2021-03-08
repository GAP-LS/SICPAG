package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.models.TipoSecao;

public class TipoSecaoService extends AbstratoService {
	
	public static List<TipoSecao> listar() throws Exception, DBException {
		String sql = "SELECT * FROM TIPOSECAO TS ORDER BY ORDEM";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			List<TipoSecao> tipos = new ArrayList<TipoSecao>();
			while (rs.next()) {
				tipos.add(new TipoSecao(rs.getInt("TS.ID"), rs.getString("TS.TIPO")));
			}
			if (tipos.isEmpty()) {
				throw new Exception("Nenhum tipo de seção encontrado");
			}
			return tipos;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

}
