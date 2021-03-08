package com.suchorski.sicpag.exceptions;

public class DBException extends Exception {

	private static final long serialVersionUID = -5280598825243824968L;

	public DBException(String message) {
		super(parse(message));
	}

	private static String parse(String message) {
		if (message.matches(".+'UQ_NUMERO_VOLUME'"))
			return "Esse número de PAG já está incluído no sistema";
		if (message.matches(".+'UQ_PAM_VOLUME'"))
			return "Esse número de PAM já está incluído no sistema";
		if (message.matches(".+'UQ_DERIVATIVO'"))
			return "Esse número de derivativo já está incluído no sistema";
		if (message.matches(".+'UQ_PPM'"))
			return "Esse número de PPM já está incluído no sistema";
		if (message.matches(".+'UQ_SIGLA'"))
			return "Sigla de seção já existente";
		if (message.matches(".+CONSTRAINT\\s+`FK_DERIVATIVO_PAG`.+"))
			return "Esse PAG tem derivativos e não pode ser removido";
		if (message.matches(".+CONSTRAINT\\s+`FK_RASTREIO_SECAO`.+"))
			return "Essa seção tem documentos pendentes e não pode ser removida";
		if (message.matches(".+CONSTRAINT\\s+`FK_USUARIO_SECAO`.+"))
			return "Essa seção tem usuários ativos e não pode ser removida";
		if (message.matches(".+CONSTRAINT\\s+`FK_SECAO_PAI`.+"))
			return "Essa seção tem subseções e não pode ser removida";
		if (message.matches("Data\\s+truncation.+'NUMERO'.+"))
			return "Número de PAG incorreto";
		if (message.matches("Data\\s+truncation.+'PAM'.+"))
			return "Número de PAM incorreto";
		if (message.matches("Data\\s+truncation.+'PPM'.+"))
			return "Número de PPM incorreto";
		if (message.matches("Data\\s+truncation.+'DESCRICAO'.+"))
			return "Descrição muito longa";
		if (message.matches("Data\\s+truncation.+'SECAO'.+"))
			return "Sigla de seção muito longa";
		if (message.matches("Data\\s+truncation.+'OBSERVACAO'.+"))
			return "Observação muito longa";
		return "Exceção: " + message;
	}

}
