package com.suchorski.sicpag.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.services.RelatorioService;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;

@WebServlet("/informacoes")
public class InformacoesServlet extends HttpServlet {

	private static final long serialVersionUID = 5723264491089701695L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setAttribute("administradores", RelatorioService.listarAdministradores());
			request.setAttribute("template", "informacoes");
			request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
		} catch (DBException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
		}
	}

}
