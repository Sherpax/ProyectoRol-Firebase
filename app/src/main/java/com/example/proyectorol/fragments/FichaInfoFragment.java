package com.example.proyectorol.fragments;


import com.example.proyectorol.ElegirPersonaje;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyectorol.ElegirPersonaje;
import com.example.proyectorol.R;
import com.example.proyectorol.ficha.ListaClases;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FichaInfoFragment extends Fragment {

    private Map<String,ListaClases> fichas = new HashMap<>();
    private ArrayList<ListaClases> fichasJugador=new ArrayList<>();

    public FichaInfoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ficha_info, container, false);
        //Cargamos las fichas de la BD y las ponemos en el listView


        FloatingActionButton aniadirFicha = view.findViewById(R.id.creaFicha);
        aniadirFicha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),ElegirPersonaje.class);
                com.example.proyectorol.ficha.ListaClases ficha = new ListaClases();
                final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
                ficha.setEmailJugador(usuario.getEmail());
                intent.putExtra("Ficha",ficha);
                startActivity(intent);
            }
        });

        //

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
        DatabaseReference ref_fichas = baseDatos.getReference("fichas");
        //Con esto se coge toda la info de la tabla
        ref_fichas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                fichas = (Map<String, ListaClases>) snapshot.getValue(ListaClases.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        ArrayList<String> nombreFichas= new ArrayList<>();
        //La filtramos para dejar la relativa al usuario
        for(Map.Entry<String,ListaClases> entry : fichas.entrySet()){
            if(entry.getValue().getEmailJugador().equals(usuario.getEmail())){
                fichasJugador.add(entry.getValue());
                nombreFichas.add(entry.getKey());
            }
        }
        ListView listafichas = getView().findViewById(R.id.listFichas);
        ArrayAdapter adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
            nombreFichas);
        listafichas.setAdapter(adaptador);
    }
}