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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CreatePlanoActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    Button btnCreatePlano;
    Button btnCancel;
    EditText editTextPlanoname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plano);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        editTextPlanoname = findViewById(R.id.editTextNomePlano);
        btnCreatePlano = findViewById(R.id.btn_createPlano);
        btnCancel = findViewById(R.id.btn_cancelPlano);

        btnCreatePlano.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createPlano();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelAndBack();
            }
        });

    }

    public void createPlano(){
        String userplano = editTextPlanoname.getText().toString();
        DatabaseReference reference;
        if (userplano!=null){
            if(!planoExist(userplano)){
                reference = mDatabase;
                reference.child(ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.user).child(""+ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.planos.size()).
                        child("nome").setValue(""+userplano);

                Toast.makeText(this, "Plano com o nome "+userplano+ " criado!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),ViewListPlanosUtilizadorActivity.class);
                intent.putExtra("User",ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.user);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Plano já existe", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void cancelAndBack(){
        Intent intent = new Intent(getApplicationContext(),ViewListPlanosUtilizadorActivity.class);
        startActivity(intent);

    }

    public boolean planoExist(String planos){
        if(ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.planos.size()>0){
            List<Plano> listPlanos = ViewListPlanosUtilizadorActivity.ViewPlanosAdmin.planos;

                for(int p = 0; p<listPlanos.size();p++){
                    if(listPlanos.get(p).getTexto().equals(planos)){
                        return true;
                    }
                }
            return false;
        }else {
            return false;
        }
    }
}