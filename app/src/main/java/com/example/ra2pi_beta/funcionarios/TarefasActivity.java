package com.example.ra2pi_beta.funcionarios;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ra2pi_beta.PlayActivity;
import com.example.ra2pi_beta.MainActivity;
import com.example.ra2pi_beta.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

public class TarefasActivity extends AppCompatActivity implements @NotNull AutoPermissionsListener {

    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }
    @Override
    public void onDenied(int i, @NotNull String[] strings) {
    }

    @Override
    public void onGranted(int i, @NotNull String[] strings) {
    }

    class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera camera = null;

        public CameraSurfaceView (Context context){
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);
        }
        public void surfaceCreated(SurfaceHolder holder){
            camera = Camera.open();
            try {
                camera.setPreviewDisplay(mHolder);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        public void surfaceChanged (SurfaceHolder holder, int format,int width, int height){
            camera.startPreview();
        }

        public void surfaceDestroyed (SurfaceHolder holder){
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        public boolean capture(Camera.PictureCallback handler){
            if (camera != null){
                camera.takePicture(null,null,handler);
                return true;
            }else {
                return false;
            }
        }
    }

    TarefasActivity.CameraSurfaceView cameraView;

    boolean estadoBoton;
    Button boton;
    TextView textV;
    int numeroPlano = 0;
    int posicao = 0;
    String estado;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefas);

        FrameLayout previewFrame = findViewById(R.id.previewFrame);
        cameraView = new CameraSurfaceView(this);
        previewFrame.addView(cameraView);

        AutoPermissions.Companion.loadAllPermissions(this,101);

        estadoBoton=true;
        boton=findViewById(R.id.Button);
        textV= findViewById(R.id.textView);

        numeroPlano = getIntent().getIntExtra("NumeroPlano",0);

        if(PlayActivity.Main.dadosApp_.getFeito(numeroPlano,0) == false){
            estado = " Por fazer";
        }else{
            estado = " Feito";
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
                        }else{
                            estado = " Feito";
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
                        }else{
                            estado = " Feito";
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void updateFirebase(int plano, int tarefa, boolean estado){
        mDatabase.child(PlayActivity.Main.user).child(""+plano).child("passos").child(""+tarefa).child("feito").setValue(estado);
    }
}