<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Gerenciar usuários</h1>
<div class="row">
	<div class="col-sm-5">
		<div class="form-group">
			<form method="get">
				<label for="usuario">Usuário</label> <select
					class="form-control selectpicker" data-live-search="true"
					id="usuario" name="id_usuario" onchange="mudarUsuario(this)">
					<c:forEach var="u" items="${usuarios}">
						<option value="${u.id}" ${usuario.id eq u.id ? 'selected' : ''}>${u.nome}</option>
					</c:forEach>
				</select>
			</form>
		</div>
	</div>
	<form method="post">
		<input type="hidden" name="acao" value="definir_secao" /> <input
			type="hidden" name="id_usuario" value="${usuario.id}" />
		<div class="col-sm-3">
			<div class="form-group">
				<label for="secao">Seção</label> <select
					class="form-control selectpicker" data-live-search="true"
					id="secao" name="id_secao">
					<c:forEach var="secao" items="${secoes}">
						<option value="${secao.id}"
							${usuario.secao.id eq secao.id ? 'selected' : ''}>${secao.sigla}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="col-sm-4">
			<div class="form-group">
				<label>&nbsp;</label> <input type="submit"
					class="btn btn-primary btn-block" value="Definir seção" />
			</div>
		</div>
	</form>
</div>
<h2>Perfis do usuário ${usuario.nome}</h2>
<form method="post">
	<input type="hidden" name="acao" value="definir_perfis" /> <input
		type="hidden" name="id_usuario" value="${usuario.id}" />
	<table class="table">
		<thead>
			<tr>
				<th class="col-sm-2">Perfil</th>
				<th class="col-sm-6">Descrição</th>
				<th>Ativação</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="p" items="${perfis}">
				<tr>
					<td>${p.perfil}</td>
					<td>${p.nome}</td>
					<td>
						<div class="btn-group" data-toggle="buttons">
							<c:choose>
								<c:when test="${usuario.hasPerfil(p.perfil)}">
									<label class="btn btn-primary btn-xs bs-cb active"> <input
										type="checkbox" name="perfis" value="${p.id}"
										autocomplete="off" checked> <span
										class="glyphicon glyphicon-ok"></span>
									</label>
								</c:when>
								<c:otherwise>
									<label class="btn btn-primary btn-xs bs-cb"> <input
										type="checkbox" name="perfis" value="${p.id}"
										autocomplete="off"> <span
										class="glyphicon glyphicon-ok"></span>
									</label>
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<input type="submit" class="btn btn-success btn-block"
		value="Salvar perfis" />
</form>