package com.example.ra2pi_beta.funcionarios;

import android.content.Intent;

public class RespostaActivity {
    private String fala;
    private String resposta;
    private Intent intent;

    public RespostaActivity(String fala, String resposta, Intent intent ) {
        this.fala = fala;
        this.resposta = resposta;
        this.intent = intent;

    }


    public String getFala() {
        return fala;
    }

    public String getResposta() {
        return resposta;
    }

    public Intent getIntent () {
        return intent;
    }
}
