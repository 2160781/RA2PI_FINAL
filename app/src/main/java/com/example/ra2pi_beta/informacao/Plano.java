package com.example.ra2pi_beta.informacao;

public class Plano {
    private String texto;
    private Tarefas[] tarefas;



    public Plano(String texto, Tarefas[] tarefas) {
        this.texto = texto;
        this.tarefas = tarefas;

    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(){
        this.texto = null;
    }

    public Tarefas[] gettarefas(){
        return tarefas;
    }

    public void setTarefas(Tarefas[] tarefas) {
        this.tarefas = tarefas;
    }
}
