<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<nav class="navbar navbar-inverse" role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#navBar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="./">SICPAG</a>
		</div>
		<div class="collapse navbar-collapse" id="navBar">
			<ul class="nav navbar-nav">
				<c:if test="${sessionScope.logado}">
					<li role="presentation" class="dropdown"><a
						class="dropdown-toggle" data-toggle="dropdown" href="#"
						role="button" aria-haspopup="true" aria-expanded="false"> PAGs
							<span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a href="./rastrear">Rastrear</a></li>
							<li class="divider" />
							<li><a href="./enviar">Enviar</a></li>
							<li><a href="./receber">Receber</a></li>
						</ul></li>
					<li role="presentation" class="dropdown"><a
						class="dropdown-toggle" data-toggle="dropdown" href="#"
						role="button" aria-haspopup="true" aria-expanded="false">
							Gerenciamento <span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a href="./gerenciar_pag">Gerenciar PAG</a></li>
							<li><a href="./gerenciar_derivativo">Gerenciar
									derivativo</a></li>
							<!--li class="divider" /> 
							<li><a href="./senhas_temporarias">Senhas temporárias</a></li-->
						</ul></li>
					<li role="presentation" class="dropdown"><a
						class="dropdown-toggle" data-toggle="dropdown" href="#"
						role="button" aria-haspopup="true" aria-expanded="false">
							Administração <span class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<li><a href="./gerenciar_secoes">Gerenciar seções</a></li>
							<li><a href="./gerenciar_usuarios">Gerenciar usuários</a></li>
							<li class="divider" />
							<li><a href="./novos_usuarios">Novos usuários</a></li>
						</ul></li>
				</c:if>
				<li role="presentation" class="dropdown"><a
					class="dropdown-toggle" data-toggle="dropdown" href="#"
					role="button" aria-haspopup="true" aria-expanded="false"> Ajuda
						<span class="caret"></span>
				</a>
					<ul class="dropdown-menu">
						<li><a href="./informacoes">Informações do sistema</a></li>
						<li class="divider" />
						<li><a href="./ajuda">Ajuda do sistema</a></li>
					</ul></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><c:choose>
						<c:when test="${sessionScope.logado}">
							<li><a href="./logout"><span
									class="glyphicon glyphicon-log-out"></span>
									${sessionScope.usuario.nome}</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="./login"><span
									class="glyphicon glyphicon-log-in"></span> Entrar</a></li>
						</c:otherwise>
					</c:choose></li>
			</ul>
		</div>
	</div>
</nav>