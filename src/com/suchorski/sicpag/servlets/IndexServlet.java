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
import com.suchorski.sicpag.services.RastrearService;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = -1194117735276465115L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Usuario u = (Usuario) request.getSession().getAttribute("usuario");
			RastrearService.listarRecebimentosPendentes(u.getSecao().getId());
			response.sendRedirect("./receber");
		} catch (DBInfo e) {
			response.sendRedirect("./rastrear");
		} catch (DBException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
		}
	}

}
