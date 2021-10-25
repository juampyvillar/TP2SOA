package com.example.sacudeycome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sacudeycome.ui.login.LoginActivity;

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

    private static final String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";
    private static final String URI_REGISTER= "http://so-unlam.net.ar/api/api/register";

    private static final Integer COMISION=3900;
    private static final Integer GRUPO=9;

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
        buttonRegistrar.setEnabled(true);
        buttonRegistrar.setOnClickListener(HandlerCmdRegistrar);

        configurarBroadcastReceiver();
    }

    private View.OnClickListener HandlerCmdRegistrar = new View.OnClickListener()
    {

            public void onClick (View v)
            {
                Log.d("Pasa por acaa boton registro","Biennnnn1");
                JSONObject obj = new JSONObject();
                try {
                    Log.d("Pasa por acaa boton registro","Biennnnn2");
                    obj.put("env","PROD");
                    obj.put("name",editNombre.getText().toString());
                    obj.put("lastname",editApellido.getText().toString());
                    obj.put("dni",Long.parseLong(editDni.getText().toString()));
                    obj.put("email",editEmail.getText().toString());
                    obj.put("password",editPass.getText().toString());
                    obj.put("commission",COMISION);
                    obj.put("group",GRUPO);

                    Log.d("Pasa por acaa boton registro","Biennnnn3");
                    Intent i = new Intent(RegisterActivity.this, ServicesHttp_POST.class);
                    Log.d("Pasa por acaa boton registro","Biennnnn5");
                    i.putExtra("uri", URI_REGISTER);
                    Log.d("Pasa por acaa boton registro","Biennnnn6");
                    i.putExtra("datosJson", obj.toString());
                    startService(i);
                    Log.d("Pasa por acaa boton registro",obj.toString());
                    Intent pasarActivity = new Intent(RegisterActivity.this, SelectorActivity.class);
                    Log.d("ENTRO","Bien2");
                    startActivity(pasarActivity);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    };

    private void configurarBroadcastReceiver(){
        filtro = new IntentFilter("com.example.intentservice.internet.action.RESPUESTA_OPERACION");
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver,filtro);
    }

    public class ReceptorOperacion extends BroadcastReceiver{
        public void onReceive(Context context, Intent intent){
            try{
                Log.d("Pasa por acaa","Biennnnn1");
                String datosJsonString = intent.getStringExtra("datosJson");
                JSONObject datosJson = new JSONObject(datosJsonString);
                Log.i("Loggeo Main","Datos Json Main Thread: "+datosJsonString);
                Toast.makeText(getApplicationContext(), "Se recibio respuesta del server", Toast.LENGTH_SHORT).show();
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}