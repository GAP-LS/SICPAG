package com.suchorski.sicpag.models;

public class Secao extends AbstratoModel {
	
	private String sigla;
	private Secao pai;
	private TipoSecao tipo;

	public Secao(int id, String sigla, Secao pai, TipoSecao tipo) {
		super(id);
		this.sigla = sigla;
		this.pai = pai;
		this.tipo = tipo;
	}
	
	public Secao(int id, String sigla, int pai, int tipo) {
		this(id, sigla, new Secao(pai, "", null, null), new TipoSecao(tipo, ""));
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public Secao getPai() {
		return pai;
	}
	
	public void setPai(Secao pai) {
		this.pai = pai;
	}
	
	public TipoSecao getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoSecao tipo) {
		this.tipo = tipo;
	}
	
}
