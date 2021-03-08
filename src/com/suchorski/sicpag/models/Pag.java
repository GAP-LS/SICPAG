package com.suchorski.sicpag.models;

public class Pag extends AbstratoModel {
	
	private String numero;
	private String pam;
	private String volume;
	private String secao;
	private Documento documento;
	
	public Pag(int id, String numero, String pam, String volume, String secao, Documento documento) {
		super(id);
		this.numero = numero;
		this.pam = pam;
		this.volume = volume;
		this.secao = secao;
		this.documento = documento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getPam() {
		return pam;
	}

	public void setPam(String pam) {
		this.pam = pam;
	}
	
	public String getVolume() {
		return volume;
	}
	
	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getSecao() {
		return secao;
	}

	public void setSecao(String secao) {
		this.secao = secao;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	
}
