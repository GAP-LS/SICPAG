package com.suchorski.sicpag.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.services.RastrearService;
import com.suchorski.sicpag.utils.ServletUtils.AccessUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.sicpag.utils.ServletUtils.ParamUtils;

@WebServlet("/rastrear")
public class RastrearServlet extends HttpServlet {

	private static final long serialVersionUID = -854391235008594166L;
	private static final String[] perfis = { "CR" };
	private static final String deniedMessage = "Para acessar essa função, favor solicitar o perfil CR a um <a href='./informacoes'>administrador</a> do sistema.";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (AccessUtils.hasAccess(request, response, perfis, deniedMessage)) {
			try {
				int documento = ParamUtils.parseInt(request, "documento", 0);
				String busca = ParamUtils.parseString(request, "busca", "");
				request.setAttribute("busca", busca);
				if (documento > 0) {
					request.setAttribute("rastreio", RastrearService.rastrearPorId(documento));
				} else if (!busca.isEmpty()) {
					request.setAttribute("rastreio", RastrearService.rastrearPorNumero(busca));
				} else {
					request.setAttribute("rastreio", null);
				}
				request.setAttribute("template", "rastrear");
				request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
			} catch (DBInfo e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.info, request, response);
			} catch (DBException e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			}
		}
	}

}
