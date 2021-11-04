package com.example.proyectorol.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectorol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UsuarioFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
    private final DatabaseReference ref_usuario = baseDatos.getReference("usuarios").child(user.getUid()).child("nombre"); //Esto nos permite controlar las referencias al usuario por ID
    private final DatabaseReference ref_usuario_foto = baseDatos.getReference("usuarios").child(user.getUid()).child("foto"); //Esto nos permite controlar las referencias al usuario por ID

    public UsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuario, container, false);
        //Nombre de usuario y perfil
        TextView nombreUser = view.findViewById(R.id.nombreUser);
        ImageView imgPerfil = view.findViewById(R.id.imagenPerfilUsuario);

        //Podemos hacer un shared preferences y una opción para cambiar el nombre etc
        assert user != null;

        nombreUser.setText(user.getDisplayName());

        //TODO: Probar con single
        ref_usuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String nuevoNombre = snapshot.getValue(String.class);
                if(snapshot.exists()){
                    nombreUser.setText(nuevoNombre);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        ref_usuario_foto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usuarioFoto = snapshot.getValue(String.class);
                if(snapshot.exists()){
                    Picasso.get().load(usuarioFoto)
                            .placeholder(R.drawable.vampirito)
                            .into(imgPerfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //TODO: ÁNGEL, BUSCA IMÁGENES PARA LOS PERSONAJES QUE QUIERAS USAR...
        // (POR DEFECTO HE PUESTO UN VAMPIRO GRASIOSETE)


        return view;
    }



}