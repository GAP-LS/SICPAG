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
import com.suchorski.sicpag.utils.ServletUtils.ParamUtils;
import com.suchorski.sicpag.utils.ServletUtils.ToastUtils;

@WebServlet("/receber")
public class ReceberServlet extends HttpServlet {

	private static final long serialVersionUID = 5353448219306219925L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Usuario u = (Usuario) request.getSession().getAttribute("usuario");
			request.setAttribute("pendentes", RastrearService.listarRecebimentosPendentes(u.getSecao().getId()));
		} catch (DBInfo e) {
			request.setAttribute("info", e.getMessage());
		} catch (DBException e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			return;
		}
		request.setAttribute("template", "receber");
		request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Usuario u = (Usuario) request.getSession().getAttribute("usuario");
			String acao = ParamUtils.parseString(request, "acao", "desconhecida");
			int idDocumento = ParamUtils.parseInt(request, "id_documento", 0);
//			String senhaTemporaria = ParamUtils.parseString(request, "senha_temporaria", "");
			switch (acao) {
			case "receber":
					ParamUtils.checkInts(idDocumento);
//					ParamUtils.checkStrings(senhaTemporaria);
//					RastrearService.receber(senhaTemporaria, idDocumento, u.getSecao().getId(), u.getId());
					RastrearService.receberSemSenhaTemporaria(idDocumento, u.getSecao().getId(), u.getId());
					ToastUtils.insert(request, "Documento recebido com sucesso", TYPE.success);
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
