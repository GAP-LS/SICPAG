<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<h1>Senhas Temporárias</h1>
<div class="alert alert-info">
	<strong>Sistema de senhas temporárias desabilitado</strong>
</div>
<!--
<div class="row">
	<form method="post">
		<div class="col-sm-10 col-sm-offset-1">
			<div class="form-group">
				<input type="hidden" name="acao" value="incluir" />
				<input type="submit" class="btn btn-primary btn-block" value="Gerar senha temporária" />
			</div>
		</div>
	</form>
</div>
-->
<!--
<c:choose>
	<c:when test="${error}">
		<div class="alert alert-info"><strong>${message}</strong></div>
	</c:when>
	<c:otherwise>
		<table class="table">
			<thead>
				<tr>
					<th class="col-sm-5">Senha</th>
					<th class="col-sm-4">Data de criação</th>
					<th class="col-sm-3">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="senha" items="${senhas}">
					<tr>
						<td>${senha.senha}</td>
						<td><fmt:formatDate value="${senha.criacao}" type="both" pattern="dd/MM/yyyy à's' HH:mm:ss" /></td>
						<td>
							<form method="post" class="form-inline">
								<input type="hidden" name="acao" value="excluir" />
								<input type="hidden" name="id" value="${senha.id}" />
								<button type="submit" class="btn btn-danger">
									<span class="glyphicon glyphicon glyphicon-trash" aria-hidden="true"></span>
								</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>
-->