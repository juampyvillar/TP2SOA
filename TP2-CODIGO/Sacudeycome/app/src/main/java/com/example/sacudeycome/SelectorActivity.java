package com.example.sacudeycome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

public class SelectorActivity extends AppCompatActivity {

    private static String menuPath = "Menu.txt";
    private ArrayList<String[]> listaMenus = new ArrayList<String[]>();

    private TextView titulo;
    private TextView descripcion;
    private TextView precio;
    private ImageView imagen;


    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        Log.d("Estoy en el archivo buscando","Sigo buscando");
//        leerArchivo();
        cargarLista();
        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.desc);
        precio = findViewById(R.id.precio);
        imagen = findViewById(R.id.imagen);
        cargarMenu(0);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    public void cargarMenu (int nunMenu){
        Log.d("LLega aca?","LLEGA ACA!");

        String[] campos=listaMenus.get(nunMenu);
        titulo.setText(campos[0]);
        descripcion.setText(campos[2]);
        precio.setText(campos[1]);

//        switch (nunMenu) {
//            case 0:
//                imagen.setImageResource(R.drawable.muzzarella);
//                break;
//            case 1:
//                imagen.setImageResource(R.drawable.huevo);
//                break;
//            case 2:
//                imagen.setImageResource(R.drawable.fugazzetta);
//                break;
//            case 3:
//                imagen.setImageResource(R.drawable.fugazzetta_con_panceta);
//                break;
//            case 4:
//                imagen.setImageResource(R.drawable.jamon);
//                break;
//            case 5:
//                imagen.setImageResource(R.drawable.jamon_y_morrones);
//                break;
//            case 6:
//                imagen.setImageResource(R.drawable.napolitana);
//                break;
//            case 7:
//                imagen.setImageResource(R.drawable.calabresa);
//                break;
//            case 8:
//                imagen.setImageResource(R.drawable.roquefort);
//                break;
//        }
//        imagen.setImageURI(Uri.parse(menuPath+"/"+campos[3]));
    }


    public void cargarLista(){
        listaMenus.add(("Muzzarella-460-Salsa de tomate, muzzarella, aceitunas").split("-"));
        listaMenus.add(("Huevo-500-Salsa de tomate, muzzarella, huevo duro, oregano, aceitunas").split("-"));
        listaMenus.add(("Fugazzetta-500-Cebolla, muzzarella, adobo p/pizza, aceitunas").split("-"));
        listaMenus.add(("Fugazzeta con panceta-550-Muzzarella, cebolla, panceta, adobo p/pizza, aceitunas").split("-"));
        listaMenus.add(("Jamon-550-Salsa de tomate, muzzarella, jamon, oregano, aceitunas").split("-"));
        listaMenus.add(("Jamon y morrones-600-Salsa de tomate, muzzarella, jamon, morrones asados, oregano, aceitunas").split("-"));
        listaMenus.add(("Napolitana-510-Salsa de tomate, muzzarella, tomate en rodajas, provenzal, aceitunas").split("-"));
        listaMenus.add(("Calabresa-610-Salsa de tomate, muzzarella,longaniza, morron asado en tiras, oregano, aceitunas").split("-"));
        listaMenus.add(("Roquefort-610-Salsa de tomate, muzzarella, roquefort, oregano, aceitunas").split("-"));
    }

    public void leerArchivo (){
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;

            try {
                archivo = new File("Menu.txt");
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);

                String linea;
                while ((linea = br.readLine()) != null){
                    Log.d("Linea: ", linea);
                    String[] campos = linea.split("-");
                    Log.d("Linea: ", campos[0]);
                    listaMenus.add(campos);
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

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }



}

//Titulo-Precio-Descripcion-IdImagen