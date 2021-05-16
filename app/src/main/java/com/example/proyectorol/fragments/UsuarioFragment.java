package com.example.proyectorol.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectorol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UsuarioFragment extends Fragment {

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
        ImageView imgPerfil = view.findViewById(R.id.imagenPerfil);

        final FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        //Podemos hacer un shared preferences y una opción para cambiar el nombre etc
        assert usuario != null;
        nombreUser.setText(usuario.getDisplayName());
        //TODO: ÁNGEL, BUSCA IMÁGENES PARA LOS PERSONAJES QUE QUIERAS USAR...
        // (POR DEFECTO HE PUESTO UN VAMPIRO GRASIOSETE)
        imgPerfil.setImageResource(R.drawable.vampirito);

        return view;
    }
}