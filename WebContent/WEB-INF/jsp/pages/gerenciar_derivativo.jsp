<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Gerenciar Derivativo</h1>
<div class="row">
	<form method="get">
		<div class="col-sm-5">
			<div class="form-group">
				<input type="text" class="form-control" name="busca"
					placeholder="Busca por: PAG, PPM, Seção ou descrição" />
			</div>
		</div>
		<div class="col-sm-3">
			<input type="submit" class="btn btn-primary btn-block" value="Buscar" />
		</div>
	</form>
	<div class="col-sm-4">
		<button class="btn btn-success btn-block"
			onclick="incluirDerivativo('${usuario.nome}')">Incluir
			derivativo</button>
	</div>
</div>
<h2>Derivativos</h2>
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
					<th class="col-sm-2">Derivativo</th>
					<th class="col-sm-4">Descrição</th>
					<th class="col-sm-1">Contagem</th>
					<th class="col-sm-1">PPM</th>
					<th class="col-sm-1">Volume</th>
					<th class="col-sm-1">Seção</th>
					<th class="col-sm-2">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="derivativo" items="${derivativos}">
					<tr id="d${derivativo.id}" data-id="${derivativo.id}"
						data-usuario="${derivativo.documento.usuario.nome}"
						data-id-documento="${derivativo.documento.id}"
						data-id-modalidade="${derivativo.documento.modalidade.id}"
						data-id-tipoderivativo="${derivativo.tipoDerivativo.id}">
						<td>${derivativo.pag.numero}</td>
						<td>${derivativo.documento.descricao}</td>
						<td>${derivativo.contagem}</td>
						<td>${derivativo.ppm}</td>
						<td>${derivativo.volume}</td>
						<td>${derivativo.pag.secao}</td>
						<td>
							<form method="post" class="form-inline">
								<input type="hidden" name="acao" value="excluir" /> <input
									type="hidden" name="id" value="${derivativo.documento.id}" />
								<c:if test="${podeRastrear}">
									<a type="button" class="btn btn-info"
										href="rastrear?documento=${derivativo.documento.id}"> <span
										class="glyphicon glyphicon glyphicon-question-sign"
										aria-hidden="true"></span>
									</a>
								</c:if>
								<button type="button" class="btn btn-warning"
									onclick="editarDerivativo('#d${derivativo.id}')">
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
<div class="modal fade" id="modal_derivativo">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="modal_derivativo_titulo">&nbsp;</h4>
			</div>
			<form method="post">
				<input type="hidden" id="modal_derivativo_acao" name="acao" value="" />
				<input type="hidden" id="modal_derivativo_id" name="id" value="" />
				<input type="hidden" id="modal_derivativo_id_documento"
					name="id_documento" value="" />
				<div class="modal-body">
					<div class="form-group">
						<label for="modal_derivativo_pag">PAG</label> <select
							class="form-control selectpicker" data-live-search="true"
							id="modal_derivativo_pag" name="id_pag">
							<c:forEach var="pag" items="${pags}">
								<option value="${pag.id}">${pag.numero}(Volume:
									${pag.volume})</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="modal_derivativo_contagem">Contagem</label> <input
							type="text" class="form-control" id="modal_derivativo_contagem"
							name="contagem" pattern="\d+" required="required" />
					</div>
					<div class="form-group">
						<label for="modal_derivativo_descricao">Descrição</label> <input
							type="text" class="form-control" id="modal_derivativo_descricao"
							name="descricao" pattern=".{1,256}" required="required" />
					</div>
					<div class="form-group">
						<label for="modal_derivativo_ppm">PPM</label> <input type="text"
							class="form-control" id="modal_derivativo_ppm" name="numero_ppm" />
					</div>
					<div class="form-group">
						<label for="modal_derivativo_volume">Volume</label> <input
							type="text" class="form-control" id="modal_derivativo_volume"
							name="volume" pattern=".{1,16}" required="required" />
					</div>
					<div class="form-group">
						<label for="modal_derivativo_modalidade">Modalidade</label> <select
							class="form-control selectpicker" data-live-search="true"
							id="modal_derivativo_modalidade" name="id_modalidade">
							<c:forEach var="modalidade" items="${modalidades}">
								<option value="${modalidade.id}">${modalidade.tipo}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="modal_derivativo_tipoderivativo">Tipo de
							derivativo</label> <select class="form-control selectpicker"
							data-live-search="true" id="modal_derivativo_tipoderivativo"
							name="id_tipoderivativo">
							<c:forEach var="tipo" items="${tipos}">
								<option value="${tipo.id}">${tipo.tipo}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="modal_derivativo_usuario">Criador</label> <input
							type="text" class="form-control" id="modal_derivativo_usuario"
							name="usuario" readonly />
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Salvar</button>
				</div>
			</form>
		</div>
	</div>
</div>