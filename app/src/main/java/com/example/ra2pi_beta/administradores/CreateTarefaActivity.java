package com.example.ra2pi_beta.administradores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ra2pi_beta.R;
import com.example.ra2pi_beta.informacao.Plano;
import com.example.ra2pi_beta.informacao.Tarefas;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateTarefaActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    Button btnCreateTarefa;
    Button btnCancelTarefa;
    Button btnConcluirPlano;
    EditText editTextTarefaname;
    Plano plano;
    String nomePlano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tarefa);
        nomePlano = getIntent().getStringExtra("NomePlano");



        mDatabase = FirebaseDatabase.getInstance().getReference();
        editTextTarefaname = findViewById(R.id.editTextNomeTarefa);
        btnCreateTarefa = findViewById(R.id.btn_createTarefa);
        btnCancelTarefa = findViewById(R.id.btn_cancelTarefa);

        plano = getPlano();
        btnCreateTarefa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createTarefa();
            }
        });

        btnCancelTarefa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelAndBack();
            }
        });

    }

    public Plano getPlano(){
        for (Plano p:ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.planos) {
            if(p.getTexto().equals(nomePlano)){
                return p;
            }
        }
        return null;
    }

    public int getIdPlano(Plano plano){
        int i = 0;
        for (Plano p:ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.planos) {
            if (p.getTexto().equals(plano.getTexto())){
                return i;
            }
            i++;
        }
        return i;
    }

    public void createTarefa(){
        String usertarefa = editTextTarefaname.getText().toString();
        DatabaseReference reference;
        if (usertarefa!=null){
            if(!tarefaExist(usertarefa)){
                reference = mDatabase;
                int idNovoTarefa = plano.gettarefas().length;
                reference.child(ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.user).child(""+getIdPlano(plano)).
                        child("passos").child(""+idNovoTarefa).child("texto").setValue(""+usertarefa);
                reference.child(ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.user).child(""+getIdPlano(plano)).
                        child("passos").child(""+idNovoTarefa).child("feito").setValue(false);

                Tarefas[] novasTarefas = new Tarefas[idNovoTarefa+1];

                for (int i = 0; i<plano.gettarefas().length ; i++){
                    novasTarefas[i]=plano.gettarefas()[i];
                }
                novasTarefas[novasTarefas.length-1] = new Tarefas(usertarefa,false);
                ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.planos.get(getIdPlano(plano)).setTarefas(novasTarefas);

                Toast.makeText(this, "Tarefa com o nome "+usertarefa+ " criado!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),CreateTarefaActivity.class);
                intent.putExtra("NomePlano",nomePlano);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Tarefa já existe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cancelAndBack(){
        Intent intent = new Intent(getApplicationContext(),ViewListTarefasPlanoUtilizadorActivity.class);
        intent.putExtra("NomePlano",nomePlano);
        startActivity(intent);

    }

    public boolean tarefaExist(String tarefa) {
        if (plano.gettarefas().length > 0) {
            Tarefas[] tarefas = plano.gettarefas();

            for (int t = 0; t < tarefas.length; t++) {
                if (tarefas[t].getTexto().equals(tarefa)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}