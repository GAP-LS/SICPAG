/* Onload */
$(function() {
	$('input[data-mask]').each(function (i, e) {
		$(e).mask($(e).data('mask'));
	});
});

/* Spinner functions */
function showModal(idModal, callback) {
	var id = idModal;
	$(id).on('shown.bs.modal', callback);
	$(id).modal('show');
}

function showSpinner(callback) {
	$('#spinner').fadeIn('fast', callback);
}

function hideSpinner(callback) {
	$('#spinner').fadeOut('fast', callback);
}

/* Toast functions */
function toast(message, type) {
	$.toaster(message, '', type);
}

/* Functions PAG */
function incluirPag(usuario) {
	$('#modal_pag_titulo').text('Incluir PAG');
	$('#modal_pag_acao').val('incluir');
	$('#modal_pag_id').val(0);
	$('#modal_pag_id_documento').val(0);
	$('#modal_pag_numero').val('');
	$('#modal_pag_descricao').val('');
	$('#modal_pag_pam').val('');
	$('#modal_pag_volume').val('');
	$('#modal_pag_secao').val('');
	$('#modal_pag_modalidade').selectpicker('val', $('#modal_pag_modalidade option:first').val());
	$('#modal_pag_usuario').val(usuario);
	showModal('#modal_pag', function(e) {
		$('#modal_pag_numero').focus();
	});
}

function editarPag(id) {
	var l = $(id);
	$('#modal_pag_titulo').text('Editar PAG');
	$('#modal_pag_acao').val('editar');
	$('#modal_pag_id').val(l.data('id'));
	$('#modal_pag_id_documento').val(l.data('id-documento'));
	$('#modal_pag_numero').val(l.find('td:nth-child(1)').text());
	$('#modal_pag_descricao').val(l.find('td:nth-child(2)').text());
	$('#modal_pag_pam').val(l.find('td:nth-child(3)').text());
	$('#modal_pag_volume').val(l.find('td:nth-child(4)').text());
	$('#modal_pag_secao').val(l.find('td:nth-child(5)').text());
	$('#modal_pag_modalidade').selectpicker('val', l.data('id-modalidade'));
	$('#modal_pag_usuario').val(l.data('usuario'));
	showModal('#modal_pag', function(e) {
		$('#modal_pag_numero').focus();
	});
}

/* Functions derivativo */
function incluirDerivativo(usuario) {
	$('#modal_derivativo_titulo').text('Incluir derivativo');
	$('#modal_derivativo_acao').val('incluir');
	$('#modal_derivativo_id').val(0);
	$('#modal_derivativo_id_documento').val(0);
	$('#modal_derivativo_pag').selectpicker('val', $('#modal_derivativo_pag option:first').val());
	$('#modal_derivativo_contagem').val('');
	$('#modal_derivativo_descricao').val('');
	$('#modal_derivativo_ppm').val('');
	$('#modal_derivativo_volume').val('');
	$('#modal_derivativo_modalidade').selectpicker('val', $('#modal_derivativo_modalidade option:first').val());
	$('#modal_derivativo_tipoderivativo').selectpicker('val', $('#modal_derivativo_tipoderivativo option:contains("PPM")').val());
	$('#modal_derivativo_usuario').val(usuario);
	showModal('#modal_derivativo', function(e) {
		$('#modal_derivativo_contagem').focus();
	});
}

function editarDerivativo(id) {
	var l = $(id);
	$('#modal_derivativo_titulo').text('Editar derivativo');
	$('#modal_derivativo_acao').val('editar');
	$('#modal_derivativo_id').val(l.data('id'));
	$('#modal_derivativo_id_documento').val(l.data('id-documento'));
	$('#modal_derivativo_pag').selectpicker('val', l.data('id-pag'));
	$('#modal_derivativo_contagem').val(l.find('td:nth-child(3)').text());
	$('#modal_derivativo_descricao').val(l.find('td:nth-child(2)').text());
	$('#modal_derivativo_ppm').val(l.find('td:nth-child(4)').text());
	$('#modal_derivativo_volume').val(l.find('td:nth-child(5)').text());
	$('#modal_derivativo_modalidade').selectpicker('val', l.data('id-modalidade'));
	$('#modal_derivativo_tipoderivativo').selectpicker('val', l.data('id-tipoderivativo'));
	$('#modal_derivativo_usuario').val(l.data('usuario'));
	showModal('#modal_derivativo', function(e) {
		$('#modal_derivativo_contagem').focus();
	});
}

/* Functions Secao */
function incluirSecao() {
	$('#modal_secao_titulo').text('Incluir seção');
	$('#modal_secao_acao').val('incluir');
	$('#modal_secao_id').val('0');
	$('#modal_secao_sigla').val('');
	$('#modal_secao_pai').selectpicker('val', $('#modal_secao_pai option:first').val());
	$('#modal_secao_tipo').selectpicker('val', $('#modal_secao_tipo option:first').val());
	showModal('#modal_secao', function(e) {
		$('#modal_secao_sigla').focus();
	});
}

function editarSecao() {
	var secao = $('#secao option:selected');
	$('#modal_secao_titulo').text('Editar seção');
	$('#modal_secao_acao').val('editar');
	$('#modal_secao_id').val(secao.val());
	$('#modal_secao_sigla').val(secao.text());
	$('#modal_secao_pai').selectpicker('val', secao.data('id-pai'));
	$('#modal_secao_tipo').selectpicker('val', secao.data('id-tipo'));
	showModal('#modal_secao', function(e) {
		$('#secao_sigla').focus();
	});
}

/* Functions rastreio */
function enviarDocumento() {
	$('#modal_id_documento').val($('#enviar_documento').val());
	$('#modal_id_secao_envio').val($('#enviar_secao').val());
	showModal('#modal_enviar', function(e) {
		$('#modal_enviar_observacao').focus();
	});
}

function receberDocumento(id) {
	var l = $(id);
	$('#modal_receber_id_documento').val(l.data('id-documento'));
	$('#modal_receber_numero').val(l.find('td:nth-child(1)').text());
	$('#modal_receber_usuario').val(l.data('usuario'));
	$('#modal_receber_senha').val('');
	showModal('#modal_receber', function(e) {
		$('#modal_receber_senha').focus();
	});
}

/* Users functions */
function mudarUsuario(f) {
	showSpinner(function() {
		f.form.submit();
	});
}

function atualizaAlocacao(id) {
	$('#i' + id).val($('#s' + id).val());
	$('#a' + id).attr('disabled', false);
}