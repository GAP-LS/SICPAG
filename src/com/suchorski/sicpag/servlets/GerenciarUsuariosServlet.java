package com.suchorski.sicpag.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Usuario;
import com.suchorski.sicpag.services.PerfilService;
import com.suchorski.sicpag.services.SecaoService;
import com.suchorski.sicpag.services.UsuarioService;
import com.suchorski.sicpag.utils.ServletUtils.AccessUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.sicpag.utils.ServletUtils.ParamUtils;
import com.suchorski.sicpag.utils.ServletUtils.ToastUtils;

@WebServlet("/gerenciar_usuarios")
public class GerenciarUsuariosServlet extends HttpServlet {

	private static final long serialVersionUID = -6469237780792138002L;
	private static final String[] perfis = { "UA" };
	private static final String deniedMessage = "Para acessar essa função, favor solicitar o perfil UA a um <a href='./informacoes'>administrador</a> do sistema.";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, perfis, deniedMessage)) {
			try {
				Usuario u = (Usuario) request.getSession().getAttribute("usuario");
				int idUsuario = ParamUtils.parseInt(request, "id_usuario", 0);
				List<Usuario> usuarios = UsuarioService.listarAtivos();
				request.setAttribute("usuarios", usuarios);
				request.setAttribute("secoes", SecaoService.listar());
				request.setAttribute("perfis", PerfilService.listar());
				if (idUsuario > 0) {				
					request.setAttribute("usuario", UsuarioService.procura(idUsuario));
				} else {
					request.setAttribute("usuario", usuarios.get(0));
				}
				request.setAttribute("developer", u.hasAcesso("UD"));
				request.setAttribute("admin", u.hasAcesso("UA"));
				request.setAttribute("template", "gerenciar_usuarios");
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
				List<Integer> perfis = ParamUtils.parseInts(request, "perfis");
				switch (acao) {
				case "definir_secao":
					ParamUtils.checkInts(idUsuario, idSecao);
					UsuarioService.atualizarSecao(idUsuario, idSecao);
					ToastUtils.insert(request, "Seção definida com sucesso", TYPE.success);
					break;
				case "definir_perfis":
					ParamUtils.checkInts(idUsuario);
					UsuarioService.atualizarPerfis(idUsuario, perfis);
					ToastUtils.insert(request, "Perfis definidos com sucesso", TYPE.success);
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
