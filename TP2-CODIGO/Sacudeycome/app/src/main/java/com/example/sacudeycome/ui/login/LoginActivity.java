package com.example.sacudeycome.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.sacudeycome.R;
import com.example.sacudeycome.RegisterActivity;
import com.example.sacudeycome.SelectorActivity;
import com.example.sacudeycome.ServicesHttp_POST;


import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private com.example.sacudeycome.databinding.ActivityLoginBinding binding;
    private static final String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";
    final EditText usernameEditText = binding.username;
    final EditText passwordEditText = binding.password;
    final Button loginButton = binding.login;
    private Button registerButton = binding.Register;
    final ProgressBar loadingProgressBar = binding.loading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug", "oncreate Activity");

        binding = com.example.sacudeycome.databinding.ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);


       registerButton.setEnabled(true);

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

                //Complete and destroy login activity once successful
                //finish();
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
                Log.d("ENTRO LOGIN","Bien1");
                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
                JSONObject obj = new JSONObject();
                try {
                    Log.d("Pasa por acaa boton registro","Biennnnn2");
                    obj.put("email",usernameEditText.getText().toString());
                    obj.put("password",passwordEditText.getText().toString());

                    Intent i = new Intent(LoginActivity.this, ServicesHttp_POST.class);
                    i.putExtra("uri", URI_LOGIN);
                    i.putExtra("datosJson", obj.toString());
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
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = "Bienvenido/a " + usernameEditText.getText();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
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
                    // ParametrosGenerales objeto = ParametrosGenerales.getInstancia(token, token_refresh);
                    Toast.makeText(getApplicationContext(), "Acceso exitoso", Toast.LENGTH_SHORT).show();
                    Intent pasarActivity  = new Intent(LoginActivity.this, SelectorActivity.class);
                    startActivity(pasarActivity);
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


