package com.example.sacudeycome.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacudeycome.presentador.GMail;
import com.example.sacudeycome.modelo.MiAplicacion;
import com.example.sacudeycome.modelo.MyOpenHelper;
import com.example.sacudeycome.R;
import com.example.sacudeycome.presentador.Metricas;
import com.example.sacudeycome.presentador.ServicesHttp_POST;
import com.example.sacudeycome.presentador.ShakeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectorActivity extends AppCompatActivity implements SensorEventListener {

    private TextView titulo;
    private TextView descripcion;
    private TextView precio;
    private Button finalizarButton;

    private ArrayList<String[]> listaMenus = new ArrayList<String[]>();
    private ArrayList<String> destinos = new ArrayList<String>();

    private int idMenu = 0;
    private int contShakes = 0;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;
    private String cuerpoMensaje;

    public IntentFilter filtro;
    private ReceptorOperacion receiver = new SelectorActivity.ReceptorOperacion();

    private static final String URI_EVENTO = "http://so-unlam.net.ar/api/api/event";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        listaMenus = MiAplicacion.cargarLista();
        ((MiAplicacion) getApplication()).actualizarTiempoTranscurrido();
        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.desc);
        precio = findViewById(R.id.precio);
        finalizarButton = findViewById(R.id.finalizarButton);
        finalizarButton.setOnClickListener(HandlerCmdRegistrar);
        cargarMenu(idMenu);
        Log.d("Token", ((MiAplicacion) getApplication()).getToken());
        Log.d("Token Refresh", ((MiAplicacion) getApplication()).getToken_refresh());
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        ((MiAplicacion) getApplication()).actualizarTiempoTranscurrido();

        MyOpenHelper dbhelper= new MyOpenHelper(SelectorActivity.this);
        SQLiteDatabase db= dbhelper.getWritableDatabase();
        if(db != null){
            Metricas.ingresarMetrica("Cantidad Shakes mediodia", contShakes, "De 12:00 a 16:00",SelectorActivity.this);
            Metricas.ingresarMetrica("Cantidad Shakes noche", contShakes, "De 20:00 a 00:00",SelectorActivity.this);
            String query=null;
            Cursor c;
            c=db.rawQuery("select * from main.metrica",null);
            contShakes= Integer.parseInt(c.getString(1));
            Toast.makeText(getApplicationContext(),"Cantidad Shakes Mediodia:"+contShakes,Toast.LENGTH_SHORT);
            c.moveToNext();
            contShakes=Integer.parseInt(c.getString(1));
            Toast.makeText(getApplicationContext(),"Cantidad shakes noche:" + contShakes,Toast.LENGTH_SHORT);
        }else
        {
            Log.d("ERROR CREAR LA BASE","ERROR" );
        }


        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count, boolean right) {
                if (idMenu == 0 && !right)
                    Toast.makeText(getApplicationContext(), "Este es el primer menú, realice un shake hacia la derecha para avanzar al siguiente menú", Toast.LENGTH_SHORT).show();
                if (idMenu == listaMenus.size()-1 && right)
                    Toast.makeText(getApplicationContext(), "Este es el ultimo  menú, realice un shake hacia la izquierda para volver al anterior menú", Toast.LENGTH_SHORT).show();
                if (idMenu >= 0 && idMenu < listaMenus.size()-1 && right)
                    idMenu++;
                if (idMenu <= listaMenus.size()-1 && idMenu > 0 && !right)
                    idMenu--;

                cargarMenu(idMenu);

                ((MiAplicacion) getApplication()).actualizarTiempoTranscurrido();

                Calendar rightNow = Calendar.getInstance();
                int horaActual = rightNow.get(Calendar.HOUR_OF_DAY);
                Log.d("HORA","Hora actual: " + horaActual);
               if(horaActual >= 12 & horaActual < 16)
                  Metricas.actualizarMetrica("Cantidad Shakes mediodia",  ++contShakes,SelectorActivity.this);
                else if(horaActual >= 20)
                   Metricas.actualizarMetrica("Cantidad Shakes noche",  ++contShakes,SelectorActivity.this);
            }
        });
    }

    private View.OnClickListener HandlerCmdRegistrar = new View.OnClickListener()
    {
        public void onClick (View v)
        {
            Toast.makeText(getApplicationContext(), "Saliendo de la aplicacion", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void cargarMenu(int numMenu) {
        Log.d("ID MENU", "     " + numMenu);
        String[] campos = listaMenus.get(numMenu);
        titulo.setText(campos[0]);
        descripcion.setText(campos[2]);
        precio.setText("$" + campos[1]);
        ImageView imagen = (ImageView) findViewById(R.id.imagen);
        Drawable myDrawable;

        switch (numMenu) {
            case 0:
                myDrawable = getResources().getDrawable(R.drawable.muzzarella);
                imagen.setImageDrawable(myDrawable);
                break;
            case 1:
                myDrawable = getResources().getDrawable(R.drawable.huevo);
                imagen.setImageDrawable(myDrawable);
                break;
            case 2:
                myDrawable = getResources().getDrawable(R.drawable.fugazzetta);
                imagen.setImageDrawable(myDrawable);
                break;
            case 3:
                myDrawable = getResources().getDrawable(R.drawable.fugazzetta_con_panceta);
                imagen.setImageDrawable(myDrawable);
                break;
            case 4:
                myDrawable = getResources().getDrawable(R.drawable.jamon);
                imagen.setImageDrawable(myDrawable);
                break;
            case 5:
                myDrawable = getResources().getDrawable(R.drawable.jamon_y_morrones);
                imagen.setImageDrawable(myDrawable);
                break;
            case 6:
                myDrawable = getResources().getDrawable(R.drawable.napolitana);
                imagen.setImageDrawable(myDrawable);
                break;
            case 7:
                myDrawable = getResources().getDrawable(R.drawable.calabresa);
                imagen.setImageDrawable(myDrawable);
                break;
            case 8:
                myDrawable = getResources().getDrawable(R.drawable.roquefort);
                imagen.setImageDrawable(myDrawable);
                break;
        }
    }

    private void configurarBroadcastReceiver() {
        filtro = new IntentFilter("com.example.intentservice.intent.action.RUN");
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filtro);
    }


    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        ((MiAplicacion) getApplication()).actualizarTiempoTranscurrido();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        ((MiAplicacion) getApplication()).actualizarTiempoTranscurrido();
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                Toast.makeText(getApplicationContext(), "Pedido Seleccionado", Toast.LENGTH_SHORT).show();
                destinos.add(((MiAplicacion) getApplication()).getUsuario());
                cuerpoMensaje = "<h2><u><font COLOR='red'>Detalle del pedido: </font></u></h2> <BR>"
                        + "<b>Menú:</b> " + "Pizza de " + titulo.getText() + "<BR>"
                        + "<b>Descripción:</b> " + descripcion.getText() + "<BR>"
                        + "<b>Precio:</b> " + precio.getText() + "<BR>"
                        + "<img src='https://image.freepik.com/foto-gratis/gente-comiendo-pizza-restaurante_23-2148172684.jpg' width='200'> <BR><BR>"
                        + "En breve le estaremos alcanzando su pedido, que lo disfrute :)";
                new SendMailTask(SelectorActivity.this).execute("sacudeycome@gmail.com",
                        "sacudeycome123", destinos, "Pedido confirmado... alta pizza", cuerpoMensaje);
                Log.d("tiempo", "Transcurrido: " + ((MiAplicacion) getApplication()).getTiempoInicio());
                if(chequearConexionInternet()){
                    configurarBroadcastReceiver();
                    String username= ((MiAplicacion) getApplication()).getUsuario();
                    registrarEventoEnServidor("Pedido Registrado", " El usuario " + username + " se ha realizado el pedido");
                    Log.d( "ChequeoInternet","Hay Conexion a Internet ");
                }
                else
                    Log.d( "ChequeoInternet","No hay Conexion a Internet ");
            }
        }
        ((MiAplicacion) getApplication()).actualizarTiempoTranscurrido();
    }

    public void registrarEventoEnServidor(String tipoEvento, String descripcion){
        JSONObject objEvento = new JSONObject();

        try {
            objEvento.put("env","PROD");
            objEvento.put("type_events",tipoEvento);
            objEvento.put("description",descripcion);

            Intent i = new Intent(SelectorActivity.this, ServicesHttp_POST.class);
            i.putExtra("metodo","POST");
            i.putExtra("uri", URI_EVENTO);
            i.putExtra("datosJson", objEvento.toString());
            i.putExtra("tipo","evento");
            startService(i);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean chequearConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public class SendMailTask extends AsyncTask {

        private ProgressDialog statusDialog;
        private Activity sendMailActivity;

        public SendMailTask(Activity activity) {
            sendMailActivity = activity;

        }

        protected void onPreExecute() {
            statusDialog = new ProgressDialog(sendMailActivity);
            statusDialog.setMessage("Getting ready...");
            statusDialog.setIndeterminate(false);
            statusDialog.setCancelable(false);
            statusDialog.show();
        }



        @Override
        protected Object doInBackground(Object... args) {
            try {
                ((MiAplicacion) getApplication()).actualizarTiempoTranscurrido();
                Log.i("SendMailTask", "About to instantiate GMail...");
                publishProgress("Processing input....");
                GMail androidEmail = new GMail(args[0].toString(),
                        args[1].toString(), (ArrayList) args[2], args[3].toString(),
                        args[4].toString());
                publishProgress("Preparando mensaje de Email....");
                androidEmail.createEmailMessage();
                publishProgress("Enviando email....");
                androidEmail.sendEmail();
                publishProgress("Email Enviado.");
                Log.i("SendMailTask", "Email Enviado.");
                ((MiAplicacion) getApplication()).actualizarTiempoTranscurrido();
                unregisterReceiver(receiver);
                cargarMenu(0);
            } catch (Exception e) {
                publishProgress(e.getMessage());
                Log.e("SendMailTask", e.getMessage(), e);
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Object... values) {
            statusDialog.setMessage(values[0].toString());

        }

        @Override
        public void onPostExecute(Object result) {
            statusDialog.dismiss();
        }
    }



    public class ReceptorOperacion extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String datosJsonString = intent.getStringExtra("datosJson");
                JSONObject datosJson = new JSONObject(datosJsonString);
                Log.d("Resultadorequest:", "Request: " + datosJson.get("success").toString());
                if (datosJson.get("success").toString().equals("true")) {
                    Toast.makeText(getApplicationContext(), "Pedido registrado ", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Login/Registro incorrecto", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}