package com.example.ra2pi_beta.informacao;

import com.example.ra2pi_beta.MainActivity;

public class DadosApp extends MainActivity {

    private Tarefas[] listaPassos;
    private Plano[] listaPlanos;

    private int posicao;
    private int PassosFeitos = 0;

    int posicaoPlano;

    public DadosApp(Tarefas[] listaPassos, Plano[] listaPlanos, int posicao, int passosFeitos, int posicaoPlano) {
        this.listaPassos = listaPassos;
        this.listaPlanos = listaPlanos;
        this.posicao = posicao;
        PassosFeitos = passosFeitos;
        this.posicaoPlano = posicaoPlano;
    }

    public DadosApp() {
        this.listaPlanos = new Plano[10];
    }

    public void adicionarPlano(String name, Tarefas[] lista){
        if (lista != null && name != null){
            listaPlanos[getNumeroPlanos()] = new Plano(""+name,  lista);

        }

    }

    public String getTextPlano(int posicao){
        return listaPlanos[posicao].getTexto();
    }

    public String getTextTarefa(int idPlano,int idTarefa){
        return listaPlanos[idPlano].gettarefas()[idTarefa].getTexto();
    }

    public Tarefas[] getListaTarefas(int posicao) {
        return listaPlanos[posicao].gettarefas();
    }

    public int getNumeroTarefasDePlano(int idPlano){
        return listaPlanos[idPlano].gettarefas().length;
    }

    public boolean getFeito(int idPlano,int idTarefa){
        return listaPlanos[idPlano].gettarefas()[idTarefa].getFeito();
    }

    public int getSizeListPassos(int posicao) {

        return listaPlanos[posicao].gettarefas().length;
    }


    public void marcarFeito(int idPlano,int idTarefa) {
        listaPlanos[idPlano].gettarefas()[idTarefa].setFeito(true);
    }

    public void marcarErrado(int idPlano,int idTarefa) {
        listaPlanos[idPlano].gettarefas()[idTarefa].setFeito(false);
    }

    public int getNumeroPlanos(){
        int count = 0;
        for (Plano p: listaPlanos) {
            if (p!=null){
                count++;
            }
        }
        return count;
    }

    public int getNumeroPassosFeitos(int posicao) {
        int count = 0;

        Tarefas[] tarefas = listaPlanos[posicao].gettarefas();

        if (tarefas != null) {
            for (Tarefas t : tarefas) {
                if (t.feito) {
                    count++;
                }
            }
        }
        return count;
    }
}
