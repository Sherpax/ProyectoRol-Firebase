package com.example.proyectorol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class ElegirPersonaje extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_personaje);

        CircleImageView imagenRaza = findViewById(R.id.imagenPerfil);
        Spinner spinerRaza = findViewById(R.id.razaSpin);

        spinerRaza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position){
                    case 0:
                        imagenRaza.setImageResource(R.drawable.vampirito);
                        break;
                    case 1:
                        //imagenRaza.setImageDrawable(R.drawable);
                        break;
                    case 2:
                     //   imagenRaza.setImageDrawable(R.drawable);
                        break;
                    case 3:
                     //   imagenRaza.setImageDrawable(R.drawable);
                        break;
                    case 4:
                     //   imagenRaza.setImageDrawable(R.drawable);
                        break;
                    case 5:
                     //   imagenRaza.setImageDrawable(R.drawable);
                        break;
                    case 6:
                     //   imagenRaza.setImageDrawable(R.drawable);
                        break;
                    case 7:
                     //   imagenRaza.setImageDrawable(R.drawable);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

    }

    public void irAtributos(View view) {
        //Guardar datos en DB

        //Cargar activity
        Intent intentAtribs = new Intent(this, ElegirAtributos.class);
        startActivity(intentAtribs);
    }

}