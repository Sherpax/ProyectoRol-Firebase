package com.example.proyectorol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerFicha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ficha);
        TextView nombre = findViewById(R.id.nombre);
        TextView clan = findViewById(R.id.clan);
        TextView naturaleza = findViewById(R.id.naturaleza);
        CircleImageView imagenRaza = findViewById(R.id.imagenPerfil2);
        TextView conciencia = findViewById(R.id.conciencia);
        TextView autocontrol = findViewById(R.id.autocontrol);
        TextView coraje = findViewById(R.id.coraje);
        String[] clanes = getResources().getStringArray(R.array.clan);
        boolean imagenEncontrada=false;
        com.example.proyectorol.ficha.ListaClases ficha = (com.example.proyectorol.ficha.ListaClases) getIntent().getSerializableExtra("Ficha");
        //PERSONAJE
        nombre.setText("Nombre: "+ficha.getNombre());
        clan.setText("Clan: "+ficha.getClan());
        naturaleza.setText("Naturaleza: "+ficha.getNaturaleza());
        //FOTO
        byte i=0;
        while(i<clanes.length && !clanes[i].equals(ficha.getClan())){
            i++;
        }
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
        //ATRIBUTOS
        ListView atributos = findViewById(R.id.atributos);
        List<String> listAtributos=new ArrayList<>();
        listAtributos.add("Atributos Fisicos");
        listAtributos.add("Fuerza: "+((HashMap<String,Integer>)ficha.getListaAtributos().get(0)).get("fuerza"));
        listAtributos.add("Destreza: "+((HashMap<String,Integer>)ficha.getListaAtributos().get(0)).get("destreza"));
        listAtributos.add("Resistencia: "+((HashMap<String,Integer>)ficha.getListaAtributos().get(0)).get("resistencia"));
        listAtributos.add("Atributos Mentales");
        listAtributos.add("Astucia: "+((HashMap<String,Integer>)ficha.getListaAtributos().get(1)).get("astucia"));
        listAtributos.add("Inteligencia: "+((HashMap<String,Integer>)ficha.getListaAtributos().get(1)).get("inteligencia"));
        listAtributos.add("Percepcion: "+((HashMap<String,Integer>)ficha.getListaAtributos().get(1)).get("percepcion"));
        listAtributos.add("Atributos Sociales");
        listAtributos.add("Apariencia: "+((HashMap<String,Integer>)ficha.getListaAtributos().get(2)).get("apariencia"));
        listAtributos.add("Carisma: "+((HashMap<String,Integer>)ficha.getListaAtributos().get(2)).get("carisma"));
        listAtributos.add("Manipulación: "+((HashMap<String,Integer>)ficha.getListaAtributos().get(2)).get("manipulacion"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listAtributos);
        atributos.setAdapter(adapter);
        //HABILIDADES
        ListView habilidades = findViewById(R.id.habilidades);
        List<String> listHabilidades=new ArrayList<>();
        listHabilidades.add("Conocimientos");
        listHabilidades.add("Informática: "+((HashMap<String,Integer>)ficha.getListaHabilidades().get(0)).get("informatica"));
        listHabilidades.add("Medicina: "+((HashMap<String,Integer>)ficha.getListaHabilidades().get(0)).get("medicina"));
        listHabilidades.add("Tecnologia: "+((HashMap<String,Integer>)ficha.getListaHabilidades().get(0)).get("tecnologia"));
        listHabilidades.add("Talentos");
        listHabilidades.add("Alerta: "+((HashMap<String,Integer>)ficha.getListaHabilidades().get(1)).get("alerta"));
        listHabilidades.add("Atletismo: "+((HashMap<String,Integer>)ficha.getListaHabilidades().get(1)).get("atletismo"));
        listHabilidades.add("Intimidacion: "+((HashMap<String,Integer>)ficha.getListaHabilidades().get(1)).get("intimidacion"));
        listHabilidades.add("Técnicas");
        listHabilidades.add("Sigilo: "+((HashMap<String,Integer>)ficha.getListaHabilidades().get(2)).get("sigilo"));
        listHabilidades.add("Armas de Fuego: "+((HashMap<String,Integer>)ficha.getListaHabilidades().get(2)).get("armasFuego"));
        listHabilidades.add("Armas cuerpo a cuerpo: "+((HashMap<String,Integer>)ficha.getListaHabilidades().get(2)).get("armasMelee"));
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listHabilidades);
        habilidades.setAdapter(adapter2);
        conciencia.setText("Conciencia: "+((HashMap<String,Long>)ficha.getListaVirtudes().get(0)).get("conciencia"));
        autocontrol.setText("Autocontrol: "+((HashMap<String,Long>)ficha.getListaVirtudes().get(0)).get("autocontrol"));
        coraje.setText("Coraje: "+((HashMap<String,Long>)ficha.getListaVirtudes().get(0)).get("coraje"));
    }
}