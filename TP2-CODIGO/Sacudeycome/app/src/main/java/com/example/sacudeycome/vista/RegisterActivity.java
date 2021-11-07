package com.example.sacudeycome.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sacudeycome.modelo.MiAplicacion;
import com.example.sacudeycome.R;
import com.example.sacudeycome.presentador.Conexiones;
import com.example.sacudeycome.presentador.ServicesHttp_POST;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText editNombre;
    private EditText editApellido;
    private EditText editDni;
    private EditText editEmail;
    private EditText editPass;

    private Button buttonRegistrar;

    public IntentFilter filtro;
    private ReceptorOperacion receiver = new ReceptorOperacion();

    private static final String URI_REGISTER= "http://so-unlam.net.ar/api/api/register";

    private static final Integer COMISION=3900;
    private static final Integer GRUPO=9;
    private Conexiones conexion = new Conexiones();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editNombre = findViewById(R.id.Nombre);
        editApellido = findViewById(R.id.Apellido);
        editDni = findViewById(R.id.DNI);
        editEmail = findViewById(R.id.Email);
        editPass = findViewById(R.id.password);
        buttonRegistrar = findViewById(R.id.register);
        if(conexion.chequearConexionInternet()){
            Toast.makeText(getApplicationContext(), "Hay Conexion a Internet ", Toast.LENGTH_SHORT).show();
            buttonRegistrar.setEnabled(true);
            buttonRegistrar.setOnClickListener(HandlerCmdRegistrar);
            configurarBroadcastReceiver();
        }
        else {
            Toast.makeText(getApplicationContext(), "No hay conexion a Internet ", Toast.LENGTH_SHORT).show();
            buttonRegistrar.setEnabled(false);
        }
    }

    private View.OnClickListener HandlerCmdRegistrar = new View.OnClickListener()
    {

            public void onClick (View v)
            {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("env","PROD");
                    obj.put("name",editNombre.getText().toString());
                    obj.put("lastname",editApellido.getText().toString());
                    obj.put("dni",Long.parseLong(editDni.getText().toString()));
                    obj.put("email",editEmail.getText().toString());
                    obj.put("password",editPass.getText().toString());
                    obj.put("commission",COMISION);
                    obj.put("group",GRUPO);

                    Intent i = new Intent(RegisterActivity.this, ServicesHttp_POST.class);
                    i.putExtra("metodo","POST");
                    i.putExtra("uri", URI_REGISTER);
                    i.putExtra("datosJson", obj.toString());
                    i.putExtra("tipo","registro");
                    startService(i);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    };

    private void configurarBroadcastReceiver(){
        filtro = new IntentFilter("com.example.intentservice.intent.action.RUN");
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver,filtro);
    }

    public class ReceptorOperacion extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent){
            try{
                String datosJsonString = intent.getStringExtra("datosJson");
                JSONObject datosJson = new JSONObject(datosJsonString);
                if(datosJson.get("success").toString().equals("true")){
                    String token =  new String();
                    String token_refresh =new String();
                    token=datosJson.get("token").toString();
                    token_refresh=datosJson.get("token_refresh").toString();
                    ((MiAplicacion) getApplication()).setUsuario(editEmail.getText().toString());
                    ((MiAplicacion) getApplication()).setToken(token);
                    ((MiAplicacion) getApplication()).setToken_refresh(token_refresh);
                    ((MiAplicacion) getApplication()).setTiempoInicio(SystemClock.elapsedRealtime());

                    Toast.makeText(getApplicationContext(), "Acceso exitoso", Toast.LENGTH_SHORT).show();
                    Intent pasarActivity = new Intent(RegisterActivity.this, SelectorActivity.class);
                    startActivity(pasarActivity);
                    unregisterReceiver(receiver);
                    finish();

                }
                else {
                    Toast.makeText(getApplicationContext(), "Login/Registro incorrecto", Toast.LENGTH_SHORT).show();
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}