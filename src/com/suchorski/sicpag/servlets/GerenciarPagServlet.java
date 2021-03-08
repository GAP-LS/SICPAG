package com.suchorski.sicpag.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.digitgenerator.exception.InvalidNumberException;
import com.suchorski.digitgenerator.generator.custom.NUPGenerator;
import com.suchorski.digitgenerator.number.custom.NUPNumber;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Documento;
import com.suchorski.sicpag.models.Modalidade;
import com.suchorski.sicpag.models.Pag;
import com.suchorski.sicpag.models.Usuario;
import com.suchorski.sicpag.services.ModalidadeService;
import com.suchorski.sicpag.services.PagService;
import com.suchorski.sicpag.utils.PaginationUtils;
import com.suchorski.sicpag.utils.PaginationUtils.PaginationInfoUtil;
import com.suchorski.sicpag.utils.ServletUtils.AccessUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.sicpag.utils.ServletUtils.ParamUtils;
import com.suchorski.sicpag.utils.ServletUtils.ToastUtils;

@WebServlet("/gerenciar_pag")
public class GerenciarPagServlet extends HttpServlet {

	private static final long serialVersionUID = 8193274282102074771L;
	private static final String[] profiles = { "AG", "AE" };
	private static final String deniedMessage = "Para acessar essa função, favor solicitar o perfil AG ou AE a um <a href='./informacoes'>administrador</a> do sistema.";
	private static final String[] upProfiles = { "AG" };
	private static final String upDeniedMessage = "Para acessar essa função, favor solicitar o perfil AG a um <a href='./informacoes'>administrador</a> do sistema.";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Usuario u = (Usuario) request.getSession().getAttribute("usuario");
		if (AccessUtils.hasAccess(request, response, profiles, deniedMessage)) {
			try {
				request.setAttribute("busca", ParamUtils.parseString(request, "busca", ""));
				request.setAttribute("modalidades", ModalidadeService.listar());
				request.setAttribute("podeRastrear", u.hasAcesso("CR"));
				request.setAttribute("podeExcluir", u.hasAcesso("AG"));
				PaginationInfoUtil pi = PaginationUtils.paginate(request);
				List<Pag> pags = PagService.listar(pi.offset, pi.maxRowsPerPage, request.getParameter("busca"));
				request.setAttribute("page", pi.page);
				request.setAttribute("pages", pi.numPages(PagService.listarCount(request.getParameter("busca"))));
				request.setAttribute("pags", pags);
			} catch (DBInfo e) {
				request.setAttribute("error", true);
				request.setAttribute("message", e.getMessage());
			} catch (Exception e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
				return;
			}
			request.setAttribute("template", "gerenciar_pag");
			request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Usuario u = (Usuario) request.getSession().getAttribute("usuario");
			String acao = ParamUtils.parseString(request, "acao", "desconhecida");
			int id = ParamUtils.parseInt(request, "id", 0);
			String numeroPag = ParamUtils.parseString(request, "numero_pag", "");
			String volume = ParamUtils.parseString(request, "volume", "");
			String descricao = ParamUtils.parseString(request, "descricao", "");
			String numeroPam = ParamUtils.parseString(request, "numero_pam", "");
			String secao = ParamUtils.parseString(request, "secao", "");
			int idModalidade = ParamUtils.parseInt(request, "id_modalidade", 0);
			int idDocumento = ParamUtils.parseInt(request, "id_documento", 0);
			switch (acao) {
			case "incluir":
				if (AccessUtils.hasAccess(request, response, profiles, deniedMessage)) {
					ParamUtils.checkInts(idModalidade);
					ParamUtils.checkStrings(numeroPag, volume, descricao, secao);
					new NUPGenerator().check(new NUPNumber(numeroPag.replaceAll("\\D", "")));
					PagService.incluir(numeroPag, numeroPam, volume, secao, descricao, idModalidade, u.getId(), u.getSecao().getId());
					ToastUtils.insert(request, "PAG adicionado com sucesso", TYPE.success);
				}
				break;
			case "editar":
				if (AccessUtils.hasAccess(request, response, profiles, deniedMessage)) {
					ParamUtils.checkInts(id, idDocumento, idModalidade);
					ParamUtils.checkStrings(numeroPag, volume, descricao, secao);
					new NUPGenerator().check(new NUPNumber(numeroPag.replaceAll("\\D", "")));
					PagService.atualizar(new Pag(id, numeroPag, numeroPam, volume, secao, new Documento(idDocumento, descricao, new Modalidade(idModalidade, ""), new Usuario(0, u.getLogin(), u.getNome(), null, null))));
					ToastUtils.insert(request, "PAG atualizado com sucesso", TYPE.success);
				}
				break;
			case "excluir":
				if (AccessUtils.hasAccess(request, response, upProfiles, upDeniedMessage)) {
					ParamUtils.checkInts(id);
					PagService.excluir(id);
					ToastUtils.insert(request, "PAG removido com sucesso", TYPE.success);
				}
				break;
			default:
				break;
			}
			doGet(request, response);
		} catch (InvalidNumberException e) {
			MessageUtils.dispatch("Número de PAG inválido", TYPE.danger, request, response);
		} catch (DBInfo e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.danger, request, response);
		} catch (Exception e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
		}
	}

}
