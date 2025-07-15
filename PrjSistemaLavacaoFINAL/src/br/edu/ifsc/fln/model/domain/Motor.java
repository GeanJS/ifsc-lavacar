/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author Geanj
 */
public class Motor {
    private double potencia;
    private ETipoCombustivel combustivel;

    public Motor(){
        
    }
    public Motor(double potencia) {
        this.potencia = potencia;
    }

    public double getPotencia() {
        return potencia;
    }

    public void setPotencia(double potencia) {
        this.potencia = potencia;
    }

    public ETipoCombustivel getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(ETipoCombustivel combustivel) {
        this.combustivel = combustivel;
    }

    @Override
    public String toString() {
        return "Motor{" + "combustivel=" + combustivel + '}';
    }

    
}
