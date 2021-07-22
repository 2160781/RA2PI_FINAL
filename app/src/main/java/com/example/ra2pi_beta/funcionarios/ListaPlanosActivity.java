package com.example.ra2pi_beta.funcionarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ra2pi_beta.MainActivity;
import com.example.ra2pi_beta.PlayActivity;
import com.example.ra2pi_beta.R;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

public class ListaPlanosActivity extends AppCompatActivity implements AutoPermissionsListener {

    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }
    @Override
    public void onDenied(int i, @NotNull String[] strings) {}

    @Override
    public void onGranted(int i, @NotNull String[] strings) {}

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

    CameraSurfaceView cameraView;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_planos);
        FrameLayout previewFrame = findViewById(R.id.previewFrame);
        cameraView = new CameraSurfaceView(this);
        previewFrame.addView(cameraView);

        AutoPermissions.Companion.loadAllPermissions(this,101);

        listviewPlanos();
    }

    public boolean listviewPlanos(){

        listView = findViewById(R.id.listview_usersList);

        int numP = PlayActivity.Main.dadosApp_.getNumeroPlanos();

        String[] values = new String[numP+1];

        for(int i = 0; i < PlayActivity.Main.dadosApp_.getNumeroPlanos(); i++) {
            values[i] = "" + PlayActivity.Main.dadosApp_.getTextPlano(i) + " - "
                    + PlayActivity.Main.dadosApp_.getNumeroPassosFeitos(i)
                    + " de " + PlayActivity.Main.dadosApp_.getSizeListPassos(i);
        }

        values[values.length-1] = "Voltar atrás";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position=position;

                for(int p = 0; p < values.length-1; p++) {
                    if(p == position){
                        Intent Tarefa = new Intent(view.getContext(),
                                TarefasActivity.class);
                        Tarefa.putExtra("NumeroPlano",position);
                        startActivity(Tarefa);
                    }

                }
                if (position == values.length-1){
                    Intent main = new Intent(view.getContext(),
                            MainActivity.class);
                    startActivity(main);
                }
            }
        });
        return true;
    }



}