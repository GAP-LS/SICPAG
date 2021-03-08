package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.models.Usuario;

public class RelatorioService extends AbstratoService {
	
	public static List<Usuario> listarAdministradores() throws DBException {
		String sql = "SELECT * FROM PERFIL P JOIN USUARIO_PERFIL UP ON P.ID = UP.ID_PERFIL JOIN USUARIO U ON UP.ID_USUARIO = U.ID WHERE P.PERFIL LIKE 'UA' ORDER BY U.NOME";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			while (rs.next()) {
				int idu = rs.getInt("ID");
				usuarios.add(new Usuario(idu, rs.getString("U.LOGIN"), rs.getString("U.NOME"), null, rs.getTimestamp("U.DATA_CRIACAO"), null));
			}
			return usuarios;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

}
