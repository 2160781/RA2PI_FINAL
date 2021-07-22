package com.example.ra2pi_beta.administradores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ra2pi_beta.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateUserActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    Button btnCreateUser;
    Button btnCancel;
    EditText editTextUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        editTextUsername = findViewById(R.id.editTextNomeUser);
        btnCreateUser = findViewById(R.id.btn_createUser);
        btnCancel = findViewById(R.id.btn_cancelUser);

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createUser();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelAndBack();
            }
        });

    }

    public void createUser(){
        String username = editTextUsername.getText().toString();
        DatabaseReference reference;
        if (username!=null){
            if(!userExist(username)){
                reference = mDatabase;
                reference.child(String.valueOf(Integer.parseInt(username))).child("tipo").setValue("func");

                Toast.makeText(this, "Utilizador com o nome "+username+ " criado!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),MainAdministradoresActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Utilizador já existe", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void cancelAndBack(){
        Intent intent = new Intent(getApplicationContext(),MainAdministradoresActivity.class);
        startActivity(intent);

    }

    public boolean userExist(String username){
        if(MainAdministradoresActivity.MainAdministradores.usernames.length>0){
            String[] listUsernames = MainAdministradoresActivity.MainAdministradores.usernames;

            for (int i = 0; i<listUsernames.length;i++){
                if(listUsernames[i].equals(username)){
                    return true;
                }
            }
            return false;
        }else {
            return false;
        }
    }
}