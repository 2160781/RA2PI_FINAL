package com.example.ra2pi_beta.funcionarios;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ra2pi_beta.MainActivity;
import com.example.ra2pi_beta.R;

import java.text.Normalizer;
import java.util.ArrayList;

public class NavegacaoVozActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int RECOGNIZE_SPEECH_ACTIVITY = 2;
    private static final int RECONHECEDOR_VOZ = 7;
    private TextView ouve;
    private TextView resposta;
    private ArrayList <RespostaActivity> respostaActivities;
    private TextToSpeech ler;
    private Object TextView;

    private String fala;
    private String resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacaovoz);
        inicializar();
        ativacaoMicro();
    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (resultCode == RESULT_OK && requestCode == RECONHECEDOR_VOZ) {
            ArrayList<String> reconhecido =
                    data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
            String ouvir = reconhecido.get(0);
            fala = ouvir;
            ouve.setText(ouvir);
            prepararRespuesta(ouvir);
        }
    }

    private void prepararRespuesta (String ouvir) {
        String normalizar = Normalizer.normalize(ouvir, Normalizer.Form.NFD);
        String sint = normalizar.replaceAll("[^\\p{ASCII}]", "");

        for (int i = 0; i < respostaActivities.size(); i++) {
            int resultado = sint.toLowerCase().indexOf(respostaActivities.get(i).getFala());
            if (resultado != -1) {
                responder(respostaActivities.get(i));
                return;
            }
        }
    }

    private void responder (RespostaActivity respostaActivity) {
        startActivity( respostaActivity.getIntent() );
    }


    private void inicializar () {

        ouve = (TextView) findViewById(R.id.textView);
        resposta = (TextView) findViewById(R.id.tvRespuesta);
        respostaActivities = provarDados();
        ler = new TextToSpeech(this, this);
    }

    private ArrayList <RespostaActivity> provarDados () {
        ArrayList <RespostaActivity> respostaActivities = new ArrayList <>();

        //Qr code Planos
        respostaActivities.add(new RespostaActivity("scan", "  ",
                new Intent(this,
                PlanoQRCodeActivity.class)));
        respostaActivities.add(new RespostaActivity("qr code", "  ",
                new Intent(this,
                PlanoQRCodeActivity.class)));
        respostaActivities.add(new RespostaActivity("code", "  ",
                new Intent(this,
                        PlanoQRCodeActivity.class)));
        respostaActivities.add(new RespostaActivity("qr", "  ",
                new Intent(this,
                        PlanoQRCodeActivity.class)));
        respostaActivities.add(new RespostaActivity("scaner", "  ",
                new Intent(this,
                        PlanoQRCodeActivity.class)));

        //Lista dos Planos

        respostaActivities.add(new RespostaActivity("planos", "  ",
                new Intent(this,
                        ListaPlanosActivity.class)));
        respostaActivities.add(new RespostaActivity("lista planos", "  ",
                new Intent(this,
                        ListaPlanosActivity.class)));
        respostaActivities.add(new RespostaActivity("tarefas" , "  ",
                new Intent(this,
                        ListaPlanosActivity.class)));

        //Inicio
        respostaActivities.add( new RespostaActivity( "inicio", " ",
                new Intent(this,
                MainActivity.class)));

        return respostaActivities;
    }

    @Override
    public boolean dispatchKeyEvent( KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            //Ligar micro
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (action == KeyEvent.ACTION_DOWN) {
                    ativacaoMicro();
                }
                return true;
            //Anterior
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent main = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(main);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    public void ativacaoMicro(){
        Intent falar = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        falar.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX" );
        startActivityForResult( falar, RECONHECEDOR_VOZ);
    }

    @Override
    public void onInit(int status) {

    }
}
