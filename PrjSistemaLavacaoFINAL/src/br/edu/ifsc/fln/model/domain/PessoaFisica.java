package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;

/**
 *
 * @author Geanj
 */
public class PessoaFisica extends Cliente{
    
    private String cpf;
    private LocalDate dataNascimento;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
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
