package com.suchorski.sicpag.models;

public class Pendente extends AbstratoModel {
	
	private int idDocumento;
	private String numero;
	private int contagem;
	private String pamPpm;
	private int volume;
	private String tipo;
	private String observacao;
	private Usuario usuario;
	private String secao;
	
	public Pendente(int id, int idDocumento, String numero, int contagem, String pamPpm, int volume, String tipo, String observacao,
			Usuario usuario, String secao) {
		super(id);
		this.idDocumento = idDocumento;
		this.numero = numero;
		this.contagem = contagem;
		this.pamPpm = pamPpm;
		this.volume = volume;
		this.tipo = tipo;
		this.observacao = observacao;
		this.usuario = usuario;
		this.secao = secao;
	}

	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public int getContagem() {
		return contagem;
	}

	public void setContagem(int contagem) {
		this.contagem = contagem;
	}

	public String getPamPpm() {
		return pamPpm;
	}

	public void setPamPpm(String pamPpm) {
		this.pamPpm = pamPpm;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getSecao() {
		return secao;
	}

	public void setSecao(String secao) {
		this.secao = secao;
	}
	
}
