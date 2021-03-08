package com.suchorski.sicpag.servlets;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Pendente;
import com.suchorski.sicpag.models.Secao;
import com.suchorski.sicpag.models.Usuario;
import com.suchorski.sicpag.services.RastrearService;
import com.suchorski.sicpag.services.SecaoService;
import com.suchorski.sicpag.utils.ServletUtils.AccessUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.sicpag.utils.ServletUtils.ParamUtils;
import com.suchorski.sicpag.utils.ServletUtils.ToastUtils;

@WebServlet("/enviar")
public class EnviarServlet extends HttpServlet {

	private static final long serialVersionUID = 6745936794674485858L;
	private static final String[] perfis = { "AT" };
	private static final String deniedMessage = "Para acessar essa função, favor solicitar o perfil AT a um <a href='./informacoes'>administrador</a> do sistema.";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, perfis, deniedMessage)) {
			try {
				Usuario u = (Usuario) request.getSession().getAttribute("usuario");
				List<Pendente> pendentes = RastrearService.listarEnviosPendentes(u.getSecao().getId());
				List<Pendente> documentosSecao = RastrearService.listarDocumentosSecao(u.getSecao().getId());
				List<Secao> secoes = SecaoService.listar().stream().filter(s -> s.getId() != u.getSecao().getId()).collect(Collectors.toList());
				if (secoes.isEmpty()) {
					MessageUtils.dispatch("Não existem seções para enviar", TYPE.warning, request, response);					
				} else if (pendentes.isEmpty() && documentosSecao.isEmpty()) {
					MessageUtils.dispatch("Seção sem documentos pendentes de envio", TYPE.info, request, response);					
				} else {
					request.setAttribute("info_pendentes", "Nenhum documento pendente de envio");
					request.setAttribute("pendentes", pendentes);
					request.setAttribute("info_documentos_secao", "Nenhum documento na seção a ser enviado");
					request.setAttribute("documentos_secao", documentosSecao);
					request.setAttribute("secoes", secoes);
					request.setAttribute("template", "enviar");
					request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
				}
			} catch (DBException e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, perfis, deniedMessage)) {
			try {
				Usuario u = (Usuario) request.getSession().getAttribute("usuario");
				String acao = ParamUtils.parseString(request, "acao", "desconhecida");
				int id = ParamUtils.parseInt(request, "id", 0);
				int idDocumento = ParamUtils.parseInt(request, "id_documento", 0);
				String observacao = ParamUtils.parseString(request, "observacao", "");
				int idSecaoEnvio = ParamUtils.parseInt(request, "id_secao_envio", 0);
				switch (acao) {
				case "enviar":
					ParamUtils.checkInts(idDocumento, idSecaoEnvio);
					RastrearService.enviar(idDocumento, observacao, idSecaoEnvio, u.getId(), u.getSecao().getId());
					ToastUtils.insert(request, "Documento enviado com sucesso", TYPE.success);
					break;
				case "excluir":
					ParamUtils.checkInts(id);
					RastrearService.cancelarEnvio(id, u.getSecao().getId());
					ToastUtils.insert(request, "Envio cancelado com sucesso", TYPE.success);
					break;
				default:
					break;
				}
				doGet(request, response);
			} catch (DBInfo e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
			} catch (Exception e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			}
		}
	}

}
