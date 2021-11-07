package com.example.sacudeycome.presentador;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.TextView;

import com.example.sacudeycome.vista.MainActivity;

public class Bateria {
    public static void verificarBateria(TextView bateria, Context contexto) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = contexto.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        Float batteryPct = level * 100 / (float)scale;
        bateria.setText("Nivel de Bateria: " + batteryPct.intValue() + "%");
    }
}
