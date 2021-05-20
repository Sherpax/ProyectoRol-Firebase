package com.example.proyectorol.fragments;


import com.example.proyectorol.ElegirPersonaje;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.proyectorol.ElegirPersonaje;
import com.example.proyectorol.R;
import com.example.proyectorol.VerFicha;
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
import java.util.Iterator;
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
                ficha.setUid(usuario.getUid());
                intent.putExtra("Ficha",ficha);
                intent.putExtra("Lista",fichasJugador);
                startActivity(intent);
            }
        });

        //

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ListView listafichas = getView().findViewById(R.id.listFichas);
        final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
        DatabaseReference ref_fichas = baseDatos.getReference("fichas");
        ArrayList<String> nombreFichas= new ArrayList<>();
        //Con esto se coge toda la info de la tabla
        ref_fichas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                ListaClases fichaAux = null;
                while(iterator.hasNext()){
                    fichaAux = iterator.next().getValue(ListaClases.class);
                    if(fichaAux.getUid().equals(usuario.getUid())){
                        fichasJugador.add(fichaAux);
                        nombreFichas.add(fichaAux.getNombre());
                    }
                }
                ArrayAdapter adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                            nombreFichas);
                    listafichas.setAdapter(adaptador);
                    listafichas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(),VerFicha.class);
                            intent.putExtra("Ficha",fichasJugador.get(position));
                            startActivity(intent);
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
        }
    }