package com.example.proyectorol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ElegirPersonaje extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_personaje);
    }

    public void irAtributos(View view) {
        //Guardar datos en DB

        //Cargar activity
        Intent intentAtribs = new Intent(this, ElegirAtributos.class);
        startActivity(intentAtribs);
    }

}