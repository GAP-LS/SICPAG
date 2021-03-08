package com.suchorski.sicpag.models;

public class Perfil extends AbstratoModel {
	
	private String perfil;
	private String nome;
	
	public Perfil(int id, String perfil, String nome) {
		super(id);
		this.perfil = perfil;
		this.nome = nome;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
