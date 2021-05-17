package com.example.proyectorol.fragments;


import com.example.proyectorol.ElegirPersonaje;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectorol.ElegirPersonaje;
import com.example.proyectorol.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FichaInfoFragment extends Fragment {


    public FichaInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ficha_info, container, false);
        //
        FloatingActionButton aniadirFicha = view.findViewById(R.id.creaFicha);

        aniadirFicha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),ElegirPersonaje.class);
                startActivity(intent);
            }
        });

        //

        return view;
    }
}