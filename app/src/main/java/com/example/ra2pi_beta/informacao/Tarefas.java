package com.example.ra2pi_beta.informacao;

public class Tarefas {

    public String texto;
    public Boolean feito;

    public Tarefas(String texto, boolean feito) {
        this.texto = texto;
        this.feito = feito;

    }

    public String getTexto() {
        return texto;
    }

    public boolean getFeito(){
        return feito;
    }

    public void setFeito(Boolean feito) {
        this.feito = feito;
    }
}
