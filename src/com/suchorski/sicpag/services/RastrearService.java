package com.suchorski.sicpag.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Pendente;
import com.suchorski.sicpag.models.Rastreio;
import com.suchorski.sicpag.models.Usuario;

public class RastrearService extends AbstratoService {

	public static void incluir(Connection c, int idDocumento, int idSecao, int idUsuario) throws DBInfo, SQLException {
		String sql = "INSERT INTO RASTREIO (ID_DOCUMENTO, ID_SECAO, ID_USUARIO, ID_TIPO) VALUES (?, ?, ?, 1)";
		try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, idDocumento);
			ps.setInt(2, idSecao);
			ps.setInt(3, idUsuario);
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Documento não inserido");
			}
		}
	}

	public static void enviar(int idDocumento, String observacao, int idSecaoEnvio, int idUsuario, int idSecao) throws DBInfo, DBException {
		String sql = "INSERT INTO RASTREIO (ID_DOCUMENTO, OBSERVACAO, ID_SECAO, ID_USUARIO, ID_TIPO) VALUES (?, ?, ?, ?, 2)";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, idDocumento);
			ps.setString(2, observacao);
			ps.setInt(3, idSecaoEnvio);
			ps.setInt(4, idUsuario);
			checaDocumento(c, idDocumento, idSecao);
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Documento não enviado");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static void receber(String senha, int idDocumento, int idSecao, int idUsuario) throws DBInfo, DBException {
		try {
			String sql = "INSERT INTO RASTREIO (ID_DOCUMENTO, OBSERVACAO, ID_SECAO, ID_USUARIO, ID_TIPO) VALUES (?, 'Recebimento de documento', ?, ?, 3)";
			Connection c = getConnection(false);
			try {
				SenhaTemporariaService.usar(c, senha, idDocumento);
				try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
					ps.setInt(1, idDocumento);
					ps.setInt(2, idSecao);
					ps.setInt(3, idUsuario);
					if (ps.executeUpdate() == 0) {
						c.rollback();
						throw new DBInfo("Documento não enviado");
					}
				}
				c.commit();
			} catch (DBInfo e) {
				c.rollback();
				throw e;
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
	
	public static void receberSemSenhaTemporaria(int idDocumento, int idSecao, int idUsuario) throws DBInfo, DBException {
		String sql = "INSERT INTO RASTREIO (ID_DOCUMENTO, OBSERVACAO, ID_SECAO, ID_USUARIO, ID_TIPO) VALUES (?, 'Recebimento de documento', ?, ?, 3)";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, idDocumento);
			ps.setInt(2, idSecao);
			ps.setInt(3, idUsuario);
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Documento não está pendente de recebimento");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static void cancelarEnvio(int id, int idSecaoUsuario) throws DBInfo, DBException {
		String sql = "DELETE R.* FROM RASTREIO R JOIN USUARIO U ON R.ID_USUARIO = U.ID WHERE R.ID = ? AND U.ID_SECAO = ? AND R.ID_TIPO = 2";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			checaDocumentoPendenteEnvio(c, id, idSecaoUsuario);
			ps.setInt(1, id);
			ps.setInt(2, idSecaoUsuario);
			if (ps.executeUpdate() == 0) {
				throw new DBInfo("Documento não está pendente de envio");
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static List<Rastreio> rastrearPorId(int idDocumento) throws DBInfo, DBException {
		String sql = "SELECT * FROM RASTREIO R JOIN TIPORASTREIO TR ON R.ID_TIPO = TR.ID JOIN USUARIO U ON R.ID_USUARIO = U.ID JOIN SECAO S ON R.ID_SECAO = S.ID WHERE R.ID_DOCUMENTO = ? ORDER BY DATA_MOVIMENTACAO DESC";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, idDocumento);
			try (ResultSet rs = ps.executeQuery()) {
				List<Rastreio> rastreios = new ArrayList<Rastreio>();
				while (rs.next()) {
					rastreios.add(new Rastreio(
							rs.getInt("R.ID"),
							rs.getString("R.OBSERVACAO"),
							rs.getTimestamp("R.DATA_MOVIMENTACAO"),
							new Usuario(rs.getInt("U.ID"), rs.getString("U.LOGIN"), rs.getString("U.NOME")),
							rs.getString("S.SIGLA"),
							rs.getString("TR.TIPO")
							));
				}
				if (rastreios.isEmpty()) {
					throw new DBInfo("Documento inexistente");
				}
				return rastreios;
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static List<Rastreio> rastrearPorNumero(String numero) throws DBInfo, DBException {
		String sql = "SELECT ID FROM PAG WHERE NUMERO = ? OR PAM = ? UNION ALL SELECT ID FROM DERIVATIVO WHERE PPM = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setString(1, numero);
			ps.setString(2, numero);
			ps.setString(3, numero);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rastrearPorId(rs.getInt("ID"));
				} else {
					throw new DBInfo("Nenhum documento foi encontrado");
				}
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static List<Pendente> listarEnviosPendentes(int idSecao) throws DBException {
		String sql = "SELECT R.ID AS ID, R.ID_DOCUMENTO AS ID_DOCUMENTO, P.NUMERO AS NUMERO, 0 AS CONTAGEM, P.PAM AS PAM_PPM, P.VOLUME AS VOLUME, D.DESCRICAO AS DESCRICAO, 'P' AS TIPO, R.OBSERVACAO AS OBSERVACAO, U.ID AS ID_USUARIO, U.LOGIN AS USUARIO, U.NOME AS NOME, S.SIGLA AS SECAO FROM RASTREIO R LEFT JOIN RASTREIO RT ON R.ID_DOCUMENTO = RT.ID_DOCUMENTO AND R.DATA_MOVIMENTACAO < RT.DATA_MOVIMENTACAO JOIN DOCUMENTO D ON R.ID_DOCUMENTO = D.ID JOIN PAG P ON D.ID = P.ID_DOCUMENTO JOIN USUARIO U ON R.ID_USUARIO = U.ID JOIN SECAO S ON R.ID_SECAO = S.ID WHERE RT.DATA_MOVIMENTACAO IS NULL AND R.ID_TIPO = 2 AND R.ID_SECAO <> ? AND U.ID_SECAO = ? UNION ALL SELECT R.ID, R.ID_DOCUMENTO, P.NUMERO, A.CONTAGEM, A.PPM, P.VOLUME, D.DESCRICAO, 'D', R.OBSERVACAO, U.ID, U.LOGIN, U.NOME, S.SIGLA FROM RASTREIO R LEFT JOIN RASTREIO RT ON R.ID_DOCUMENTO = RT.ID_DOCUMENTO AND R.DATA_MOVIMENTACAO < RT.DATA_MOVIMENTACAO JOIN DOCUMENTO D ON R.ID_DOCUMENTO = D.ID JOIN DERIVATIVO A ON D.ID = A.ID_DOCUMENTO JOIN PAG P ON A.ID_PAG = P.ID JOIN USUARIO U ON R.ID_USUARIO = U.ID JOIN SECAO S ON R.ID_SECAO = S.ID WHERE RT.DATA_MOVIMENTACAO IS NULL AND R.ID_TIPO = 2 AND R.ID_SECAO <> ? AND U.ID_SECAO = ?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, idSecao);
			ps.setInt(2, idSecao);
			ps.setInt(3, idSecao);
			ps.setInt(4, idSecao);
			try (ResultSet rs = ps.executeQuery()) {
				List<Pendente> pendentes = new ArrayList<Pendente>();
				while (rs.next()) {
					pendentes.add(new Pendente(rs.getInt("ID"), rs.getInt("ID_DOCUMENTO"), rs.getString("NUMERO"), rs.getInt("CONTAGEM"), rs.getString("PAM_PPM"), rs.getInt("VOLUME"), rs.getString("TIPO"), rs.getString("OBSERVACAO"), new Usuario(rs.getInt("ID_USUARIO"), rs.getString("USUARIO"), rs.getString("NOME")), rs.getString("SECAO")));
				}
				return pendentes;
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static List<Pendente> listarRecebimentosPendentes(int idSecao) throws DBInfo, DBException {
		String sql = "SELECT R.ID AS ID, R.ID_DOCUMENTO AS ID_DOCUMENTO, P.NUMERO AS NUMERO, 0 AS CONTAGEM, P.PAM AS PAM_PPM, P.VOLUME AS VOLUME, D.DESCRICAO AS DESCRICAO, 'P' AS TIPO, R.OBSERVACAO AS OBSERVACAO, U.ID AS ID_USUARIO, U.LOGIN AS USUARIO, U.NOME AS NOME, S.SIGLA AS SECAO FROM RASTREIO R LEFT JOIN RASTREIO RT ON R.ID_DOCUMENTO = RT.ID_DOCUMENTO AND R.DATA_MOVIMENTACAO < RT.DATA_MOVIMENTACAO JOIN DOCUMENTO D ON R.ID_DOCUMENTO = D.ID JOIN PAG P ON D.ID = P.ID_DOCUMENTO JOIN USUARIO U ON R.ID_USUARIO = U.ID JOIN SECAO S ON R.ID_SECAO = S.ID WHERE RT.DATA_MOVIMENTACAO IS NULL AND R.ID_TIPO = 2 AND R.ID_SECAO = ? UNION ALL SELECT R.ID, R.ID_DOCUMENTO, P.NUMERO, A.CONTAGEM, A.PPM, P.VOLUME, D.DESCRICAO, 'D', R.OBSERVACAO, U.ID, U.LOGIN, U.NOME, S.SIGLA FROM RASTREIO R LEFT JOIN RASTREIO RT ON R.ID_DOCUMENTO = RT.ID_DOCUMENTO AND R.DATA_MOVIMENTACAO < RT.DATA_MOVIMENTACAO JOIN DOCUMENTO D ON R.ID_DOCUMENTO = D.ID JOIN DERIVATIVO A ON D.ID = A.ID_DOCUMENTO JOIN PAG P ON A.ID_PAG = P.ID JOIN USUARIO U ON R.ID_USUARIO = U.ID JOIN SECAO S ON R.ID_SECAO = S.ID WHERE RT.DATA_MOVIMENTACAO IS NULL AND R.ID_TIPO = 2 AND R.ID_SECAO = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, idSecao);
			ps.setInt(2, idSecao);
			try (ResultSet rs = ps.executeQuery()) {
				List<Pendente> pendentes = new ArrayList<Pendente>();
				while (rs.next()) {
					pendentes.add(new Pendente(rs.getInt("ID"), rs.getInt("ID_DOCUMENTO"), rs.getString("NUMERO"), rs.getInt("CONTAGEM"), rs.getString("PAM_PPM"), rs.getInt("VOLUME"), rs.getString("TIPO"), rs.getString("OBSERVACAO"), new Usuario(rs.getInt("ID_USUARIO"), rs.getString("USUARIO"), rs.getString("NOME")), rs.getString("SECAO")));
				}
				if (pendentes.isEmpty()) {
					throw new DBInfo("Nenhum documento pendente de recebimento");
				}
				return pendentes;
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static List<Pendente> listarDocumentosSecao(int idSecao) throws DBException {
		String sql = "SELECT R.ID AS ID, R.ID_DOCUMENTO AS ID_DOCUMENTO, P.NUMERO AS NUMERO, 0 AS CONTAGEM, P.PAM AS PAM_PPM, P.VOLUME AS VOLUME, D.DESCRICAO AS DESCRICAO, 'P' AS TIPO, R.OBSERVACAO AS OBSERVACAO, U.ID AS ID_USUARIO, U.LOGIN AS USUARIO, U.NOME AS NOME, S.SIGLA AS SECAO FROM RASTREIO R LEFT JOIN RASTREIO RT ON R.ID_DOCUMENTO = RT.ID_DOCUMENTO AND R.DATA_MOVIMENTACAO < RT.DATA_MOVIMENTACAO JOIN DOCUMENTO D ON R.ID_DOCUMENTO = D.ID JOIN PAG P ON D.ID = P.ID_DOCUMENTO JOIN USUARIO U ON R.ID_USUARIO = U.ID JOIN SECAO S ON R.ID_SECAO = S.ID WHERE RT.DATA_MOVIMENTACAO IS NULL AND R.ID_TIPO <> 2 AND R.ID_SECAO = ? UNION ALL SELECT R.ID, R.ID_DOCUMENTO, P.NUMERO, A.CONTAGEM, A.PPM, P.VOLUME, D.DESCRICAO, 'D', R.OBSERVACAO, U.ID, U.LOGIN, U.NOME, S.SIGLA FROM RASTREIO R LEFT JOIN RASTREIO RT ON R.ID_DOCUMENTO = RT.ID_DOCUMENTO AND R.DATA_MOVIMENTACAO < RT.DATA_MOVIMENTACAO JOIN DOCUMENTO D ON R.ID_DOCUMENTO = D.ID JOIN DERIVATIVO A ON D.ID = A.ID_DOCUMENTO JOIN PAG P ON A.ID_PAG = P.ID JOIN USUARIO U ON R.ID_USUARIO = U.ID JOIN SECAO S ON R.ID_SECAO = S.ID WHERE RT.DATA_MOVIMENTACAO IS NULL AND R.ID_TIPO <> 2 AND R.ID_SECAO = ?";
		try (Connection c = getConnection(); PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, idSecao);
			ps.setInt(2, idSecao);
			try (ResultSet rs = ps.executeQuery()) {
				List<Pendente> pendentes = new ArrayList<Pendente>();
				while (rs.next()) {
					pendentes.add(new Pendente(rs.getInt("ID"), rs.getInt("ID_DOCUMENTO"), rs.getString("NUMERO"), rs.getInt("CONTAGEM"), rs.getString("PAM_PPM"), rs.getInt("VOLUME"), rs.getString("TIPO"), rs.getString("OBSERVACAO"), new Usuario(rs.getInt("ID_USUARIO"), rs.getString("USUARIO"), rs.getString("NOME")), rs.getString("SECAO")));
				}
				return pendentes;
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	public static void checaDocumento(Connection c, int idDocumento, int idSecao) throws DBInfo, DBException {
		String sql = "SELECT * FROM RASTREIO WHERE ID_DOCUMENTO = ? ORDER BY DATA_MOVIMENTACAO DESC LIMIT 1";
		try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, idDocumento);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next() || rs.getInt("ID_TIPO") == 2 || rs.getInt("ID_SECAO") != idSecao) {
					throw new DBInfo("Documento não está na seção");
				}
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	private static void checaDocumentoPendenteEnvio(Connection c, int idRastreio, int idSecaoUsuario) throws DBInfo, SQLException {
		String sql = "SELECT R.ID FROM RASTREIO R LEFT JOIN RASTREIO RT ON R.ID_DOCUMENTO = RT.ID_DOCUMENTO AND R.DATA_MOVIMENTACAO < RT.DATA_MOVIMENTACAO JOIN USUARIO U ON R.ID_USUARIO = U.ID WHERE RT.DATA_MOVIMENTACAO IS NULL AND R.ID = ? AND U.ID_SECAO = ?";
		try (PreparedStatement ps = (PreparedStatement) c.prepareStatement(sql)) {
			ps.setInt(1, idRastreio);
			ps.setInt(2, idSecaoUsuario);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {					
					throw new DBInfo("Documento não está pendente de envio");
				}
			}
		}
	}

}
