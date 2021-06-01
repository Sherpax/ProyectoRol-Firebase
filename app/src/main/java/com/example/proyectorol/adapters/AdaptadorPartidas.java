package com.example.proyectorol.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectorol.R;
import com.example.proyectorol.Sesion;
import com.example.proyectorol.fragments.PartidasFragment;
import com.example.proyectorol.pojos.Partida;
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
import java.util.Iterator;
import java.util.List;

public class AdaptadorPartidas extends RecyclerView.Adapter<AdaptadorPartidas.viewHolderAdapter>{

    List<Partida> partidas;
    Context context;

    public AdaptadorPartidas(List<Partida> partidas,Context context) {
        this.partidas = partidas;
        this.context = context;
    }

    @NonNull
    @Override
    public AdaptadorPartidas.viewHolderAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_partidas,parent,false);
        AdaptadorPartidas.viewHolderAdapter holder = new AdaptadorPartidas.viewHolderAdapter(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdaptadorPartidas.viewHolderAdapter holder, int position) {
        Partida partida = partidas.get(position);
        holder.nomPartida.setText(partida.getNombre());
        if(partida.isPublica()){
            holder.iconPrivada.setVisibility(View.GONE);
        }else{
            holder.iconPrivada.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return partidas.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {

        TextView nomPartida;
        //  CircleImageView imagenPerfil;
        ImageView iconPrivada;
        private ArrayList<com.example.proyectorol.ficha.ListaClases> fichasJugador = new ArrayList<>();
        com.example.proyectorol.ficha.ListaClases ficha;

        public viewHolderAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            nomPartida = itemView.findViewById(R.id.nombrePartidaRow);
            iconPrivada = itemView.findViewById(R.id.iconPrivado);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
                    DatabaseReference ref_fichas = baseDatos.getReference("partidas")
                            .child(partidas.get(getAdapterPosition()).getIdPartida());
                    baseDatos.getReference("usuarios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            Usuario usuario=(Usuario)snapshot.child(user.getUid()).getValue(Usuario.class);
                            if(partidas.get(getAdapterPosition()).getPass().isEmpty()){
                                Intent intent = new Intent(context, Sesion.class);
                                intent.putExtra("DATOS", partidas.get(getAdapterPosition()));
                                ref_fichas.child("jugadores").child(user.getUid()).setValue(usuario);

                                //ELECCIÓN DE FICHA AL ENTRAR EN LA PARTIDA
                                FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
                                DatabaseReference ref_fichas = baseDatos.getReference("fichas");
                                ArrayList<String> nombreFichas = new ArrayList<>();
                                //Con esto se coge toda la info de la tabla
                                ref_fichas.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        AlertDialog.Builder dialogFicha = new AlertDialog.Builder(context);
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
                                                baseDatos.getReference("partidas").child(partidas.get(getAdapterPosition()).getIdPartida())
                                                        .child("jugadores").child(user.getUid()).child("ficha").setValue(ficha);
                                                intent.putExtra("FICHA",ficha);
                                                context.startActivity(intent);
                                            }
                                        }).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }else{
                                AlertDialog.Builder bulder2 = new AlertDialog.Builder(context);
                                bulder2.setTitle("Introduce la contraseña").setView(R.layout.op_introducir_pass)
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Dialog in = (Dialog)dialog;
                                                EditText txt_nombre = in.findViewById(R.id.txtPass);
                                                com.example.proyectorol.ficha.ListaClases ficha;
                                                if(txt_nombre.getText().toString().equals(partidas.get(getAdapterPosition()).getPass())){
                                                    Intent intent = new Intent(context, Sesion.class);
                                                    intent.putExtra("DATOS", partidas.get(getAdapterPosition()));
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
                                                            AlertDialog.Builder dialogFicha = new AlertDialog.Builder(context);
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
                                                                    viewHolderAdapter.this.ficha = fichasJugador.get(which);
                                                                    baseDatos.getReference("partidas").child(partidas.get(getAdapterPosition()).getIdPartida())
                                                                            .child("jugadores").child(user.getUid()).child("ficha").setValue(viewHolderAdapter.this.ficha);
                                                                    intent.putExtra("FICHA",viewHolderAdapter.this.ficha);
                                                                    context.startActivity(intent);
                                                                }
                                                            }).show();
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                        }
                                                    });

                                                }else{
                                                    Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
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
    }
}
