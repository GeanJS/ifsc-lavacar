package br.edu.ifsc.fln.model.domain;

import java.io.Serializable;

public class Modelo implements Serializable {

    private int id;
    private String descricao;
    
    private Marca marca;
    private Motor motor;
    private ECategoria categoria;

    public Modelo(int id, String descricao, Marca marca) {
        this();
        this.id = id;
        this.descricao = descricao;
        this.marca = marca;
        
    }

    public Modelo() {
        createMotor();
    }

    public Modelo(int id,String descricao) {
        this();
        this.id = id;
        this.descricao = descricao;
    }
    
    private void createMotor() {
        this.motor = new Motor();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Motor getMotor() {
        return motor;
    }


    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }
    

    @Override
    public String toString() {
        return descricao;
    }
    
    
}
