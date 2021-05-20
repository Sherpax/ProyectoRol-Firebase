package com.example.proyectorol.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectorol.R;
import com.example.proyectorol.adapters.AdaptadorJugadores;
import com.example.proyectorol.pojos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class FragmentJugadores extends Fragment {

    public FragmentJugadores() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ProgressBar barraProgeso;

        View view = inflater.inflate(R.layout.fragment_jugadores, container, false);

        //Nombre de usuario y perfil
        TextView nombreUser = view.findViewById(R.id.nombreUser1);
        ImageView imgPerfil = view.findViewById(R.id.imagenPerfil1);

        final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        barraProgeso = view.findViewById(R.id.barraProgreso);

        //Podemos hacer un shared preferences y una opción para cambiar el nombre etc
        assert usuario != null;
        nombreUser.setText(usuario.getDisplayName());
        imgPerfil.setImageResource(R.drawable.vampirito);         //CAMBIAR

        RecyclerView rv;

        final ArrayList<Usuario> usuarioArrayList =  new ArrayList<>();
        final AdaptadorJugadores adaptadorJugadores;

        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rv = view.findViewById(R.id.rv1);
        rv.setLayoutManager(layoutManager);

        adaptadorJugadores = new AdaptadorJugadores(usuarioArrayList,getContext());

        rv.setAdapter(adaptadorJugadores);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference db_ref = firebaseDatabase.getReference("usuarios");
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    rv.setVisibility(View.VISIBLE); //Mostramos cardview cuando encuentre usuarios

                    barraProgeso.setVisibility(View.GONE); //Quitamos la barrita de progreso si ha encontrado usuarios
                    //Si algo cambia en tiempo real, que no se vuelva a mostrar:
                    usuarioArrayList.removeAll(usuarioArrayList);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Usuario _user = dataSnapshot.getValue(Usuario.class);
                        usuarioArrayList.add(_user);
                    }
                    adaptadorJugadores.notifyDataSetChanged();
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