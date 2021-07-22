package com.example.ra2pi_beta.administradores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ra2pi_beta.R;
import com.example.ra2pi_beta.informacao.Plano;
import com.example.ra2pi_beta.informacao.Tarefas;

public class ViewListTarefasPlanoUtilizadorActivity extends AppCompatActivity {

    Tarefas[] tarefas;
    String nomePlano;
    Plano plano;
    ListView listViewTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_tarefas_plano_utilizador);
        listViewTarefas = findViewById(R.id.listviewUsersTarefasPlanos);
        nomePlano = getIntent().getStringExtra("NomePlano");

        plano = getPlano();

        if(plano!=null){

            tarefas = plano.gettarefas();
            listviewTarefasShow();
        }
    }
    public Plano getPlano(){
        for (Plano p:ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.planos) {
            if(p.getTexto().equals(nomePlano)){
                return p;
            }
        }
        return null;
    }

    public boolean listviewTarefasShow() {

        String[] values = new String[tarefas.length+1];
        System.out.println();
        for(int i = 0; i < values.length-1; i++) {
            values[i] = "" + tarefas[i].getTexto() + ":" + tarefas[i].getFeito();
        }

        values[values.length-1] = "Criar tarefa";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,values);

        listViewTarefas.setAdapter(adapter);

        listViewTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position=position;

                for(int p = 0; p < values.length; p++) {
                    if (position==values.length-1){
                        Intent intent = new Intent(getApplicationContext(),CreateTarefaActivity.class);
                        intent.putExtra("NomePlano", plano.getTexto());
                        startActivity(intent);
                    }
                }
            }
        });
        return true;
    }
}