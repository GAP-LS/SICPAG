package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.TipoDerivativo;

public class TipoDerivativoService extends AbstratoService {
	
	public static List<TipoDerivativo> listar() throws DBInfo, DBException {
		String sql = "SELECT * FROM TIPODERIVATIVO ORDER BY TIPO ASC";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			List<TipoDerivativo> tipos = new ArrayList<TipoDerivativo>();
			while (rs.next()) {
				tipos.add(new TipoDerivativo(rs.getInt("ID"), rs.getString("TIPO")));
			}
			if (tipos.isEmpty()) {
				throw new DBInfo("Não existem tipos de derivativos cadastrados");
			}
			return tipos;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

}
