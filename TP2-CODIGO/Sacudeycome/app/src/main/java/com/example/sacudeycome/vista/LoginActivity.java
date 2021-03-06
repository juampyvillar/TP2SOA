package com.example.sacudeycome.vista;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sacudeycome.modelo.MiAplicacion;
import com.example.sacudeycome.presentador.LoginFormState;
import com.example.sacudeycome.presentador.LoginResult;
import com.example.sacudeycome.modelo.LoginViewModel;
import com.example.sacudeycome.modelo.LoginViewModelFactory;
import com.example.sacudeycome.presentador.ServicesHttp_POST;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private com.example.sacudeycome.databinding.ActivityLoginBinding binding;
    private static final String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";
    private static final String URI_EVENTO = "http://so-unlam.net.ar/api/api/event";
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private ProgressBar loadingProgressBar ;
    public IntentFilter filtro;
    private ReceptorOperacion receiver = new LoginActivity.ReceptorOperacion();
    private boolean esLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", "oncreate Activity");

        binding = com.example.sacudeycome.databinding.ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginButton = binding.login;
        registerButton = binding.Register;
        loadingProgressBar = binding.loading;


       registerButton.setEnabled(true);
        if(chequearConexionInternet()){
            loginButton.setEnabled(true);
            configurarBroadcastReceiver();
            Toast.makeText(getApplicationContext(), "Hay Conexion a Internet ", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getApplicationContext(), "No hay conexion a Internet ", Toast.LENGTH_SHORT).show();
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);
                esLogin=true;
                JSONObject obj = new JSONObject();

                try {
                    obj.put("email",usernameEditText.getText().toString());
                    obj.put("password",passwordEditText.getText().toString());

                    Intent i = new Intent(LoginActivity.this, ServicesHttp_POST.class);
                    i.putExtra("metodo","POST");
                    i.putExtra("uri", URI_LOGIN);
                    i.putExtra("datosJson", obj.toString());
                    i.putExtra("tipo","loggin");
                    startService(i);
                }catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pasarActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(pasarActivity);
                finish();
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = "Bienvenido/a ";
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public boolean chequearConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void configurarBroadcastReceiver(){
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
                Log.d("Resultadorequest:" ,"Request: " + datosJson.get("success").toString());
                loadingProgressBar.setVisibility(View.GONE);
                if(datosJson.get("success").toString().equals("true")){
                    String token =  new String();
                    String token_refresh =new String();
                    token=datosJson.get("token").toString();
                    token_refresh=datosJson.get("token_refresh").toString();
                    //Seteo el token y token refresh
                    ((MiAplicacion) getApplication()).setUsuario(usernameEditText.getText().toString());
                    ((MiAplicacion) getApplication()).setToken(token);
                    ((MiAplicacion) getApplication()).setToken_refresh(token_refresh);
                    ((MiAplicacion) getApplication()).setTiempoInicio(SystemClock.elapsedRealtime());//Cero desde token refresh
                    Toast.makeText(getApplicationContext(), "Acceso exitoso", Toast.LENGTH_SHORT).show();

                    if(esLogin){
                        registrarEventoEnServidor("Login Correcto", " El usuario "+ usernameEditText.getText() + " se ha loggeado exitosamente");
                        esLogin=false;
                    }

                    Intent pasarActivity  = new Intent(LoginActivity.this, SelectorActivity.class);
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

        public void registrarEventoEnServidor(String tipoEvento, String descripcion){
            JSONObject objEvento = new JSONObject();

            try {
                objEvento.put("env","PROD");
                objEvento.put("type_events",tipoEvento);
                objEvento.put("description",descripcion);

                Intent i = new Intent(LoginActivity.this, ServicesHttp_POST.class);
                i.putExtra("metodo","POST");
                i.putExtra("uri", URI_EVENTO);
                i.putExtra("datosJson", objEvento.toString());
                i.putExtra("tipo","evento");
                startService(i);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


