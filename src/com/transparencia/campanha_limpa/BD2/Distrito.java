package com.transparencia.campanha_limpa.BD2;

public class Distrito {
	private Integer ID_distrito;
	private String nome;

	public Distrito (){
		this.ID_distrito=null;
		this.nome=null;
	}
	
	public void setIddistrito (Integer id_distrito ){
		this.ID_distrito=id_distrito;
	}
	public void setnome(String nome){
		this.nome=nome;
	}

	public Integer getIddistrito(){
		return this.ID_distrito;
	}
	public String getnome(){
		return this.nome;
	}
}

