package com.suchorski.sicpag.models;

public class Documento extends AbstratoModel {
	
	private String descricao;
	private Modalidade modalidade;
	private Usuario usuario;
	
	public Documento(int id, String descricao, Modalidade modalidade, Usuario usuario) {
		super(id);
		this.descricao = descricao;
		this.modalidade = modalidade;
		this.usuario = usuario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
