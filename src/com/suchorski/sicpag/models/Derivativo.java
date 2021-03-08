package com.suchorski.sicpag.models;

public class Derivativo extends AbstratoModel {
	
	private int contagem;
	private String ppm;
	private String volume;
	private Pag pag;
	private Documento documento;
	private TipoDerivativo tipoDerivativo;
	
	public Derivativo(int id, int contagem, String ppm, String volume, Pag pag, Documento documento, TipoDerivativo tipoDerivativo) {
		super(id);
		this.contagem = contagem;
		this.ppm = ppm;
		this.volume = volume;
		this.pag = pag;
		this.documento = documento;
		this.tipoDerivativo = tipoDerivativo;
	}

	public int getContagem() {
		return contagem;
	}

	public void setContagem(int contagem) {
		this.contagem = contagem;
	}

	public String getPpm() {
		return ppm;
	}

	public void setPpm(String ppm) {
		this.ppm = ppm;
	}
	
	public String getVolume() {
		return volume;
	}
	
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	public Pag getPag() {
		return pag;
	}
	
	public void setPag(Pag pag) {
		this.pag = pag;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	
	public TipoDerivativo getTipoDerivativo() {
		return tipoDerivativo;
	}
	
	public void setTipoDerivativo(TipoDerivativo tipoDerivativo) {
		this.tipoDerivativo = tipoDerivativo;
	}
	
}
