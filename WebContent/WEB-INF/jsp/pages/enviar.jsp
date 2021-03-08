<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Enviar documento</h1>
<c:choose>
	<c:when test="${empty documentos_secao}">
		<div class="alert alert-info">
			<strong>${info_documentos_secao}</strong>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-sm-5">
				<div class="form-group">
					<select class="form-control selectpicker" data-live-search="true"
						id="enviar_documento">
						<c:forEach var="documento" items="${documentos_secao}">
							<option value="${documento.idDocumento}">${documento.numero}
								(Derivativo: ${documento.contagem} e Volume:
								${documento.volume})</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="col-sm-3">
				<div class="form-group">
					<select class="form-control selectpicker" data-live-search="true"
						id="enviar_secao">
						<c:forEach var="secao" items="${secoes}">
							<option value="${secao.id}">${secao.sigla}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="col-sm-4">
				<div class="form-group">
					<button class="btn btn-primary btn-block"
						onclick="enviarDocumento()">Enviar</button>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>
<h2>Documentos pendentes de envio</h2>
<c:choose>
	<c:when test="${empty pendentes}">
		<div class="alert alert-info">
			<strong>${info_pendentes}</strong>
		</div>
	</c:when>
	<c:otherwise>
		<%@ include file="../common/pagination.jsp"%>
		<table class="table">
			<thead>
				<tr>
					<th class="col-sm-3">Número</th>
					<th class="col-sm-1">Contagem</th>
					<th class="col-sm-2">Seção</th>
					<th class="col-sm-2">PAM/PPM</th>
					<th class="col-sm-1">Volume</th>
					<th class="col-sm-3">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="pendente" items="${pendentes}">
					<tr id="d${pendente.id}">
						<td>${pendente.numero}</td>
						<td>${pendente.contagem}</td>
						<td>${pendente.secao}</td>
						<td>${pendente.pamPpm}</td>
						<td>${pendente.volume}</td>
						<td>
							<form method="post" class="form-inline">
								<input type="hidden" name="acao" value="excluir" /> <input
									type="hidden" name="id" value="${pendente.id}" />
								<button type="submit" class="btn btn-danger">
									<span class="glyphicon glyphicon glyphicon-trash"
										aria-hidden="true"></span>
								</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file="../common/pagination.jsp"%>
	</c:otherwise>
</c:choose>
<div class="modal fade" id="modal_enviar">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Enviar documento</h4>
			</div>
			<form method="post">
				<input type="hidden" name="acao" value="enviar" /> <input
					type="hidden" id="modal_id_documento" name="id_documento" value="" />
				<input type="hidden" id="modal_id_secao_envio" name="id_secao_envio"
					value="" />
				<div class="modal-body">
					<div class="form-group">
						<label for="modal_enviar_observacao">Observação</label> <input
							type="text" class="form-control" id="modal_enviar_observacao"
							name="observacao" />
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Enviar</button>
				</div>
			</form>
		</div>
	</div>
</div>
