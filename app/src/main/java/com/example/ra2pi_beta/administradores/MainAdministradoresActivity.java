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
import android.widget.Toast;

import com.example.ra2pi_beta.PlayActivity;
import com.example.ra2pi_beta.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainAdministradoresActivity extends AppCompatActivity {



    private DatabaseReference mDatabase;
    ListView listViewTrabalhador;
    public static class MainAdministradores {

        public  static String[] usernames;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administradores);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        listViewTrabalhador = findViewById(R.id.listview_usersList);
        getUsers();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                listviewUtilizadorTrabalhador();
            }
        }, 3000);


    }

    public void getUsers(){

        DatabaseReference reference1 = mDatabase;
        List<String> userList = new ArrayList<>();

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    String username = singleSnapshot.getKey();
                    String tipo = singleSnapshot.child("tipo").getValue().toString();
                    System.out.println(tipo);

                    if(tipo.equals("func")){
                        userList.add(username);
                        Toast toast = Toast.makeText(getApplicationContext(), "Loading..."
                                , Toast.LENGTH_SHORT);
                        toast.show();
                        System.out.println(username);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(userList != null){
                    MainAdministradores.usernames = new String[userList.size()];

                    int i = 0;
                    for (String user:userList) {
                        MainAdministradores.usernames[i] = user;
                        i++;
                    }
                }
            }
        }, 1500);

    }

    public boolean listviewUtilizadorTrabalhador() {

        String[] values = new String[MainAdministradores.usernames.length+2];

        for(int i = 0; i < values.length-2; i++) {
            values[i] = "" + MainAdministradores.usernames[i];
        }
        values[values.length-2] = "Criar utilizador";
        values[values.length-1] = "Terminar sessão";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,values);

        listViewTrabalhador.setAdapter(adapter);

        listViewTrabalhador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position=position;

                for(int p = 0; p < values.length-2; p++) {
                    if(p == position){
                        Intent intent = new Intent(getApplicationContext(),ViewListPlanosUtilizadorActivity.class);
                        intent.putExtra("User",MainAdministradores.usernames[p]);
                        startActivity(intent);
                    }
                }
                if (position==values.length-2){
                    Intent intent = new Intent(getApplicationContext(),CreateUserActivity.class);
                    startActivity(intent);
                }
                if (position==values.length-1){
                    Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                    startActivity(intent);
                }
            }
        });
        return true;
    }
}