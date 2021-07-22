package com.example.ra2pi_beta.administradores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ra2pi_beta.R;
import com.example.ra2pi_beta.informacao.Plano;
import com.example.ra2pi_beta.informacao.Tarefas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewListPlanosUtilizadorActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;


    ListView listViewPlanosUser;

    public static class ViewPlanosAdmin {

        public  static List<Plano> planos;
        public static String user;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_planos_utilizador);
        ViewPlanosAdmin.planos = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listViewPlanosUser = findViewById(R.id.listviewUsersPlanos);

        ViewPlanosAdmin.user = getIntent().getStringExtra("User");

        getUsersPlanos();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                listviewUtilizadorTrabalhador();
            }
        }, 3000);
    }

    public void getUsersPlanos(){
        DatabaseReference reference1 = mDatabase;;

        reference1.child(ViewPlanosAdmin.user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int numPlanos = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));

                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    String key = singleSnapshot.getKey();
                    if (!key.equals("tipo")) {
                        String nome = singleSnapshot.child("nome").getValue().toString();
                        int numeroTarefas = (int) singleSnapshot.child("passos").getChildrenCount();
                        Tarefas[] tarefas = new Tarefas[numeroTarefas];
                        for (int i = 0;i<numeroTarefas;i++){
                            String nomeTarefa = singleSnapshot.child("passos").child(""+i).child("texto").getValue().toString();
                            boolean feito = (boolean) singleSnapshot.child("passos").child(""+i).child("feito").getValue();
                            tarefas[i] = new Tarefas(nomeTarefa,feito);
                        }
                        ViewPlanosAdmin.planos.add(new Plano(nome,tarefas));
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    public boolean listviewUtilizadorTrabalhador() {

        String[] values = new String[ViewPlanosAdmin.planos.size()+1];
        System.out.println();
        for(int i = 0; i < values.length-1; i++) {
            values[i] = "" + ViewPlanosAdmin.planos.get(i).getTexto();

        }
        values[values.length-1] = "Criar plano";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,values);

        listViewPlanosUser.setAdapter(adapter);

        listViewPlanosUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position=position;

                for(int p = 0; p < values.length-1; p++) {
                    if(p == position){
                        Intent intent = new Intent(getApplicationContext(),ViewListTarefasPlanoUtilizadorActivity.class);
                        intent.putExtra("NomePlano", ViewPlanosAdmin.planos.get(p).getTexto());
                        startActivity(intent);
                    }
                }
                if (position==values.length-1){
                    Intent intent = new Intent(getApplicationContext(),CreatePlanoActivity.class);
                    startActivity(intent);
                }
            }
        });
        return true;
    }
}