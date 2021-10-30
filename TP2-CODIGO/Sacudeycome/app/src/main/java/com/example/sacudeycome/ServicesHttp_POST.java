package com.example.sacudeycome;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class ServicesHttp_POST extends IntentService {

    private Exception exception=null;
    private HttpURLConnection httpConnection;
    private URL mUrl;

    public ServicesHttp_POST() { super("ServicesHttp_Get");}

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("Loggeo_Service","Service onCreate()");
    }

    protected void onHandleIntent(Intent intent){
        try {
            Log.d("Pasa por acaa Handle ","Biennnnn1");
            String metodo = intent.getExtras().getString("metodo");
            String uri = intent.getExtras().getString("uri");
            JSONObject datosJson = new JSONObject(intent.getExtras().getString("datosJson"));
            if(metodo.equals("POST")) {
                ejecutarPost(uri,datosJson);
            } else if(metodo.equals("PUT")) {
                ejecutarPut(uri,new JSONObject());
            } else {
                Log.d("Service","Metodo " + metodo +" no valido para el servidor");
            }
        }catch(Exception e) {
            Log.e("Loggeo_Service","Error: "+e.toString());
        }
    }

    private StringBuilder convertInputStreamToString(InputStreamReader inputStream) throws IOException{
        BufferedReader br = new BufferedReader(inputStream);
        StringBuilder result = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            result.append((line + "\n"));
        }
        br.close();
        return result;
    }

    private String POST(String uri, JSONObject datosJson){
         HttpURLConnection urlConnection = null;
        String result = "";
        try{
            URL mUrl = new URL(uri);
            urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(5000);
            Log.i("Loggeo_Service","               33333333333"+datosJson.toString());
            urlConnection.setRequestMethod("POST");
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(datosJson.toString().getBytes(StandardCharsets.UTF_8));
            Log.i("Loggeo_Service","Se va a enviar al servidor"+datosJson.toString());
            wr.flush();
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if((responseCode == HttpURLConnection.HTTP_OK) || (responseCode == HttpURLConnection.HTTP_CREATED)){
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                result = convertInputStreamToString(inputStream).toString();
            }
            else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getErrorStream());
                result = convertInputStreamToString(inputStream).toString();
            }
            else{
                result = "NO_OK";
            }
            exception = null;
            wr.close();
            urlConnection.disconnect();
            return result;

        }catch(Exception e){
            exception = e;
            return null;
        }
    }



    protected void ejecutarPost(String uri,JSONObject datosJson){
        String result = POST(uri,datosJson);
        Log.d("POST prueba: ", (result!=null? result:"no hay result"));
        if(result == null){
            Log.e("Loggeo_Service", "Error en Post:\n" + exception.toString());
            return;
        }
        if(result == "NO_OK"){
            Log.e("Loggeo_Service","Se recibio response NO_OK");
            return;
        }
        Intent i = new Intent("com.example.intentservice.intent.action.RUN");
        i.putExtra("datosJson", result);
        sendBroadcast(i);
        Log.i("Loggeo_Service","               44444444444");
    }

    private String PUT(String uri, JSONObject datosJson){
        HttpURLConnection urlConnection = null;
        String result = "";
        try{
            URL mUrl = new URL(uri);
            urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            String token_refresh = ((MiAplicacion) getApplication()).getToken_refresh();
            urlConnection.setRequestProperty("Authorization", "Bearer " + token_refresh +"; charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(5000);
            Log.i("Actualizar_token","               33333333333");
            urlConnection.setRequestMethod("PUT");
            //en el caso de PUT no se envia nada al body
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(datosJson.toString().getBytes(StandardCharsets.UTF_8));
            Log.i("Actualizar_token","Se va a enviar al servidor"+datosJson.toString());
            wr.flush();
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if((responseCode == HttpURLConnection.HTTP_OK) || (responseCode == HttpURLConnection.HTTP_CREATED)){
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                result = convertInputStreamToString(inputStream).toString();
            }
            else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getErrorStream());
                result = convertInputStreamToString(inputStream).toString();
            }
            else{
                result = "NO_OK";
            }
            exception = null;
            wr.close();
            urlConnection.disconnect();
            return result;

        }catch(Exception e){
            exception = e;
            return null;
        }
    }
    protected void ejecutarPut(String uri,JSONObject datosJson){
        String result = PUT(uri,datosJson);
        Log.d("POST prueba: ", (result!=null? result:"no hay result"));
        if(result == null){
            Log.e("Put_Service", "Error en Put:\n" + exception.toString());
            return;
        }
        if(result == "NO_OK"){
            Log.e("Put_Service","Se recibio response NO_OK");
            return;
        }
        Intent i = new Intent("com.example.intentservice.intent.action.RUN");
        i.putExtra("datosJson", result);
        sendBroadcast(i);
        Log.i("Loggeo_Service","               44444444444");
    }

    @Override
    public void onDestroy()
    {
        super.onCreate();
        Log.i("Loggeo Service", "Service onDestroy()");
    }
}
