package com.example.ra2pi_beta.funcionarios;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ra2pi_beta.PlayActivity;
import com.example.ra2pi_beta.MainActivity;
import com.example.ra2pi_beta.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TarefasActivity extends AppCompatActivity {

    boolean estadoBoton;
    Button boton;
    TextView textV;
    int numeroPlano = 0;
    int posicao = 0;
    String estado;
    ImageView ver_imagem;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefas);

        estadoBoton=true;
        boton=findViewById(R.id.Button);
        textV= findViewById(R.id.textView);
        ver_imagem =findViewById(R.id.imageView);

        numeroPlano = getIntent().getIntExtra("NumeroPlano",0);

        if(PlayActivity.Main.dadosApp_.getFeito(numeroPlano,0) == false){
            estado = " Por fazer";
            ver_imagem.setImageResource(R.drawable.errado);
        }else{
            estado = " Feito";
            ver_imagem.setImageResource(R.drawable.certo);
        }

        textV.setText(PlayActivity.Main.dadosApp_.getTextTarefa(numeroPlano,posicao)+":"
                + estado);

    }

    //Metodos
    @Override
    public boolean dispatchKeyEvent( KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            //Seguinte
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(posicao<PlayActivity.Main.dadosApp_.getNumeroTarefasDePlano(numeroPlano)-1){
                        posicao++;


                        if(PlayActivity.Main.dadosApp_.getFeito(numeroPlano, posicao) == false){
                            estado = " Por fazer";
                            ver_imagem.setImageResource(R.drawable.errado);
                        }else{
                            estado = " Feito";
                            ver_imagem.setImageResource(R.drawable.certo);
                        }
                        textV.setText(PlayActivity.Main.dadosApp_.getTextTarefa(numeroPlano,
                                posicao)+" : " + estado);

                        estadoBoton= false;
                    }
                }
                return true;
            //Anterior
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(posicao>0) {
                        posicao--;
                        estadoBoton = false;
                        if(PlayActivity.Main.dadosApp_.getFeito(numeroPlano,posicao) == false){
                            estado = " Por fazer";
                            ver_imagem.setImageResource(R.drawable.errado);
                        }else{
                            estado = " Feito";
                            ver_imagem.setImageResource(R.drawable.certo);
                        }
                        textV.setText(PlayActivity.Main.dadosApp_.getTextTarefa(numeroPlano,posicao)
                                + " : " + estado);

                    }
                }
                return true;
            //Marcar passo como "Feito"
            case KeyEvent.KEYCODE_DPAD_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    int position = posicao;
                    if(PlayActivity.Main.dadosApp_.getFeito(numeroPlano,position) == false){
                        PlayActivity.Main.dadosApp_.marcarFeito(numeroPlano,position);
                        ver_imagem.setImageResource(R.drawable.certo);
                        estado = " Feito";
                        textV.setText(PlayActivity.Main.dadosApp_.getTextTarefa(numeroPlano,posicao)
                                + " : " + estado);
                        updateFirebase(numeroPlano, position, true);
                    }
                }
                return true;
            //Marcar passo como "Por fazer"
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    int position = posicao;
                    if(PlayActivity.Main.dadosApp_.getFeito(numeroPlano,position) == true){
                        PlayActivity.Main.dadosApp_.marcarErrado(numeroPlano,position);
                        ver_imagem.setImageResource(R.drawable.errado);
                        estado = " Por fazer";
                        textV.setText(PlayActivity.Main.dadosApp_.getTextTarefa(numeroPlano,posicao)
                                + " : " + estado);
                        updateFirebase(numeroPlano, position, false);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_ENTER:
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent inicio = new Intent(this,
                            MainActivity.class);
                    startActivity(inicio);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }

    }

    public void updateFirebase(int plano, int tarefa, boolean estado){
        mDatabase.child(PlayActivity.Main.user).child(""+plano).child("passos").child(""+tarefa).child("feito").setValue(estado);
    }
}