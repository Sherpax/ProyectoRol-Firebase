package com.example.proyectorol.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectorol.CreacionPartida;
import com.example.proyectorol.R;
import com.example.proyectorol.Sesion;
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


public class PartidasFragment extends Fragment {

    private ArrayList<Partida> partidas=new ArrayList<>();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partidas, container, false);
        ListView listPartidas = view.findViewById(R.id.listPartidas);
        imagenPrivado = view.findViewById(R.id.iconTipoPartida);
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
        ArrayList<String> nombrePartidas= new ArrayList<>();
        //Con esto se coge toda la info de la tabla
        ref_fichas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                listPartidas.setAdapter(null);
                while(nombrePartidas.size()>0){
                    nombrePartidas.remove(0);
                }
                while(partidas.size()>0){
                    partidas.remove(0);
                }
                Partida aux = null;
                while (iterator.hasNext()) {
                    aux=iterator.next().getValue(Partida.class);
                    //TODO: Ahora mismo no funciona bien (solo funciona si hay 1 item)
                    //Hay que moverlo todo a un nuevo Adaptador
                    if (aux.isPublica()) {
                        imagenPrivado.setVisibility(View.GONE);
                    } else {
                        imagenPrivado.setVisibility(View.VISIBLE);
                    }
                    //
                    partidas.add(aux);
                    nombrePartidas.add(aux.getNombre());
                    }
                ArrayAdapter adaptador = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,
                        nombrePartidas);
                listPartidas.setAdapter(adaptador);
                listPartidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
                        DatabaseReference ref_fichas = baseDatos.getReference("partidas")
                                .child(partidas.get(position).getIdPartida());
                        baseDatos.getReference("usuarios").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                usuario=(Usuario)snapshot.child(user.getUid()).getValue(Usuario.class);
                                if(partidas.get(position).getPass().isEmpty()){
                                    Intent intent = new Intent(getContext(), Sesion.class);
                                    intent.putExtra("DATOS", partidas.get(position));
                                    ref_fichas.child("jugadores").child(user.getUid()).setValue(usuario);

                                    //ELECCIÓN DE FICHA AL ENTRAR EN LA PARTIDA
                                    FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
                                    DatabaseReference ref_fichas = baseDatos.getReference("fichas");
                                    ArrayList<String> nombreFichas = new ArrayList<>();
                                    //Con esto se coge toda la info de la tabla
                                    ref_fichas.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            AlertDialog.Builder dialogFicha = new AlertDialog.Builder(getContext());
                                            dialogFicha.setTitle("Elige una ficha")
                                                    .setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                                            com.example.proyectorol.ficha.ListaClases fichaAux = null;
                                            while (iterator.hasNext()) {
                                                fichaAux = iterator.next().getValue(com.example.proyectorol.ficha.ListaClases.class);
                                                if (fichaAux!=null && fichaAux.getUid().equals(user.getUid())) {
                                                    fichasJugador.add(fichaAux);
                                                    nombreFichas.add(fichaAux.getNombre());
                                                }
                                            }
                                            String[] nombres = new String[nombreFichas.size()];
                                            for (byte i = 0; i < nombreFichas.size(); i++) {
                                                nombres[i] = nombreFichas.get(i);
                                            }
                                            dialogFicha.setItems(nombres, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ficha = fichasJugador.get(which);
                                                    baseDatos.getReference("partidas").child(partidas.get(position).getIdPartida())
                                                            .child("jugadores").child(user.getUid()).child("ficha").setValue(ficha);
                                                    intent.putExtra("FICHA",ficha);
                                                    startActivity(intent);
                                                }
                                            }).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }else{
                                    AlertDialog.Builder bulder2 = new AlertDialog.Builder(getContext());
                                    bulder2.setTitle("Introduce la contraseña").setView(R.layout.op_introducir_pass)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Dialog in = (Dialog)dialog;
                                                    EditText txt_nombre = in.findViewById(R.id.txtPass);
                                                    com.example.proyectorol.ficha.ListaClases ficha;
                                                    if(txt_nombre.getText().toString().equals(partidas.get(position).getPass())){
                                                        Intent intent = new Intent(getContext(), Sesion.class);
                                                        intent.putExtra("DATOS", partidas.get(position));
                                                        ref_fichas.child("jugadores").child(user.getUid()).setValue(usuario);
                                                        //CODIGO ELEGIR FICHA

                                                        //ELECCIÓN DE FICHA AL ENTRAR EN LA PARTIDA
                                                        FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
                                                        DatabaseReference ref_fichas = baseDatos.getReference("fichas");
                                                        ArrayList<String> nombreFichas = new ArrayList<>();
                                                        //Con esto se coge toda la info de la tabla
                                                        ref_fichas.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                AlertDialog.Builder dialogFicha = new AlertDialog.Builder(getContext());
                                                                dialogFicha.setTitle("Elige una ficha")
                                                                        .setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.cancel();
                                                                            }
                                                                        });
                                                                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
                                                                com.example.proyectorol.ficha.ListaClases fichaAux = null;
                                                                while (iterator.hasNext()) {
                                                                    fichaAux = iterator.next().getValue(com.example.proyectorol.ficha.ListaClases.class);
                                                                    if (fichaAux.getUid().equals(user.getUid())) {
                                                                        fichasJugador.add(fichaAux);
                                                                        nombreFichas.add(fichaAux.getNombre());
                                                                    }
                                                                }
                                                                String[] nombres = new String[nombreFichas.size()];
                                                                for (byte i = 0; i < nombreFichas.size(); i++) {
                                                                    nombres[i] = nombreFichas.get(i);
                                                                }
                                                                dialogFicha.setItems(nombres, new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        PartidasFragment.this.ficha = fichasJugador.get(which);
                                                                        baseDatos.getReference("partidas").child(partidas.get(position).getIdPartida())
                                                                                .child("jugadores").child(user.getUid()).child("ficha").setValue(PartidasFragment.this.ficha);
                                                                        intent.putExtra("FICHA",PartidasFragment.this.ficha);
                                                                        startActivity(intent);
                                                                    }
                                                                }).show();
                                                            }
                                                            @Override
                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                            }
                                                        });

                                                    }else{
                                                        Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                                    }
                                                    dialog.cancel();
                                                }

                                    }).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }
}