package com.example.ra2pi_beta;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ra2pi_beta.funcionarios.LoginQRCodeActivity;
import com.example.ra2pi_beta.informacao.DadosApp;


public class PlayActivity extends AppCompatActivity {

    public static class Main {
        public static DadosApp dadosApp_;
        public static String user;
        public static Boolean administrador;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Main.dadosApp_ = new DadosApp();

        Intent main = new Intent(this, LoginQRCodeActivity.class);
        startActivity(main);

    }

}