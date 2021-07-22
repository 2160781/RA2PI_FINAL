package com.example.ra2pi_beta.funcionarios;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ra2pi_beta.MainActivity;
import com.example.ra2pi_beta.PlayActivity;
import com.example.ra2pi_beta.R;
import com.example.ra2pi_beta.administradores.MainAdministradoresActivity;
import com.example.ra2pi_beta.informacao.Tarefas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class LoginQRCodeActivity extends AppCompatActivity {

    List<String> usernames;
    List<String> keys;
    List<String> nomes;
    String userLogin;
    private DatabaseReference mDatabase;
    private DatabaseReference reference;
    Intent intent;
    int contadorPlanos = 0;
    boolean isAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        keys = new ArrayList<>();
        nomes = new ArrayList<>();
        usernames = new ArrayList<>();
        getUsers();
        setContentView(R.layout.activity_login_qrcode);
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String dados = result.getContents();

        for (String username : usernames) {
            if (username.equals(dados)) {
                userLogin = username;
                PlayActivity.Main.user = username;

                isAdmin = true;
                DatabaseReference reference1 =  mDatabase.child(username);
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            System.out.println(snapshot.child("tipo").getValue().toString());
                            if (snapshot.child("tipo").getValue().toString().equals("admin")){
                                isAdmin = true;
                            }else if (snapshot.child("tipo").getValue().toString().equals("func")){
                                isAdmin = false;
                            }
                            System.out.println(isAdmin);
                        }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }

                });


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        System.out.println(isAdmin);
                        if(!isAdmin){
                            getFirebase(userLogin);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    getTarefas(contadorPlanos);
                                }
                            }, 2000);
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    intent = new Intent(LoginQRCodeActivity.this,
                                            MainActivity.class);
                                }
                            }, 3000);
                        }else {
                            intent = new Intent(LoginQRCodeActivity.this,
                                    MainAdministradoresActivity.class);
                        }
                    }
                }, 1000);



            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (intent != null){
                    startActivity(intent);
                }else{
                    Context context = getApplicationContext();
                    CharSequence text = "QRCode invalido!";
                    new IntentIntegrator(LoginQRCodeActivity.this).initiateScan();
                }

            }
        }, 5000);
    }

    public void getUsers(){
        DatabaseReference reference1 = mDatabase;

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    String nome = singleSnapshot.getKey();
                    usernames.add(nome);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }



    public void getFirebase(String username){
        reference = mDatabase.child(username);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    String key = singleSnapshot.getKey();

                    System.out.println(key);
                    if (!key.equals("tipo")){
                        keys.add(key);
                        String nome = singleSnapshot.child("nome").getValue().toString();
                        nomes.add(nome);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    final void getTarefas(int passos){
        if(nomes.size() > 0){
            DatabaseReference referencePassos = reference.child(""+passos).child("passos");
            referencePassos.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    int lenght_Passos = Integer.parseInt(""+snapshot.getChildrenCount());
                    Toast toast = Toast.makeText(getApplicationContext(), "A carregar...", Toast.LENGTH_SHORT);
                    toast.show();
                    Tarefas[] listaPassos = new Tarefas[lenght_Passos];
                    int i = 0;
                    for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                        Boolean feito = (Boolean) singleSnapshot.child("feito").getValue();
                        String nome = (String) singleSnapshot.child("texto").getValue();
                        listaPassos[i] = new Tarefas(nome,feito);
                        i++;
                    }
                    PlayActivity.Main.dadosApp_.adicionarPlano(""+nomes.get(passos), listaPassos);
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
            if(contadorPlanos<keys.size()-1){
                contadorPlanos++;
                getTarefas(contadorPlanos);
            }
        }
    }
}