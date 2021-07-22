package com.example.ra2pi_beta.funcionarios;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ra2pi_beta.MainActivity;
import com.example.ra2pi_beta.PlayActivity;
import com.example.ra2pi_beta.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class PlanoQRCodeActivity extends AppCompatActivity {

    String plano;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plano_q_r_code);
        new IntentIntegrator(this).initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String dados = result.getContents();
        Intent intent = null;


        for (int i = 0; i < PlayActivity.Main.dadosApp_.getNumeroPlanos(); i++) {

            plano = PlayActivity.Main.dadosApp_.getTextPlano(i);

            if (plano.equals(dados)) {
                Intent Plano = new Intent(this,
                        TarefasActivity.class);
                Plano.putExtra("NumeroPlano", i);
                startActivity(Plano);

            }

        }


        if (intent != null) {
            startActivity(intent);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "QRCode invalido!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            count++;

            if(count < 3){
                new IntentIntegrator(this).initiateScan();
           }else{
                Intent inicio = new Intent(this,
                        MainActivity.class);
                startActivity(inicio);
            }
        }
    }
}