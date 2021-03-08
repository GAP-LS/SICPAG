package com.suchorski.sicpag.models;

import java.sql.Timestamp;

public class Rastreio extends AbstratoModel {
	
	private String observacao;
	private Timestamp dataMovimentacao;
	private Usuario usuario;
	private String secao;
	private String tipo;
	
	public Rastreio(int id, String observacao, Timestamp dataMovimentacao, Usuario usuario, String secao, String tipo) {
		super(id);
		this.observacao = observacao;
		this.dataMovimentacao = dataMovimentacao;
		this.usuario = usuario;
		this.secao = secao;
		this.tipo = tipo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Timestamp getDataMovimentacao() {
		return dataMovimentacao;
	}

	public void setDataMovimentacao(Timestamp dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
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
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
