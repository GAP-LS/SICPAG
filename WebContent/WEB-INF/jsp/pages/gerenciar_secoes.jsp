<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Gerenciar seções</h1>
<div class="row">
	<form method="get">
		<div class="col-sm-4">
			<div class="form-group">
				<select class="form-control selectpicker" data-live-search="true"
					id="secao" name="secao">
					<c:forEach var="secao" items="${secoes}">
						<option value="${secao.id}" data-id-pai="${secao.pai.id}"
							data-id-tipo="${secao.tipo.id}"
							${secaoSelecionada.id == secao.id ? 'selected' : ''}>${secao.sigla}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="col-sm-2">
			<input type="submit" class="btn btn-primary btn-block"
				value="Listar subseçoes" />
		</div>
	</form>
	<div class="col-sm-2">
		<button class="btn btn-warning btn-block" onclick="editarSecao()">Editar
			seção</button>
	</div>
	<div class="col-sm-4">
		<button class="btn btn-success btn-block" onclick="incluirSecao()">Incluir
			nova seção</button>
	</div>
</div>
<h2>Subseções de ${secaoSelecionada.sigla}</h2>
<c:choose>
	<c:when test="${empty secoesFilhas}">
		<div class="alert alert-info">
			<strong>Essa seção não possui subseções</strong>
		</div>
	</c:when>
	<c:otherwise>
		<table class="table">
			<thead>
				<tr>
					<th class="col-sm-5">Seção</th>
					<th class="col-sm-5">Seção pai</th>
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="secao" items="${secoesFilhas}">
					<tr>
						<td>${secao.sigla}(${secao.tipo.tipo})</td>
						<td>${secao.pai.sigla}(${secao.pai.tipo.tipo})</td>
						<td>
							<form method="post">
								<input type="hidden" name="acao" value="excluir" /> <input
									type="hidden" name="id" value="${secao.id}" />
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
	</c:otherwise>
</c:choose>
<div class="modal fade" id="modal_secao">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="modal_secao_titulo">&nbsp;</h4>
			</div>
			<form method="post">
				<input type="hidden" id="modal_secao_acao" name="acao" value="" />
				<input type="hidden" id="modal_secao_id" name="id" value="" />
				<div class="modal-body">
					<div class="form-group">
						<label for="modal_secao_sigla">Seção</label> <input type="text"
							class="form-control" id="modal_secao_sigla" name="sigla"
							required="required" />
					</div>
					<div class="form-group">
						<label for="modal_secao_pai">Seção pai</label> <select
							class="form-control selectpicker" data-live-search="true"
							id="modal_secao_pai" name="id_pai">
							<option value="0">Nenhuma</option>
							<c:forEach var="secao" items="${secoes}">
								<option value="${secao.id}">${secao.sigla}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="modal_secao_tipo">Tipo da seção</label> <select
							class="form-control selectpicker" data-live-search="true"
							id="modal_secao_tipo" name="id_tipo">
							<c:forEach var="tipo" items="${tipos}">
								<option value="${tipo.id}">${tipo.tipo}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Salvar</button>
				</div>
			</form>
		</div>
	</div>
</div>