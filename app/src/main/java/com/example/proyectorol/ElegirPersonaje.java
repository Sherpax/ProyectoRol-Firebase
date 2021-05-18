package com.example.proyectorol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectorol.ficha.ListaClases;

import de.hdodenhof.circleimageview.CircleImageView;

public class ElegirPersonaje extends AppCompatActivity {

    com.example.proyectorol.ficha.ListaClases ficha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_personaje);

        CircleImageView imagenRaza = findViewById(R.id.imagenPerfil);
        Spinner spinerRaza = findViewById(R.id.razaSpin);
        ficha= (ListaClases) getIntent().getSerializableExtra("Ficha");
        spinerRaza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String[] clanes = getResources().getStringArray(R.array.clan);
                ficha.setClan(clanes[position]);
                switch (position){
                    case 0:
                        imagenRaza.setImageResource(R.drawable.brujah);
                        break;
                    case 1:
                        imagenRaza.setImageResource(R.drawable.malkavian);
                        break;
                    case 2:
                        imagenRaza.setImageResource(R.drawable.nosferatu);
                        break;
                    case 3:
                        imagenRaza.setImageResource(R.drawable.tremere);
                        break;
                    case 4:
                        imagenRaza.setImageResource(R.drawable.lasombra);
                        break;
                    case 5:
                        imagenRaza.setImageResource(R.drawable.tzimisce);
                        break;
                    case 6:
                        imagenRaza.setImageResource(R.drawable.assamita);
                        break;
                    case 7:
                        imagenRaza.setImageResource(R.drawable.gangrel);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        Spinner naturaleza = findViewById(R.id.spinner2);
        naturaleza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] naturaleza = getResources().getStringArray(R.array.naturaleza);
                ficha.setClan(naturaleza[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void irAtributos(View view) {
        //Guardar datos en DB
        EditText nombre= findViewById(R.id.nombreFicha);
        ficha.setNombre(nombre.getText().toString());
        //Cargar activity
        Intent intentAtribs = new Intent(this, ElegirAtributos.class);
        intentAtribs.putExtra("Ficha",ficha);
        startActivity(intentAtribs);
    }

}