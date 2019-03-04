package com.conti.autosport.model;

import com.conti.autosport.util.Parte;

import java.util.ArrayList;

public class PreOrder {

    public static final String ID = "ID";

    private Customer cliente;
    private String placa;
    private String marca;
    private String modelo;
    private Local superior;
    private Local frente;
    private Local traseira;
    private Local latEsquerda;
    private Local latDireita;
    private Double desconto;
    private ArrayList<Peca> listaPecas;

    public PreOrder(Customer cliente2, String placa2, String marca2,
                    String modelo2, ArrayList<Peca> pecas) {
        super();
        this.cliente = cliente2;
        this.placa = placa2;
        this.marca = marca2;
        this.modelo = modelo2;
        this.listaPecas = pecas;
    }

    public PreOrder() {
        super();
    }

    public ArrayList<Peca> getListaPecas() {
        return listaPecas;
    }

    public void setListaPecas(ArrayList<Peca> listaPecas) {
        this.listaPecas = listaPecas;
    }

    public void setLocal(Local local, Parte parte) {
        switch (parte) {
            case SUPERIOR:
                this.superior = local;
                break;
            case FRENTE:
                this.frente = local;
                break;
            case TRASEIRA:
                this.traseira = local;
                break;
            case DIREITA:
                this.latDireita = local;
                break;
            case ESQUERDA:
                this.latEsquerda = local;
                break;

            default:
                break;
        }
    }

    public Local getSuperior() {
        return superior;
    }

    public void setSuperior(Local superior) {
        this.superior = superior;
    }

    public Local getFrente() {
        return frente;
    }

    public void setFrente(Local frente) {
        this.frente = frente;
    }

    public Local getTraseira() {
        return traseira;
    }

    public void setTraseira(Local traseira) {
        this.traseira = traseira;
    }

    public Local getLatEsquerda() {
        return latEsquerda;
    }

    public void setLatEsquerda(Local latEsquerda) {
        this.latEsquerda = latEsquerda;
    }

    public Local getLatDireita() {
        return latDireita;
    }

    public void setLatDireita(Local latDireita) {
        this.latDireita = latDireita;
    }

    public Customer getCliente() {
        return cliente;
    }

    public void setCliente(Customer cliente) {
        this.cliente = cliente;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Double getTotalPrice() {
        double total = 0;
        if (this.getSuperior() != null || this.getFrente() != null
                || this.getLatDireita() != null
                || this.getTraseira() != null
                || this.getLatEsquerda() != null || this.getListaPecas() != null) {

            if (this.getSuperior() != null) {
                ArrayList<Servico> servicos = this.getSuperior()
                        .getServices();
                for (int i = 0; i < servicos.size(); i++) {
                    total += servicos.get(i).getPreco();
                }
            }

            if (this.getFrente() != null) {
                ArrayList<Servico> servicos = this.getFrente()
                        .getServices();
                for (int i = 0; i < servicos.size(); i++) {
                    total += servicos.get(i).getPreco();
                }
            }

            if (this.getLatDireita() != null) {
                ArrayList<Servico> servicos = this.getLatDireita()
                        .getServices();
                for (int i = 0; i < servicos.size(); i++) {
                    total += servicos.get(i).getPreco();
                }
            }

            if (this.getTraseira() != null) {
                ArrayList<Servico> servicos = this.getTraseira()
                        .getServices();
                for (int i = 0; i < servicos.size(); i++) {
                    total += servicos.get(i).getPreco();
                }
            }

            if (this.getLatEsquerda() != null) {
                ArrayList<Servico> servicos = this.getLatEsquerda()
                        .getServices();
                for (int i = 0; i < servicos.size(); i++) {
                    total += servicos.get(i).getPreco();
                }
            }

            if (this.getListaPecas() != null) {
                ArrayList<Peca> pecas = this.getListaPecas();
                for (int i = 0; i < pecas.size(); i++) {
                    total += pecas.get(i).getPreco();
                }
            }
        }
        return total;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public boolean isEmpty() {
        return frente == null && superior == null && latDireita == null && latEsquerda == null && traseira == null && listaPecas == null;
    }

}
