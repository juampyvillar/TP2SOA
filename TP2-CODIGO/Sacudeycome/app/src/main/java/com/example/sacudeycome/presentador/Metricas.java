package com.example.sacudeycome.presentador;

import android.content.Context;

import com.example.sacudeycome.modelo.MyOpenHelper;
import com.example.sacudeycome.vista.SelectorActivity;

public class Metricas {
    public static void ingresarMetrica(String titulo, int contadorShakes, String rango, Context contexto) {
        MyOpenHelper dbhelper= new MyOpenHelper(contexto);
        dbhelper.insertar(titulo,contadorShakes,rango);
    }

    public static void actualizarMetrica(String titulo, int contadorShakes, Context contexto){
        MyOpenHelper dbhelper= new MyOpenHelper(contexto);
        dbhelper.actualizar(titulo,contadorShakes);
    }
}
