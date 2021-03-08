package com.suchorski.sicpag.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Usuario;
import com.suchorski.sicpag.services.SenhaTemporariaService;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.sicpag.utils.ServletUtils.ParamUtils;
import com.suchorski.sicpag.utils.ServletUtils.ToastUtils;

@WebServlet("/senhas_temporarias")
public class SenhasTemporariasServlet extends HttpServlet {

	private static final long serialVersionUID = -7068126622153203398L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Usuario u = (Usuario) request.getSession().getAttribute("usuario");
			request.setAttribute("senhas", SenhaTemporariaService.listar(u.getId()));
		} catch (DBInfo e) {
			request.setAttribute("error", true);
			request.setAttribute("message", e.getMessage());
		} catch (DBException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			return;
		}
		request.setAttribute("template", "senhas_temporarias");
		request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Usuario u = (Usuario) request.getSession().getAttribute("usuario");
			String acao = ParamUtils.parseString(request, "acao", "desconhecida");
			int id = ParamUtils.parseInt(request, "id", 0);
			switch (acao) {
			case "incluir":
				SenhaTemporariaService.criar(u.getId());
				ToastUtils.insert(request, "Senha gerada com sucesso", TYPE.success);
				break;
			case "excluir":
				ParamUtils.checkInts(id);				
				SenhaTemporariaService.excluir(id, u.getId());
				ToastUtils.insert(request, "Senha removida com sucesso", TYPE.success);
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
