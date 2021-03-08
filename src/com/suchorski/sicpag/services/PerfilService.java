package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Perfil;

public class PerfilService extends AbstratoService {

	public static List<Perfil> procura(int id) throws DBInfo, DBException {
		String sql = "SELECT * FROM USUARIO_PERFIL A JOIN PERFIL P ON A.ID_PERFIL = P.ID WHERE A.ID_USUARIO = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				List<Perfil> perfis = new ArrayList<Perfil>();
				while (rs.next()) {
					perfis.add(new Perfil(rs.getInt("A.ID"), rs.getString("P.PERFIL"), rs.getString("P.NOME")));
				}
				return perfis;
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static List<Perfil> listar() throws DBInfo, DBException {
		String sql = "SELECT * FROM PERFIL";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			List<Perfil> perfis = new ArrayList<Perfil>();
			while (rs.next()) {
				perfis.add(new Perfil(rs.getInt("ID"), rs.getString("PERFIL"), rs.getString("NOME")));
			}
			if (perfis.isEmpty()) {
				throw new DBInfo("Não existem perfis de cadastro");
			}
			return perfis;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

}
