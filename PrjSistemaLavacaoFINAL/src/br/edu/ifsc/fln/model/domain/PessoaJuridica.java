package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author Geanj
 */
public class PessoaJuridica extends Cliente{
    
    private String cnpj;
    private String inscricaoEstadual;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String incricaoEstadual) {
        this.inscricaoEstadual = incricaoEstadual;
    }
    
    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }   
    
    @Override
    public String getDados(String observacao) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}
