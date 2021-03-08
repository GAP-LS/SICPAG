package com.suchorski.sicpag.models;

public class TipoDerivativo extends AbstratoModel {

	private String tipo;
	
	public TipoDerivativo(int id, String tipo) {
		super(id);
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
