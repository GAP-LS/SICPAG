<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<h1>Novos Usuários</h1>
<table class="table">
	<thead>
		<tr>
			<th class="col-sm-4">Usuário</th>
			<th class="col-sm-4">Data de entrada</th>
			<th class="col-sm-2">Seção</th>
			<th class="col-sm-2">&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="inativo" items="${inativos}">
			<tr>
				<td style="vertical-align: middle;">${inativo.nome}</td>
				<td style="vertical-align: middle;"><fmt:formatDate
						value="${inativo.dataCriacao}" type="both"
						pattern="dd/MM/yyyy à's' HH:mm:ss" /></td>
				<td><select class="form-control selectpicker"
					id="s${inativo.id}" data-live-search="true"
					onchange="atualizaAlocacao(${inativo.id})">
						<c:forEach var="secao" items="${secoes}">
							<option value="${secao.id}">${secao.sigla}</option>
						</c:forEach>
				</select></td>
				<td>
					<form method="post">
						<input type="hidden" name="acao" value="alocar" /> <input
							type="hidden" name="id_usuario" value="${inativo.id}" /> <input
							type="hidden" id="i${inativo.id}" name="id_secao" value="" /> <input
							type="submit" id="a${inativo.id}"
							class="btn btn-success btn-block" value="Alocar" />
					</form>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<script type="text/javascript">
	$('input[type=submit]').attr("disabled", true);
	$('select').selectpicker('val', '');
</script>