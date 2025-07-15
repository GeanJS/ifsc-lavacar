package br.edu.ifsc.fln.model.domain;

import java.io.Serializable;

public class Veiculo implements Serializable {

    private int id;
    private String placa;
    private String observacoes;

    private Cor cor;
    private Modelo modelo;
    private Cliente Cliente;

    public Cliente getCliente() {
        return Cliente;
    }

    public void setCliente(Cliente Cliente) {
        this.Cliente = Cliente;
    }

    public Veiculo(int id, String placa, Modelo modelo) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;

    }

    public Veiculo() {
    }

    public Veiculo(int id,String placa) {
        this.id = id;
        this.placa = placa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    /**
     *
     * @return
     */
    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    
    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return ""  + modelo + ' ';
    }

    
}
