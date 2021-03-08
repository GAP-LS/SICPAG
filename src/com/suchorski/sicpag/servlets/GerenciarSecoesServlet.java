package com.suchorski.sicpag.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Secao;
import com.suchorski.sicpag.models.Usuario;
import com.suchorski.sicpag.services.SecaoService;
import com.suchorski.sicpag.services.TipoSecaoService;
import com.suchorski.sicpag.utils.ServletUtils.AccessUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.sicpag.utils.ServletUtils.ParamUtils;
import com.suchorski.sicpag.utils.ServletUtils.ToastUtils;

@WebServlet("/gerenciar_secoes")
public class GerenciarSecoesServlet extends HttpServlet {

	private static final long serialVersionUID = 4064773927315805843L;
	private static final String[] perfis = { "UA", "UM" };
	private static final String deniedMessage = "Para acessar essa função, favor solicitar o perfil UA ou UM a um <a href='./informacoes'>administrador</a> do sistema.";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, perfis, deniedMessage)) {
			try {
				Usuario u = (Usuario) request.getSession().getAttribute("usuario");
				int secao = ParamUtils.parseInt(request, "secao", u.getSecao().getId());
				request.setAttribute("secaoSelecionada", SecaoService.procurar(secao));
				request.setAttribute("tipos", TipoSecaoService.listar());
				request.setAttribute("secoesFilhas", SecaoService.procurarFilhas(secao));
				request.setAttribute("secoes", SecaoService.listar());
				request.setAttribute("template", "gerenciar_secoes");
				request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
			} catch (Exception e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, perfis, deniedMessage)) {
			try {
				String acao = ParamUtils.parseString(request, "acao", "desconhecida");
				int id = ParamUtils.parseInt(request, "id", 0);
				String sigla = ParamUtils.parseString(request, "sigla", "");
				int idPai = ParamUtils.parseInt(request, "id_pai", 0);
				int idTipo = ParamUtils.parseInt(request, "id_tipo", 0);
				switch (acao) {
				case "incluir":
					ParamUtils.checkInts(idTipo);
					ParamUtils.checkStrings(sigla);
					SecaoService.incluir(sigla, idPai, idTipo);
					ToastUtils.insert(request, "Seção adicionada com sucesso", TYPE.success);
					break;
				case "editar":
					ParamUtils.checkInts(id, idTipo);
					ParamUtils.checkStrings(sigla);
					SecaoService.atualizar(new Secao(id, sigla, idPai, idTipo));
					ToastUtils.insert(request, "Seção atualizada com sucesso", TYPE.success);
					break;
				case "excluir":
					ParamUtils.checkInts(id);
					SecaoService.excluir(id);
					ToastUtils.insert(request, "Seção removida com sucesso", TYPE.success);
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
