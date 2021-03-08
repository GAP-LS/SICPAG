<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="panel-group" id="ajudas" role="tablist"
	aria-multiselectable="true">
	<h1>Ajuda aos usuários</h1>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h1">
			<h3 class="panel-title">
				<a role="button" data-toggle="collapse" data-parent="#ajudas"
					href="#c1" aria-expanded="true" aria-controls="c1"> Primeiro
					acesso </a>
			</h3>
		</div>
		<div id="c1" class="panel-collapse collapse in" role="tabpanel"
			aria-labelledby="h1">
			<div class="panel-body text-justify">Para o primeiro acesso ao
				sistema, o usuário deverá entrar com dados do Acesso Único do CCA-RJ
				(Portal do militar, zimbra, SILOMS) e solicitar a um administrador
				do sistema sua alocação na seção, perfil UN (Usuário normal) e
				demais perfis necessários.</div>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h2">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse"
					data-parent="#ajudas" href="#c2" aria-expanded="false"
					aria-controls="c2"> Rastreamento de documentos </a>
			</h3>
		</div>
		<div id="c2" class="panel-collapse collapse" role="tabpanel"
			aria-labelledby="h2">
			<div class="panel-body text-justify">
				<pre class="alert alert-warning">Para rastrear os documentos é necessário ter o perfil CR.</pre>
				O rastreio é feito através da opção "Rastrear" dentro do menu
				"PAGs". Para efetuar o rastreio basta digitar o número do PAG, PAM
				ou PPM e clicar em "Rastrear Documento".
			</div>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h3">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse"
					data-parent="#ajudas" href="#c3" aria-expanded="false"
					aria-controls="c3"> Receber documentos </a>
			</h3>
		</div>
		<div id="c3" class="panel-collapse collapse" role="tabpanel"
			aria-labelledby="h3">
			<div class="panel-body text-justify">O recebimento é feito
				através da opção "Receber" dentro do menu "PAGs". Para receber
				documentos basta o usuário estar alocado na seção de destino do
				documento. O recebedor seleciona o documento a ser recebido.</div>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h4">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse"
					data-parent="#ajudas" href="#c4" aria-expanded="false"
					aria-controls="c4"> Enviar documentos </a>
			</h3>
		</div>
		<div id="c4" class="panel-collapse collapse" role="tabpanel"
			aria-labelledby="h4">
			<div class="panel-body text-justify">
				<pre class="alert alert-warning">Para enviar documentos é necessário ter o perfil AT.</pre>
				O envio de documentos é feito através da opção "Receber" dentro do
				menu "PAGs". Para enviar um documento, basta simplesmente selecionar
				o documento, a seção de destino e clicar no botão enviar. A
				observação é um texto que será visível ao usuário na hora do
				recebimento.
			</div>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h5">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse"
					data-parent="#ajudas" href="#c5" aria-expanded="false"
					aria-controls="c5"> Gerenciar documentos </a>
			</h3>
		</div>
		<div id="c5" class="panel-collapse collapse" role="tabpanel"
			aria-labelledby="h5">
			<div class="panel-body text-justify">
				<pre class="alert alert-warning">Para gerenciar documentos é necessário ter o perfil AG ou AE.</pre>
				O gerenciamento de documentos sé feito através da opção "Gerenciar
				PAG" ou "Gerenciar Derivativo" ambos dentro do menu "Gerenciamento".
				Gerenciar um documento é o ato de incluir, editar ou excluir um PAG
				ou um Derivativo do sistema. A tela mostra os documentos existentes
				para edição ou exclusão, e para adicionar um novo, basta clicar no
				botão "Incluir PAG" ou "Incluir Derivativo".
			</div>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h6">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse"
					data-parent="#ajudas" href="#c6" aria-expanded="false"
					aria-controls="c6"> Senhas temporárias </a>
			</h3>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h7">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse"
					data-parent="#ajudas" href="#c7" aria-expanded="false"
					aria-controls="c7"> Gerenciamento de seções </a>
			</h3>
		</div>
		<div id="c7" class="panel-collapse collapse" role="tabpanel"
			aria-labelledby="h7">
			<div class="panel-body text-justify">
				<pre class="alert alert-warning">Para gerenciar seções é necessário ter o perfil UM.</pre>
				O gerenciamento de seções é feito através da opção "Gerenciar
				seções" dentro do menu "Administração". O gerenciamento de seções
				visa cadastrar as seções onde os usuários serão alocados e onde os
				documentos serão tramitados.
			</div>
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h8">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse"
					data-parent="#ajudas" href="#c8" aria-expanded="false"
					aria-controls="c8"> Gerenciamento de usuários </a>
			</h3>
		</div>
		<div id="c8" class="panel-collapse collapse" role="tabpanel"
			aria-labelledby="h8">
			<div class="panel-body text-justify">
				<pre class="alert alert-warning">Para gerenciar usuários é necessário ter o perfil UM ou UA.</pre>
				O gerenciamento de usuários é feito através da opção "Gerenciar
				usuários" dentro do menu "Administração". O gerenciamento de
				usuários visa adicionar ou remover os perfis necessários a um
				determinado usuário.
			</div>
		</div>
	</div>
	<h1>Sobre o sistema</h1>
	<div class="panel panel-primary">
		<div class="panel-heading" role="tab" id="h9">
			<h3 class="panel-title">
				<a class="collapsed" role="button" data-toggle="collapse"
					data-parent="#ajudas" href="#c9" aria-expanded="false"
					aria-controls="c9"> Perfis de Acesso </a>
			</h3>
		</div>
		<div id="c9" class="panel-collapse collapse" role="tabpanel"
			aria-labelledby="h9">
			<div class="panel-body text-justify">
				<table class="table">
					<thead>
						<tr>
							<th>Perfil</th>
							<th>Título</th>
							<th>Descrição</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>UD</td>
							<td>Desenvolvedor do sistema</td>
							<td>Tem acesso a todos os módulos</td>
						</tr>
						<tr>
							<td>UA</td>
							<td>Administrador do sistema</td>
							<td>Tem acesso para administrar seções e usuários</td>
						</tr>
						<tr>
							<td>UM</td>
							<td>Moderador do sistema</td>
							<td>Tem acesso para administrar as seções</td>
						</tr>
						<tr>
							<td>UN</td>
							<td>Usuário do sistema</td>
							<td>Liberação para acesso ao sistema</td>
						</tr>
						<tr>
							<td>UB</td>
							<td>Usuário bloqueado</td>
							<td>Bloqueia o usuário independentemente dos outros perfis
								de acesso</td>
						</tr>
						<tr>
							<td>AG</td>
							<td>Gerenciador de documentos</td>
							<td>Tem acesso para adicionar, editar ou excluir os
								documentos</td>
						</tr>
						<tr>
							<td>AE</td>
							<td>Editor de documentos</td>
							<td>Tem acesso para adicionar ou editar os documentos</td>
						</tr>
						<tr>
							<td>AT</td>
							<td>Transferidor de documentos</td>
							<td>Tem acesso para transferir documentos da seção para
								outra</td>
						</tr>
						<tr>
							<td>CA</td>
							<td>Consultor de relatórios</td>
							<td>Tem acesso a tela de geração de relatórios</td>
						</tr>
						<tr>
							<td>CR</td>
							<td>Consultor de rastreios</td>
							<td>Tem acesso a consulta de rastreios de documentos</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>