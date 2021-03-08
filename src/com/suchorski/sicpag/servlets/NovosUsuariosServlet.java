package com.suchorski.sicpag.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.services.SecaoService;
import com.suchorski.sicpag.services.UsuarioService;
import com.suchorski.sicpag.utils.ServletUtils.AccessUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.sicpag.utils.ServletUtils.ParamUtils;
import com.suchorski.sicpag.utils.ServletUtils.ToastUtils;

@WebServlet("/novos_usuarios")
public class NovosUsuariosServlet extends HttpServlet {

	private static final long serialVersionUID = 6470533633266759394L;
	private static final String[] perfis = { "UA", "UM" };
	private static final String deniedMessage = "Para acessar essa função, favor solicitar o perfil UA ou UM a um <a href='./informacoes'>administrador</a> do sistema.";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, perfis, deniedMessage)) {
			try {
				request.setAttribute("inativos", UsuarioService.listarInativos());
				request.setAttribute("secoes", SecaoService.listar());
				request.setAttribute("template", "novos_usuarios");
				request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
			} catch (DBInfo e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.info, request, response);
			} catch (DBException e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, perfis, deniedMessage)) {
			try {
				String acao = ParamUtils.parseString(request, "acao", "desconhecida");
				int idUsuario = ParamUtils.parseInt(request, "id_usuario", 0);
				int idSecao = ParamUtils.parseInt(request, "id_secao", 0);
				switch (acao) {
				case "alocar":
					ParamUtils.checkInts(idUsuario, idSecao);
					UsuarioService.atualizarSecao(idUsuario, idSecao);
					ToastUtils.insert(request, "Usuário alocado com sucesso", TYPE.success);
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
