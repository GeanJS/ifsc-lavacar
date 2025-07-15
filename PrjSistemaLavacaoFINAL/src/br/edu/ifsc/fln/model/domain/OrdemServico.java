package br.edu.ifsc.fln.model.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Geanj
 */
public class OrdemServico {
    
    private int numero;
    private BigDecimal total;
    private LocalDate agenda;
    private double taxaDesconto;
    
    private Veiculo veiculo;
    private List<ItemOS> itensOS;
    private EStatus status;
    
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public BigDecimal getTotal() {
        calcularServico();
        return total;
    }

    public LocalDate getAgenda() {
        return agenda;
    }

    public void setAgenda(LocalDate agenda) {
        this.agenda = agenda;
    }

    public double getTaxaDesconto() {
        return taxaDesconto;
    }

    public void setTaxaDesconto(double taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public List<ItemOS> getItensOS() {
        return itensOS;
    }

    public void setItensOS(List<ItemOS> itensOS) {
        this.itensOS = itensOS;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }
    
    public void add(ItemOS itemOS) {
        if(itensOS == null) {
            itensOS = new ArrayList<>();
        }
        itensOS.add(itemOS);
        itemOS.setOrdemServico(this);
    }
    
    public void remove(ItemOS itemOS) {
        itensOS.remove(itemOS);
    }
    
    private void calcularServico() {
        total = new BigDecimal(0.0);
        for (ItemOS item: this.getItensOS()) {
            total = total.add(item.getValorServico());
        }
        if (taxaDesconto >= 0) {
            BigDecimal desconto = new BigDecimal(total.doubleValue() * taxaDesconto / 100.0);
            total = total.subtract(desconto);    
        }
    }
    
    public String getPlacaVeiculo() {
        return veiculo != null ? veiculo.getPlaca() : "";
    }
}
