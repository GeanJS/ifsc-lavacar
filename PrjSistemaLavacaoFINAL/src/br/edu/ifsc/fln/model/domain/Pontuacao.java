/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author Geanj
 */
public class Pontuacao {

    private int quantidade;
    private Cliente cliente;

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public int adicionar(int quantidade) {
        return this.quantidade += quantidade;
    }
    
    public int subtrair(int quantidade) {
        return this.quantidade -= quantidade;
    }
    
    public int saldo() {
        return quantidade;
    }
    
}
