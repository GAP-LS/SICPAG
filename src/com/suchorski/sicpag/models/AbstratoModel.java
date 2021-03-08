package com.suchorski.sicpag.models;

public abstract class AbstratoModel {
	
	private int id;
	
	public AbstratoModel(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

}
