<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<h1>Rastrear PAG</h1>
<div class="row">
	<form method="get">
		<div class="col-sm-8">
			<div class="form-group">
				<input type="text" class="form-control" name="busca"
					value="${busca}"
					placeholder="Rastreia documentos pelo PAG-VOLUME, DERIVATIVO-CONTAGEM-VOLUME, PAM ou PPM" />
			</div>
		</div>
		<div class="col-sm-4">
			<input type="submit" class="btn btn-primary btn-block"
				value="Rastrear Documento" />
		</div>
	</form>
</div>
<c:if test="${not empty rastreio}">
	<h2>Rastreio</h2>
	<table class="table">
		<thead>
			<tr>
				<th class="col-sm-2">Operação</th>
				<th class="col-sm-2">Usuário</th>
				<th class="col-sm-2">Seção</th>
				<th class="col-sm-4">Observação</th>
				<th class="col-sm-2">Data</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="r" items="${rastreio}">
				<tr>
					<td>${r.tipo}</td>
					<td>${r.usuario.nome}</td>
					<td>${r.secao}</td>
					<td>${r.observacao}</td>
					<td><fmt:formatDate value="${r.dataMovimentacao}" type="both"
							pattern="dd/MM/yyyy à's' HH:mm:ss" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:if>