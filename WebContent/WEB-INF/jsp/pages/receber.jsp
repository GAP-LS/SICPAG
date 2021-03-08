<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Receber documento</h1>
<c:choose>
	<c:when test="${not empty info}">
		<div class="alert alert-info">
			<strong>${info}</strong>
		</div>
	</c:when>
	<c:otherwise>
		<table class="table">
			<thead>
				<tr>
					<th class="col-sm-3">Número</th>
					<th class="col-sm-1">Contagem</th>
					<th class="col-sm-1">Seção</th>
					<th class="col-sm-1">PAM/PPM</th>
					<th class="col-sm-1">Volume</th>
					<th class="col-sm-4">Observação</th>
					<th class="col-sm-1">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="pendente" items="${pendentes}">
					<tr id="d${pendente.id}"
						data-id-documento="${pendente.idDocumento}"
						data-usuario="${pendente.usuario.nome}">
						<td>${pendente.numero}</td>
						<td>${pendente.contagem}</td>
						<td>${pendente.secao}</td>
						<td>${pendente.pamPpm}</td>
						<td>${pendente.volume}</td>
						<td>${pendente.observacao}</td>
						<td>
							<button type="button" class="btn btn-success"
								onclick="receberDocumento('#d${pendente.id}')">
								<span class="glyphicon glyphicon-check" aria-hidden="true"></span>
							</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>
<div class="modal fade" id="modal_receber">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Receber documento</h4>
			</div>
			<form method="post">
				<input type="hidden" name="acao" value="receber" /> <input
					type="hidden" id="modal_receber_id_documento" name="id_documento"
					value="" />
				<div class="modal-body">
					<div class="form-group">
						<label for="modal_receber_numero">PAG</label> <input type="text"
							class="form-control" id="modal_receber_numero" value="" readonly />
					</div>
					<div class="form-group">
						<label for="modal_receber_usuario">Usuário</label> <input
							type="text" class="form-control" id="modal_receber_usuario"
							value="usuario" readonly />
					</div>
					<!--
					<div class="form-group">
						<label for="modal_receber_senha">Senha temporária</label>
						<input type="text" class="form-control" id="modal_receber_senha" name="senha_temporaria" />
					</div>
					-->
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Receber</button>
				</div>
			</form>
		</div>
	</div>
</div>
