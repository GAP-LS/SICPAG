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
import com.suchorski.sicpag.services.UsuarioService;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.sicpag.utils.ServletUtils.ParamUtils;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
       
	private static final long serialVersionUID = 5219173861687892354L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		request.setAttribute("template", "login");
		request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String login = ParamUtils.parseString(request, "login", "");
			String senha = ParamUtils.parseString(request, "senha", "");
			Usuario u = UsuarioService.autentica(login, senha);
			request.getSession().setAttribute("usuario", u);
			request.getSession().setAttribute("logado", true);
			response.sendRedirect("./index");
		} catch (DBInfo e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
		} catch (DBException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
		}
	}

}
