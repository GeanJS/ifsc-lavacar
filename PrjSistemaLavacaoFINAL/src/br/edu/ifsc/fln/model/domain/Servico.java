package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author Geanj
 */
public class Servico {
    
    private int id;
    private String descricao;
    private double valor;
    static private int pontos = 0; 
    
    private ECategoria eCategoria;
    
    public Servico() {
    }

    public Servico(int id, String descricao, double valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
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

    //alteração
    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public static int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        Servico.pontos = pontos;
    }

    public ECategoria geteCategoria() {
        return eCategoria;
    }

    public void seteCategoria(ECategoria eCategoria) {
        this.eCategoria = eCategoria;
    }
    
    @Override
    public String toString() {
        return this.descricao;
    } 
}
