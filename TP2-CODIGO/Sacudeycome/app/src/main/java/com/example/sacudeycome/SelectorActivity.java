package com.example.sacudeycome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.ArrayList;

public class SelectorActivity extends AppCompatActivity {

    private static String menuPath = "Menu.txt";
    private ArrayList<String[]> listaCampos = new ArrayList<String[]>();

    private TextView titulo;
    private TextView descripcion;
    private TextView precio;
    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        Log.d("Estoy en el archivo buscando","Sigo buscando");
        leerArchivo();
        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.desc);
        precio = findViewById(R.id.precio);
        imagen = findViewById(R.id.imagen);
        cargarMenu(0);
    }

    public void cargarMenu (int nunMenu){
        Log.d("LLega aca?","LLEGA ACA!");

        String[] campos=listaCampos.get(nunMenu);
        titulo.setText(campos[0]);
        descripcion.setText(campos[2]);
        precio.setText(campos[1]);
        imagen.setImageURI(Uri.parse(menuPath+"/"+campos[3]));
    }




    public void leerArchivo (){
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Verifica permisos para Android 6.0+
            int permissionCheck = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Log.i("Mensaje", "No se tiene permiso para leer.");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
            } else {
                Log.i("Mensaje", "Se tiene permiso para leer!");
            }
        }
            try {
                archivo = new File(menuPath);
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);

                String linea;
                while ((linea = br.readLine()) != null){
                    Log.d("Linea: ", linea);
                    String[] campos = linea.split("-");
                    Log.d("Linea: ", campos[0]);
                    listaCampos.add(campos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fr) {
                        fr.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
    }
}
//Titulo-Precio-Descripcion-IdImagen