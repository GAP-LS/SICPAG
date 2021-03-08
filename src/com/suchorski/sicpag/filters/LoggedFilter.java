package com.suchorski.sicpag.filters;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Usuario;
import com.suchorski.sicpag.services.UsuarioService;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;


@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, urlPatterns = { "/*" })
public class LoggedFilter implements Filter {
	
	private static final String[] NOT_LOGGED_PAGES = {"/js/", "/css/", "/img/", "/fonts/", "/login", "/ajuda", "/informacoes"};

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		boolean doChain = false;
		String message = "";
		HttpServletRequest req = (HttpServletRequest) request;
		try {
			if (Arrays.asList(NOT_LOGGED_PAGES).stream().anyMatch(s -> req.getRequestURI().contains(s))) {
				doChain = true;
			} else {
				Boolean b = (Boolean) req.getSession().getAttribute("logado");
				if (b) {
					Usuario u = (Usuario) req.getSession().getAttribute("usuario");
					if (u != null) {
						u = UsuarioService.procura(u.getId());
						if (u.isBlocked()) {
							req.getSession().invalidate();
							message = "Usuário está bloqueado pelo sistema por ter o perfil UB";
						} else {
							if (u.hasAcesso("UN")) {							
								req.getSession().setAttribute("usuario", u);
								req.getSession().setAttribute("userName", u.getNome());
								doChain = true;
							} else {
								req.getSession().invalidate();
								message = "Para acessar essa função, favor solicitar o perfil UN a um <a href='./informacoes'>administrador</a> do sistema.";
							}
						}
					} else {					
						req.getSession().invalidate();
					}
				}
			}
		} catch (DBInfo e) {
			req.getSession().setAttribute("logado", false);
			message = e.getMessage();
		} catch (DBException e) {
			req.getSession().setAttribute("logado", false);
			doChain = false;
		}
		if (doChain) {
			chain.doFilter(request, response);
		} else {
			if (message.isEmpty()) {				
				((HttpServletResponse) response).sendRedirect("./login");
			} else {
				MessageUtils.dispatch(message, TYPE.danger, (HttpServletRequest) request, (HttpServletResponse) response);
			}
		}
	}

	public void destroy() {
		return;
	}

	public void init(FilterConfig fConfig) throws ServletException {
		return;
	}

}
