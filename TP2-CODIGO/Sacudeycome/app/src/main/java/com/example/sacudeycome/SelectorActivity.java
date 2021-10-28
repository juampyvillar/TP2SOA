package com.example.sacudeycome;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SelectorActivity extends AppCompatActivity implements SensorEventListener{

    private final ArrayList<String[]> listaMenus = new ArrayList<String[]>();

    private TextView titulo;
    private TextView descripcion;
    private TextView precio;

    private ArrayList<String> destinos = new ArrayList<String>();

    private int idMenu = 0;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        cargarLista();
        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.desc);
        precio = findViewById(R.id.precio);
        cargarMenu(idMenu);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count,boolean right) {
                if(idMenu>=0 && idMenu<8 && right)
                        idMenu++;
                if(idMenu<=8 && idMenu>0&& !right)
                        idMenu--;
                cargarMenu(idMenu);
            }
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void cargarMenu (int numMenu){
        Log.d("ID MENU","     "+numMenu);
        String[] campos=listaMenus.get(numMenu);
        titulo.setText(campos[0]);
        descripcion.setText(campos[2]);
        precio.setText("$"+campos[1]);
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

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener( this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
                destinos.add("ezezella@gmail.com");
                destinos.add("jjuampy11@gmail.com");
                destinos.add("francogd@hotmail.es");
                GMail mail = new GMail("sacudeycome@hotmail.com","sacudeycome123",destinos, "Pedido", "Pedido confirmado, alta pizza");
                try {
                    mail.sendEmail();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
//            else {
//                far
//                Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class GMail {
        final String emailPort = "587";// gmail's smtp port
        final String smtpAuth = "true";
        final String starttls = "true";
        final String emailHost = "smtp.gmail.com";
        String fromEmail;
        String fromPassword;
        ArrayList<String> toEmailList;
        String emailSubject;
        String emailBody;
        Properties emailProperties;
        Session mailSession;
        MimeMessage emailMessage;
        public GMail() {
        }
        public GMail(String fromEmail, String fromPassword, ArrayList toEmailList, String emailSubject, String emailBody) {
            this.fromEmail = fromEmail;
            this.fromPassword = fromPassword;
            this.toEmailList = toEmailList;
            this.emailSubject = emailSubject;
            this.emailBody = emailBody;
            emailProperties = System.getProperties();
            emailProperties.put("mail.smtp.port", emailPort);
            emailProperties.put("mail.smtp.auth", smtpAuth);
            emailProperties.put("mail.smtp.starttls.enable", starttls);
            Log.i("GMail", "Mail server properties set.");
        }
        public MimeMessage createEmailMessage() throws AddressException,
                MessagingException, UnsupportedEncodingException {
            mailSession = Session.getDefaultInstance(emailProperties, null);
            emailMessage = new MimeMessage(mailSession);
            emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
            for (String toEmail : toEmailList) {
                Log.i("GMail","toEmail: "+toEmail);
                emailMessage.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(toEmail));
            }
            emailMessage.setSubject(emailSubject);
            emailMessage.setContent(emailBody, "text/html");// for a html email
            // emailMessage.setText(emailBody);// for a text email
            Log.i("GMail", "Email Message created.");
            return emailMessage;
        }
        public void sendEmail() throws AddressException, MessagingException {
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(emailHost, fromEmail, fromPassword);
            Log.i("GMail","allrecipients: "+emailMessage.getAllRecipients());
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
            Log.i("GMail", "Email sent successfully.");
        }
    }

}


//Titulo-Precio-Descripcion-IdImagen