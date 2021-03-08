package com.suchorski.sicpag.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.suchorski.sicpag.models.Usuario;

public class ServletUtils {

	public static class MessageUtils {

		public enum TYPE {
			success, info, warning, danger
		}

		public static void dispatch(String message, TYPE type, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
			request.setAttribute("template", "info");
			request.setAttribute("infoMessage", message);
			request.setAttribute("infoType", type);
			request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
		}

	}

	public static class ParamUtils {

		public static String parseString(HttpServletRequest request, String param, String defaultValue) {
			String value = (String) request.getParameter(param);
			return value != null ? value : defaultValue;
		}

		public static int parseInt(HttpServletRequest request, String param, int defaultValue) {
			try {
				return Integer.parseInt((String) request.getParameter(param));
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		
		public static List<Integer> parseInts(HttpServletRequest request, String param) throws Exception {
			try {
				List<Integer> values = new ArrayList<Integer>();
				String[] strings = request.getParameterValues(param);
				if (strings != null) {
					for (String s : strings) {
						values.add(Integer.parseInt(s));
					}
				}
				return values;
			} catch (NumberFormatException e) {
				throw new Exception("Dados inválidos");
			}
		}
		
		public static void checkInts(int... ints) throws Exception {
			for (int i : ints) {
				if (i == 0) {
					throw new Exception("Dados inválidos");
				}
			}
		}
		
		public static void checkStrings(String... strings) throws Exception {
			for (String s : strings) {
				if (s == null || s.isEmpty()) {
					throw new Exception("Dados inválidos");
				}
			}
		}

	}

	public static class AccessUtils {

		public static boolean hasAccess(HttpServletRequest request, HttpServletResponse response, String[] profiles, String deniedMessage) throws ServletException, IOException {
			Usuario u = (Usuario) request.getSession().getAttribute("usuario");
			if (u.hasAcesso(profiles)) {
				return true;
			}
			MessageUtils.dispatch(deniedMessage, MessageUtils.TYPE.danger, request, response);
			return false;
		}

	}
	
	public static class ToastUtils {
		
		public static void insert(HttpServletRequest request, String message, MessageUtils.TYPE type) {
			HttpSession session = request.getSession();
			session.setAttribute("toastMessage", message);
			session.setAttribute("toastType", type);
		}
		
		public static void make(HttpServletRequest request) {
			HttpSession session = request.getSession();
			if (session.getAttribute("toastMessage") != null) {				
				request.setAttribute("toastMessage", session.getAttribute("toastMessage"));
				request.setAttribute("toastType", session.getAttribute("toastType"));
			}
			session.removeAttribute("toastMessage");
			session.removeAttribute("toastType");
		}
		
	}

}
