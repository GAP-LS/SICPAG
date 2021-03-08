package com.suchorski.sicpag.models;

import java.sql.Timestamp;

public class SenhaTemporaria extends AbstratoModel {
	
	private String senha;
	private Timestamp criacao;

	public SenhaTemporaria(int id, String senha, Timestamp criacao) {
		super(id);
		this.senha = senha;
		this.criacao = criacao;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Timestamp getCriacao() {
		return criacao;
	}
	
	public void setCriacao(Timestamp criacao) {
		this.criacao = criacao;
	}
	
}
