package com.example.proyectorol.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.proyectorol.fragments.ChatsFragment;
import com.example.proyectorol.fragments.FichaInfoFragment;
import com.example.proyectorol.fragments.SolicitudesFragment;
import com.example.proyectorol.fragments.UsuarioFragment;

import org.jetbrains.annotations.NotNull;
//Esto controla y gestiona las pestañas de los fragments
public class Adaptador extends FragmentStateAdapter {

    public Adaptador(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        //Cada posición es una pestaña fragment
        switch (position){
            case 0:
                return new UsuarioFragment();
            case 1:
                return new FichaInfoFragment();
            case 2:
                return new ChatsFragment();
            case 3:
            return new SolicitudesFragment();
            default:
                return new UsuarioFragment();
        }
    }

    //¡¡IMPORTANTE!! Modificar esto con el número de elementos
    @Override
    public int getItemCount() {
        return 4;
    }
}
