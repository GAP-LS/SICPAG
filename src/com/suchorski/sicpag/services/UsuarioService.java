package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Usuario;

public class UsuarioService extends AbstratoService {

	public static void inserir(String login, String nome) throws DBInfo, SQLException {
		String sql = "INSERT INTO USUARIO (LOGIN, NOME) VALUES (?, ?)";
		if (procura(login)) {
			atualizaNome(login, nome);			
		} else {
			try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
				ps.setString(1, login);
				ps.setString(2, nome);
				ps.executeUpdate();
			} catch (SQLException e) {
				return;
			}
		}
	}

	private static void atualizaNome(String login, String nome) throws DBInfo, SQLException {
		String sql = "UPDATE USUARIO SET NOME = ? WHERE LOGIN = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setString(1, nome);
			ps.setString(2, login);
			ps.executeUpdate();
		}
	}

	public static Usuario autentica(String login, String senha) throws DBInfo, DBException {
		try {
			String nome = ldapLogin(login, senha);
			inserir(login, nome);
			String sql = "SELECT * FROM USUARIO U WHERE U.LOGIN LIKE ?";
			try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
				ps.setString(1, login);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						int idu = rs.getInt("ID");
						return new Usuario(idu, rs.getString("LOGIN"), rs.getString("NOME"), SecaoService.secaoUsuario(rs.getInt("ID_SECAO")), rs.getTimestamp("DATA_CRIACAO"), PerfilService.procura(idu));
					}
					throw new DBInfo("Usuário ou senha incorretos");
				}
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		} catch (Exception e) {
			throw new DBInfo(e.getMessage());
		}
	}

	public static Usuario procura(int id) throws DBInfo, DBException {
		String sql = "SELECT * FROM USUARIO U WHERE U.ID = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int idu = rs.getInt("ID");
					return new Usuario(idu, rs.getString("LOGIN"), rs.getString("NOME"), SecaoService.secaoUsuario(rs.getInt("ID_SECAO")), rs.getTimestamp("DATA_CRIACAO"), PerfilService.procura(idu));
				}
				throw new DBInfo("Usuario não encontrado");
			} catch (DBInfo e) {
				throw new DBInfo(e.getMessage());
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static boolean procura(String login) throws SQLException {
		String sql = "SELECT * FROM USUARIO U WHERE U.LOGIN = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setString(1, login);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public static List<Usuario> listarAtivos() throws DBInfo, DBException {
		String sql = "SELECT * FROM USUARIO WHERE ID_SECAO IS NOT NULL ORDER BY LOGIN";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			while (rs.next()) {
				int idu = rs.getInt("ID");
				usuarios.add(new Usuario(idu, rs.getString("LOGIN"), rs.getString("NOME"), SecaoService.secaoUsuario(rs.getInt("ID_SECAO")), rs.getTimestamp("DATA_CRIACAO"), PerfilService.procura(idu)));
			}
			if (usuarios.isEmpty()) {				
				throw new DBInfo("Nenhum usuário ativo no sistema");
			}
			return usuarios;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static List<Usuario> listarInativos() throws DBInfo, DBException {
		String sql = "SELECT * FROM USUARIO WHERE ID_SECAO IS NULL ORDER BY LOGIN";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			while (rs.next()) {
				usuarios.add(new Usuario(rs.getInt("ID"), rs.getString("LOGIN"), rs.getString("NOME"), null, rs.getTimestamp("DATA_CRIACAO"), null));
			}
			if (usuarios.isEmpty()) {				
				throw new DBInfo("Nenhum usuário novo no sistema");
			}
			return usuarios;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static void atualizarSecao(int idUsuario, int idSecao) throws DBInfo, DBException {
		String sql = "UPDATE USUARIO SET ID_SECAO = ? WHERE ID = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, idSecao);
			ps.setInt(2, idUsuario);
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Seção do usuário não foi atualizada");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static void atualizarPerfis(int id, List<Integer> perfis) throws DBException {
		try {
			String sql = "INSERT INTO USUARIO_PERFIL (ID_USUARIO, ID_PERFIL) VALUES (? ,?)";
			Connection c = getConnection(false);
			try {
				removerTodos(c, id);
				for (Integer i : perfis) {
					try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
						ps.setInt(1, id);
						ps.setInt(2, i);
						ps.executeUpdate();
					}
				}
				c.commit();
			} catch (SQLException e) {
				c.rollback();
				throw new DBException(e.getMessage());
			} finally {
				c.close();
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	private static void removerTodos(Connection c, int id) throws SQLException {
		String sql = "DELETE FROM USUARIO_PERFIL WHERE ID_USUARIO = ?";
		try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}

}
