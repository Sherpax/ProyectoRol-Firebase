package com.example.proyectorol.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.proyectorol.CreacionPartida;
import com.example.proyectorol.R;
import com.example.proyectorol.Sesion;
import com.example.proyectorol.adapters.AdaptadorJugadores;
import com.example.proyectorol.adapters.AdaptadorPartidas;
import com.example.proyectorol.pojos.Partida;
import com.example.proyectorol.pojos.Usuario;
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
import java.util.Iterator;
import java.util.List;


public class PartidasFragment extends Fragment {

    private List<Partida> partidas=new ArrayList<>();
    private ArrayList<com.example.proyectorol.ficha.ListaClases> fichasJugador = new ArrayList<>();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Usuario usuario;
    com.example.proyectorol.ficha.ListaClases ficha=null;

    ImageView imagenPrivado; // {Pública,Privada}

    public PartidasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProgressBar barraProgeso;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partidas, container, false);
        FloatingActionButton crearPartidaBot = view.findViewById(R.id.botCrearGrupo);
        crearPartidaBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), CreacionPartida.class);
                startActivity(intent);
            }
        });
        FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
        DatabaseReference ref_fichas = baseDatos.getReference("partidas");
        final AdaptadorPartidas adaptadorPartidas;
        barraProgeso = view.findViewById(R.id.barraProgreso);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        RecyclerView rv = view.findViewById(R.id.rvPartidas);
        rv.setLayoutManager(layoutManager);
        adaptadorPartidas = new AdaptadorPartidas(partidas,getContext());
        rv.setAdapter(adaptadorPartidas);
        //Con esto se coge toda la info de la tabla
        ref_fichas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                    rv.setVisibility(View.VISIBLE); //Mostramos cardview cuando encuentre usuarios
                    barraProgeso.setVisibility(View.GONE); //Quitamos la barrita de progreso si ha encontrado usuarios
                    while(partidas.size()>0){
                        partidas.remove(0);
                    }
                    while (iterator.hasNext()) {
                        partidas.add(iterator.next().getValue(Partida.class));
                    }
                    adaptadorPartidas.notifyDataSetChanged();
                }else{
                    barraProgeso.setVisibility(View.GONE);
                    Toast.makeText(view.getContext(),
                            "No existen usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }
}