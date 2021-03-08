package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Documento;
import com.suchorski.sicpag.models.Modalidade;
import com.suchorski.sicpag.models.Pag;
import com.suchorski.sicpag.models.Usuario;

public class PagService extends AbstratoService {

	public static void incluir(String numero, String pam, String volume, String secao, String descricao, int idModalidade, int idUsuario, int idSecao) throws DBInfo, DBException {
		try {
			String sql = "INSERT INTO PAG (NUMERO, PAM, SECAO, VOLUME, ID_DOCUMENTO) VALUES (?, ?, ?, ?, ?)";
			Connection c = getConnection(false);
			try {
				int idDocumento = DocumentoService.incluir(c, descricao, idModalidade, idUsuario, idSecao);
				try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
					ps.setString(1, numero);
					ps.setString(2, pam);
					ps.setString(3, secao);
					ps.setString(4, volume);
					ps.setInt(5, idDocumento);
					if (ps.executeUpdate() == 0) {
						c.rollback();
						throw new DBInfo("Nenhum PAG foi adicionado");
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

	public static List<Pag> listar(int offset, int maxRows, String search) throws DBInfo, DBException {
		try (Connection c = getConnection()) {
			String sql;
			boolean hasSearch = (search != null && !search.isEmpty());
			if (hasSearch) {
				sql = "SELECT * FROM PAG P JOIN DOCUMENTO D ON P.ID_DOCUMENTO = D.ID JOIN MODALIDADE M ON D.ID_MODALIDADE = M.ID JOIN USUARIO U ON D.ID_USUARIO = U.ID WHERE P.NUMERO LIKE ? OR P.PAM LIKE ? OR P.SECAO LIKE ? OR D.DESCRICAO LIKE ? ORDER BY P.NUMERO LIMIT ?, ?";
			} else {
				sql = "SELECT * FROM PAG P JOIN DOCUMENTO D ON P.ID_DOCUMENTO = D.ID JOIN MODALIDADE M ON D.ID_MODALIDADE = M.ID JOIN USUARIO U ON D.ID_USUARIO = U.ID ORDER BY P.NUMERO LIMIT ?, ?";
			}
			try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
				int pos = 0;
				if (hasSearch) {
					for (int i = 0; i < 4; ++i) {
						ps.setString(++pos, "%" + search + "%");
					}
				}
				ps.setInt(++pos, offset);
				ps.setInt(++pos, maxRows);
				try (ResultSet rs = ps.executeQuery()) {
					List<Pag> pags = new ArrayList<Pag>();
					while (rs.next()) {
						pags.add(new Pag(
								rs.getInt("P.ID"),
								rs.getString("P.NUMERO"),
								rs.getString("P.PAM"),
								rs.getString("VOLUME"),
								rs.getString("P.SECAO"),
								new Documento(
										rs.getInt("D.ID"),
										rs.getString("D.DESCRICAO"),
										new Modalidade(rs.getInt("M.ID"), rs.getString("M.TIPO")),
										new Usuario(rs.getInt("U.ID"), rs.getString("U.LOGIN"), rs.getString("U.NOME"), null, rs.getTimestamp("U.DATA_CRIACAO"))
										)
								));
					}
					if (!pags.isEmpty()) {
						return pags;
					} else {
						if (hasSearch) {
							throw new DBInfo("Nenhum registro encontrado");
						} else {
							throw new DBInfo("Nenhum PAG encontrado");
						}
					}
				}
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static int listarCount(String search) throws DBInfo, DBException {
		try (Connection c = getConnection()) {
			String sql;
			boolean hasSearch = (search != null && !search.isEmpty());
			if (hasSearch) {
				sql = "SELECT COUNT(*) AS TOTAL FROM PAG P JOIN DOCUMENTO D ON P.ID_DOCUMENTO = D.ID JOIN MODALIDADE M ON D.ID_MODALIDADE = M.ID JOIN USUARIO U ON D.ID_USUARIO = U.ID WHERE P.NUMERO LIKE ? OR P.PAM LIKE ? OR P.SECAO LIKE ? OR D.DESCRICAO LIKE ?";
			} else {
				sql = "SELECT COUNT(*) AS TOTAL FROM PAG P JOIN DOCUMENTO D ON P.ID_DOCUMENTO = D.ID JOIN MODALIDADE M ON D.ID_MODALIDADE = M.ID JOIN USUARIO U ON D.ID_USUARIO = U.ID";
			}
			try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
				int pos = 0;
				if (hasSearch) {
					for (int i = 0; i < 4; ++i) {
						ps.setString(++pos, "%" + search + "%");
					}
				}
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt("TOTAL");
					}
					return 0;
				}
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static List<Pag> listar() throws DBInfo, DBException {
		String sql = "SELECT * FROM PAG P JOIN DOCUMENTO D ON P.ID_DOCUMENTO = D.ID JOIN MODALIDADE M ON D.ID_MODALIDADE = M.ID JOIN USUARIO U ON D.ID_USUARIO = U.ID ORDER BY P.NUMERO, P.VOLUME";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			List<Pag> pags = new ArrayList<Pag>();
			while (rs.next()) {
				pags.add(new Pag(
						rs.getInt("P.ID"),
						rs.getString("P.NUMERO"),
						rs.getString("P.PAM"),
						rs.getString("VOLUME"),
						rs.getString("P.SECAO"),
						new Documento(
								rs.getInt("D.ID"),
								rs.getString("D.DESCRICAO"),
								new Modalidade(rs.getInt("M.ID"), rs.getString("M.TIPO")),
								new Usuario(rs.getInt("U.ID"), rs.getString("U.LOGIN"), rs.getString("U.NOME"), null, rs.getTimestamp("U.DATA_CRIACAO"))
								)
						));
			}
			if (pags.isEmpty()) {
				throw new DBInfo("Nenhum PAG cadastrado");
			}
			return pags;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static void atualizar(Pag pag) throws DBInfo, DBException {
		try {
			String sql = "UPDATE PAG SET NUMERO = ?, PAM = ?, VOLUME = ?, SECAO = ? WHERE ID = ?";
			Connection c = getConnection(false);
			try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
				DocumentoService.atualizar(c, pag.getDocumento());
				ps.setString(1, pag.getNumero());
				ps.setString(2, pag.getPam());
				ps.setString(3, pag.getVolume());
				ps.setString(4, pag.getSecao());
				ps.setInt(5, pag.getId());
				if (ps.executeUpdate() == 0) {
					c.rollback();
					throw new DBInfo("Nenhum PAG foi atualizado");
				}
				c.commit();
			} catch (DBInfo e) {
				c.rollback();
				throw new DBInfo("Nenhum PAG foi atualizado");
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

	public static void excluir(int idDocumento) throws DBInfo, DBException {
		try {			
			DocumentoService.excluir(idDocumento);
		} catch (DBInfo e) {
			throw new DBInfo("Nenhum PAG foi excluído");
		}
	}

}
