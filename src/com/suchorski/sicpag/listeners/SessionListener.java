package com.suchorski.sicpag.listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event)  { 
		HttpSession session = event.getSession();
		session.setAttribute("logado", false);
		session.setAttribute("usuario", null);
	}

	public void sessionDestroyed(HttpSessionEvent event)  {
		HttpSession session = event.getSession();
		session.removeAttribute("logado");
		session.removeAttribute("usuario");
	}

}
