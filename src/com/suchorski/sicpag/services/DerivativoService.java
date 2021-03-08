package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Derivativo;
import com.suchorski.sicpag.models.Documento;
import com.suchorski.sicpag.models.Modalidade;
import com.suchorski.sicpag.models.Pag;
import com.suchorski.sicpag.models.TipoDerivativo;
import com.suchorski.sicpag.models.Usuario;

public class DerivativoService extends AbstratoService {

	public static void incluir(int contagem, String ppm, String volume, int idPag, String descricao, int idModalidade, int idTipoDerivativo, int idUsuario, int idSecao) throws DBInfo, DBException {
		try {
			String sql = "INSERT INTO DERIVATIVO (CONTAGEM, PPM, VOLUME, ID_PAG, ID_DOCUMENTO, ID_TIPODERIVATIVO) VALUES (?, ?, ?, ?, ?, ?)";
			Connection c = getConnection(false);
			try {
				int idDocumento = DocumentoService.incluir(c, descricao, idModalidade, idUsuario, idSecao);
				try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
					ps.setInt(1, contagem);
					ps.setString(2, ppm);
					ps.setString(3, volume);
					ps.setInt(4, idPag);
					ps.setInt(5, idDocumento);
					ps.setInt(6, idTipoDerivativo);
					if (ps.executeUpdate() == 0) {
						c.rollback();
						throw new DBInfo("Nenhum derivativo foi adicionado");
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

	public static List<Derivativo> listar(int offset, int maxRows, String search) throws DBInfo, DBException {
		try (Connection c = getConnection()) {
			String sql;
			boolean hasSearch = (search != null && !search.isEmpty());
			if (hasSearch) {
				sql = "SELECT * FROM DERIVATIVO A JOIN PAG P ON A.ID_PAG = P.ID JOIN DOCUMENTO D ON A.ID_DOCUMENTO = D.ID JOIN TIPODERIVATIVO TD ON A.ID_TIPODERIVATIVO = TD.ID JOIN MODALIDADE M ON D.ID_MODALIDADE = M.ID JOIN USUARIO U ON D.ID_USUARIO = U.ID WHERE P.NUMERO LIKE ? OR A.PPM LIKE ? OR P.SECAO LIKE ? OR D.DESCRICAO LIKE ? ORDER BY A.PPM LIMIT ?, ?";
			} else {
				sql = "SELECT * FROM DERIVATIVO A JOIN PAG P ON A.ID_PAG = P.ID JOIN DOCUMENTO D ON A.ID_DOCUMENTO = D.ID JOIN TIPODERIVATIVO TD ON A.ID_TIPODERIVATIVO = TD.ID JOIN MODALIDADE M ON D.ID_MODALIDADE = M.ID JOIN USUARIO U ON D.ID_USUARIO = U.ID ORDER BY A.PPM LIMIT ?, ?";
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
					List<Derivativo> derivativos = new ArrayList<Derivativo>();
					while (rs.next()) {
						derivativos.add(new Derivativo(
								rs.getInt("A.ID"),
								rs.getInt("A.CONTAGEM"),
								rs.getString("A.PPM"),
								rs.getString("A.VOLUME"),
								new Pag(rs.getInt("P.ID"), rs.getString("P.NUMERO"), rs.getString("P.PAM"), rs.getString("P.VOLUME"), rs.getString("P.SECAO"), null),
								new Documento(
										rs.getInt("D.ID"),
										rs.getString("D.DESCRICAO"),
										new Modalidade(rs.getInt("M.ID"), rs.getString("M.TIPO")),
										new Usuario(rs.getInt("U.ID"), rs.getString("U.LOGIN"), rs.getString("U.NOME"), null, rs.getTimestamp("U.DATA_CRIACAO"))
										),
								new TipoDerivativo(rs.getInt("TD.ID"), rs.getString("TD.TIPO"))
								));
					}
					if (!derivativos.isEmpty()) {
						return derivativos;
					} else {
						if (hasSearch) {
							throw new DBInfo("Nenhum registro encontrado");
						} else {
							throw new DBInfo("Nenhum derivativo encontrado");
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
				sql = "SELECT COUNT(*) AS TOTAL FROM DERIVATIVO A JOIN PAG P ON A.ID_PAG = P.ID JOIN DOCUMENTO D ON A.ID_DOCUMENTO = D.ID JOIN MODALIDADE M ON D.ID_MODALIDADE = M.ID JOIN USUARIO U ON D.ID_USUARIO = U.ID WHERE P.NUMERO LIKE ? OR A.PPM LIKE ? OR P.SECAO LIKE ? OR D.DESCRICAO LIKE ?";
			} else {
				sql = "SELECT COUNT(*) AS TOTAL FROM DERIVATIVO A JOIN PAG P ON A.ID_PAG = P.ID JOIN DOCUMENTO D ON A.ID_DOCUMENTO = D.ID JOIN MODALIDADE M ON D.ID_MODALIDADE = M.ID JOIN USUARIO U ON D.ID_USUARIO = U.ID";
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

	public static void atualizar(Derivativo derivativo) throws DBInfo, DBException {
		try {
			String sql = "UPDATE DERIVATIVO SET CONTAGEM = ?, PPM = ?, VOLUME = ?, ID_PAG = ?, ID_TIPODERIVATIVO = ? WHERE ID = ?";
			Connection c = getConnection(false);
			try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
				DocumentoService.atualizar(c, derivativo.getDocumento());
				ps.setInt(1, derivativo.getContagem());
				ps.setString(2, derivativo.getPpm());
				ps.setString(3, derivativo.getVolume());
				ps.setInt(4, derivativo.getPag().getId());
				ps.setInt(5, derivativo.getTipoDerivativo().getId());
				ps.setInt(6, derivativo.getId());
				if (ps.executeUpdate() == 0) {
					c.rollback();
					throw new DBInfo("Nenhum derivativo foi atualizado");
				}
				c.commit();
			} catch (DBInfo e) {
				c.rollback();
				throw new DBInfo("Nenhum derivativo foi atualizado");
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
			throw new DBInfo("Nenhum derivativo foi excluído");
		}
	}

}
