package com.suchorski.sicpag.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Usuario extends AbstratoModel {
	
	private String login;
	private String nome;
	private Secao secao;
	private Timestamp dataCriacao;
	List<Perfil> perfis = new ArrayList<Perfil>();
	
	public Usuario(int id, String login, String nome, Secao secao, Timestamp dataCriacao, List<Perfil> list) {
		this(id, login, nome, secao, dataCriacao);
		this.perfis = list;
	}
	
	public Usuario(int id, String login, String nome, Secao secao, Timestamp dataCriacao) {
		super(id);
		this.login = login;
		this.nome = nome;
		this.secao = secao;
		this.dataCriacao = dataCriacao;
	}
	
	public Usuario(int id, String login, String nome) {
		this(id, login, nome, null, null);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Secao getSecao() {
		return secao;
	}
	
	public void setSecao(Secao secao) {
		this.secao = secao;
	}
	
	public Timestamp getDataCriacao() {
		return dataCriacao;
	}
	
	public void setDataCriacao(Timestamp dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public List<Perfil> getPerfis() {
		return perfis;
	}
	
	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}
	
	public boolean hasAcesso(String... perfis) {
		ArrayList<String> ps = new ArrayList<String>(Arrays.asList(perfis));
		if (this.perfis.stream().anyMatch(p -> p.getPerfil().equals("UD")))
			return true;
		return ps.stream().anyMatch(l -> this.perfis.stream().anyMatch(p -> p.getPerfil().equals(l)));
	}
	
	public boolean isBlocked() {
		return this.perfis.stream().anyMatch(p -> p.getPerfil().equals("UB"));
	}
	
	public boolean hasPerfil(String perfil) {
		return perfis.stream().anyMatch(p -> p.getPerfil().equals(perfil));
	}
	
	@Override
	public String toString() {
		return nome + " (" + login + ")";
	}
	
}
