package com.example.proyectorol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectorol.ficha.ListaClases;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ElegirPersonaje extends AppCompatActivity {

    com.example.proyectorol.ficha.ListaClases ficha;
    private boolean nombreDuplicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_personaje);
        CircleImageView imagenRaza = findViewById(R.id.imagenPerfil);
        Spinner spinerRaza = findViewById(R.id.razaSpin);
        ficha= (ListaClases) getIntent().getSerializableExtra("Ficha");
        if(ficha.getNombre()!=null){
            this.recuperarDatos();
        }
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
                ficha.setNaturaleza(naturaleza[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void recuperarDatos(){
        final EditText nombre= findViewById(R.id.nombreFicha);
        Spinner naturaleza = findViewById(R.id.spinner2);
        Spinner spinerRaza = findViewById(R.id.razaSpin);
        String[] clanes = getResources().getStringArray(R.array.clan);
        String[] naturalezas = getResources().getStringArray(R.array.naturaleza);
        CircleImageView imagenRaza = findViewById(R.id.imagenPerfil);
        byte i=0;
        nombre.setText(ficha.getNombre());
        while(i<clanes.length && !clanes[i].equals(ficha.getClan())){
            i++;
        }
        spinerRaza.setSelection(i);
        switch (i){
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
        i=0;
        while(i<naturalezas.length && !naturalezas[i].equals(ficha.getNaturaleza())){
            i++;
        }
        naturaleza.setSelection(i);
    }

    public void irAtributos(View view) {
        final EditText nombre= findViewById(R.id.nombreFicha);
        final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        boolean nombreDuplicado=false;
        for(ListaClases i:(ArrayList<ListaClases>)getIntent().getSerializableExtra("Lista")){
            if(!nombreDuplicado && i.getNombre().equals(nombre.getText().toString())){
                nombreDuplicado=true;
            }
        }
        if(nombreDuplicado){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Ya posees una ficha con ese nombre de personaje. Si continuas el proceso" +
                    "con este nombre de personaje se sobreescribira tu otra ficha. ¿Deseas continuar?");
            dialog.setTitle("Nombre ya existente");
            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whiSnacch) {
                    ficha.setNombre(nombre.getText().toString());
                    //Cargar activity
                    Intent intentAtribs = new Intent(getApplicationContext(), ElegirAtributos.class);
                    intentAtribs.putExtra("Ficha",ficha);
                    dialog.cancel();
                    startActivity(intentAtribs);
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }else{
            if(!nombre.getText().toString().isEmpty()) {
                ficha.setNombre(nombre.getText().toString());
                //Cargar activity
                Intent intentAtribs = new Intent(getApplicationContext(), ElegirAtributos.class);
                intentAtribs.putExtra("Ficha", ficha);
                startActivity(intentAtribs);
            }else{
                Toast.makeText(this,
                        "El nombre no debe estar vacío", Toast.LENGTH_SHORT).show();
            }
        }
    }
}