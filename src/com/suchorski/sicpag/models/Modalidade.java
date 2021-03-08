package com.suchorski.sicpag.models;

public class Modalidade extends AbstratoModel {
	
	private String tipo;

	public Modalidade(int id, String tipo) {
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
