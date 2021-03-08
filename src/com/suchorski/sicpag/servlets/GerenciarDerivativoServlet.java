package com.suchorski.sicpag.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.sicpag.exceptions.DBException;
import com.suchorski.sicpag.exceptions.DBInfo;
import com.suchorski.sicpag.models.Derivativo;
import com.suchorski.sicpag.models.Documento;
import com.suchorski.sicpag.models.Modalidade;
import com.suchorski.sicpag.models.Pag;
import com.suchorski.sicpag.models.TipoDerivativo;
import com.suchorski.sicpag.models.Usuario;
import com.suchorski.sicpag.services.DerivativoService;
import com.suchorski.sicpag.services.ModalidadeService;
import com.suchorski.sicpag.services.PagService;
import com.suchorski.sicpag.services.TipoDerivativoService;
import com.suchorski.sicpag.utils.PaginationUtils;
import com.suchorski.sicpag.utils.PaginationUtils.PaginationInfoUtil;
import com.suchorski.sicpag.utils.ServletUtils.AccessUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils;
import com.suchorski.sicpag.utils.ServletUtils.MessageUtils.TYPE;
import com.suchorski.sicpag.utils.ServletUtils.ParamUtils;
import com.suchorski.sicpag.utils.ServletUtils.ToastUtils;

@WebServlet("/gerenciar_derivativo")
public class GerenciarDerivativoServlet extends HttpServlet {
	
	private static final long serialVersionUID = -5800155618387448202L;
	private static final String[] profiles = { "AG", "AE" };
	private static final String deniedMessage = "Para acessar essa função, favor solicitar o perfil AG ou AE a um <a href='./informacoes'>administrador</a> do sistema.";
	private static final String[] upProfiles = { "AG" };
	private static final String upDeniedMessage = "Para acessar essa função, favor solicitar o perfil AG a um <a href='./informacoes'>administrador</a> do sistema.";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Usuario u = (Usuario) request.getSession().getAttribute("usuario");
		List<Pag> pags;
		try {
			pags = PagService.listar();
		} catch (DBException | DBInfo e) {
			MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
			return;
		}
		if (AccessUtils.hasAccess(request, response, profiles, deniedMessage)) {
			try {
				request.setAttribute("busca", ParamUtils.parseString(request, "busca", ""));
				request.setAttribute("modalidades", ModalidadeService.listar());
				request.setAttribute("tipos", TipoDerivativoService.listar());
				request.setAttribute("pags", pags);
				request.setAttribute("podeRastrear", u.hasAcesso("CR"));
				request.setAttribute("podeExcluir", u.hasAcesso("AG"));
				PaginationInfoUtil pi = PaginationUtils.paginate(request);
				List<Derivativo> derivativos = DerivativoService.listar(pi.offset, pi.maxRowsPerPage, request.getParameter("busca"));
				request.setAttribute("page", pi.page);
				request.setAttribute("pages", pi.numPages(DerivativoService.listarCount(request.getParameter("busca"))));
				request.setAttribute("derivativos", derivativos);
			} catch (DBInfo e) {
				request.setAttribute("error", true);
				request.setAttribute("message", e.getMessage());
			} catch (Exception e) {
				MessageUtils.dispatch(e.getMessage(), TYPE.warning, request, response);
				return;
			}
			request.setAttribute("template", "gerenciar_derivativo");
			request.getRequestDispatcher("/WEB-INF/jsp/common/main.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Usuario u = (Usuario) request.getSession().getAttribute("usuario");
			String acao = ParamUtils.parseString(request, "acao", "desconhecida");
			int id = ParamUtils.parseInt(request, "id", 0);
			int idPag = ParamUtils.parseInt(request, "id_pag", 0);
			int contagem = ParamUtils.parseInt(request, "contagem", 0);
			String numeroPpm = ParamUtils.parseString(request, "numero_ppm", "");
			String volume = ParamUtils.parseString(request, "volume", "");
			String descricao = ParamUtils.parseString(request, "descricao", "");
			int idModalidade = ParamUtils.parseInt(request, "id_modalidade", 0);
			int idDocumento = ParamUtils.parseInt(request, "id_documento", 0);
			int idTipoDerivativo = ParamUtils.parseInt(request, "id_tipoderivativo", 0);
			switch (acao) {
			case "incluir":
				if (AccessUtils.hasAccess(request, response, profiles, deniedMessage)) {
					ParamUtils.checkInts(idPag, idModalidade);
					ParamUtils.checkStrings(volume, descricao);
					DerivativoService.incluir(contagem, numeroPpm, volume, idPag, descricao, idModalidade, idTipoDerivativo, u.getId(), u.getSecao().getId());
					ToastUtils.insert(request, "Derivativo adicionado com sucesso", TYPE.success);
				}
				break;
			case "editar":
				if (AccessUtils.hasAccess(request, response, profiles, deniedMessage)) {
					ParamUtils.checkInts(id, idPag, idDocumento, idModalidade);
					ParamUtils.checkStrings(volume, descricao);
					DerivativoService.atualizar(new Derivativo(id, contagem, numeroPpm, volume, new Pag(idPag, "", "", "", "", null), new Documento(idDocumento, descricao, new Modalidade(idModalidade, ""), null), new TipoDerivativo(idTipoDerivativo, "")));
					ToastUtils.insert(request, "Derivativo atualizado com sucesso", TYPE.success);
				}
				break;
			case "excluir":
				if (AccessUtils.hasAccess(request, response, upProfiles, upDeniedMessage)) {
					ParamUtils.checkInts(id);
					DerivativoService.excluir(id);
					ToastUtils.insert(request, "Derivativo removido com sucesso", TYPE.success);
				}
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
