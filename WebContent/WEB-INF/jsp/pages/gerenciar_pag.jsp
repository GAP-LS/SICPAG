<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Gerenciar PAG</h1>
<div class="row">
	<form method="get">
		<div class="col-sm-5">
			<div class="form-group">
				<input type="text" class="form-control" name="busca"
					placeholder="Busca por: PAG, PAM, Seção ou descrição" />
			</div>
		</div>
		<div class="col-sm-3">
			<input type="submit" class="btn btn-primary btn-block" value="Buscar" />
		</div>
	</form>
	<div class="col-sm-4">
		<button class="btn btn-success btn-block"
			onclick="incluirPag('${usuario.nome}')">Incluir PAG</button>
	</div>
</div>
<h2>PAGs</h2>
<c:choose>
	<c:when test="${error}">
		<div class="alert alert-info">
			<strong>${message}</strong>
		</div>
	</c:when>
	<c:otherwise>
		<%@ include file="../common/pagination.jsp"%>
		<table class="table">
			<thead>
				<tr>
					<th class="col-sm-2">PAG</th>
					<th class="col-sm-5">Descrição</th>
					<th class="col-sm-1">PAM</th>
					<th class="col-sm-1">Volume</th>
					<th class="col-sm-1">Seção</th>
					<th class="col-sm-2">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="pag" items="${pags}">
					<tr id="d${pag.id}" data-id="${pag.id}"
						data-usuario="${pag.documento.usuario.nome}"
						data-id-documento="${pag.documento.id}"
						data-id-modalidade="${pag.documento.modalidade.id}">
						<td>${pag.numero}</td>
						<td>${pag.documento.descricao}</td>
						<td>${pag.pam}</td>
						<td>${pag.volume}</td>
						<td>${pag.secao}</td>
						<td>
							<form method="post" class="form-inline">
								<input type="hidden" name="acao" value="excluir" /> <input
									type="hidden" name="id" value="${pag.documento.id}" />
								<c:if test="${podeRastrear}">
									<a type="button" class="btn btn-info"
										href="rastrear?documento=${pag.documento.id}"> <span
										class="glyphicon glyphicon glyphicon-question-sign"
										aria-hidden="true"></span>
									</a>
								</c:if>
								<button type="button" class="btn btn-warning"
									onclick="editarPag('#d${pag.id}')">
									<span class="glyphicon glyphicon glyphicon-pencil"
										aria-hidden="true"></span>
								</button>
								<c:if test="${podeExcluir}">
									<button type="submit" class="btn btn-danger">
										<span class="glyphicon glyphicon glyphicon-trash"
											aria-hidden="true"></span>
									</button>
								</c:if>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file="../common/pagination.jsp"%>
	</c:otherwise>
</c:choose>
<div class="modal fade" id="modal_pag">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="modal_pag_titulo">&nbsp;</h4>
			</div>
			<form method="post">
				<input type="hidden" id="modal_pag_acao" name="acao" value="" /> <input
					type="hidden" id="modal_pag_id" name="id" value="" /> <input
					type="hidden" id="modal_pag_id_documento" name="id_documento"
					value="" />
				<div class="modal-body">
					<div class="form-group">
						<label for="modal_pag_numero">PAG</label> <input type="text"
							class="form-control" id="modal_pag_numero" name="numero_pag"
							pattern="\d{5}\.\d{6}/\d{4}-\d{2}"
							data-mask="00000.000000/0000-00" required="required" />
					</div>
					<div class="form-group">
						<label for="modal_pag_descricao">Descrição</label> <input
							type="text" class="form-control" id="modal_pag_descricao"
							name="descricao" pattern=".{1,256}" required="required" />
					</div>
					<div class="form-group">
						<label for="modal_pag_pam">PAM</label> <input type="text"
							class="form-control" id="modal_pag_pam" name="numero_pam" />
					</div>
					<div class="form-group">
						<label for="modal_pag_volume">Volume</label> <input type="text"
							class="form-control" id="modal_pag_volume" name="volume"
							pattern=".{1,16}" required="required" />
					</div>
					<div class="form-group">
						<label for="modal_pag_secao">Seção</label> <input type="text"
							class="form-control" id="modal_pag_secao" name="secao"
							pattern=".{2,16}" required="required" />
					</div>
					<div class="form-group">
						<label for="modal_pag_modalidade">Modalidade</label> <select
							class="form-control selectpicker" data-live-search="true"
							id="modal_pag_modalidade" name="id_modalidade">
							<c:forEach var="modalidade" items="${modalidades}">
								<option value="${modalidade.id}">${modalidade.tipo}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="modal_pag_usuario">Criador</label> <input type="text"
							class="form-control" id="modal_pag_usuario" name="usuario"
							readonly />
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Salvar</button>
				</div>
			</form>
		</div>
	</div>
</div>