package com.example.sacudeycome;

import android.provider.BaseColumns;

public class EsquemaBase {
    private EsquemaBase(){

    }
    public static class tabla implements BaseColumns {
        public static final String TABLA = "metrica.db";
        public static final String METRICA = "Metrica";
        public static final Integer VALOR = 0;
        public static final String RANGO= "rango";
    }

}