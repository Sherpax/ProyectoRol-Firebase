package com.example.proyectorol.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectorol.Mensajes;
import com.example.proyectorol.R;
import com.example.proyectorol.pojos.Solicitudes;
import com.example.proyectorol.pojos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorJugadores extends RecyclerView.Adapter<AdaptadorJugadores.viewHolderAdapter> {

     List<Usuario> usuarioList;
     Context context;
     FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
     FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
     SharedPreferences sPref;
    public AdaptadorJugadores(List<Usuario> usuarioList, Context context) {
        this.usuarioList = usuarioList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_usuarios,parent,false);
        viewHolderAdapter holder = new viewHolderAdapter(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdaptadorJugadores.viewHolderAdapter holder, int position) {
        Usuario usuarios = usuarioList.get(position);

        //holder.imagenPerfil.setImageResource(usuarios.getFoto()); //TOOD Cambiar
        holder.nomUser.setText(usuarios.getNombre());

        //Esto lo oculta si aparecemos nosotros mismos, se puede aplicar para ocultar gente que ya seamos amigos etc

        if(usuarios.getId().equals(fUser.getUid())){
            holder.cardView.setVisibility(View.GONE);
        }else{
            holder.cardView.setVisibility(View.VISIBLE);
        }

        //Para ir cambiado los estados de los botones
        DatabaseReference ref_botones = baseDatos.getReference("EstadoSolicitudes")
                .child(fUser.getUid());

        ref_botones.child(usuarios.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                if(snapshot.exists()){
                    if(estado.equalsIgnoreCase("enviado")){
                        holder.botEnviado.setVisibility(View.VISIBLE);
                        holder.botAniadir.setVisibility(View.GONE);
                        holder.botAceptarSolicitud.setVisibility(View.GONE);
                        holder.botAmigo.setVisibility(View.GONE);
                        holder.barraProgreso.setVisibility(View.GONE);
                    }else{
                        if(estado.equalsIgnoreCase("amigos")){
                            holder.botAmigo.setVisibility(View.VISIBLE);
                            holder.botEnviado.setVisibility(View.GONE);
                            holder.botAniadir.setVisibility(View.GONE);
                            holder.botAceptarSolicitud.setVisibility(View.GONE);
                            holder.barraProgreso.setVisibility(View.GONE);
                        }else{
                            if(estado.equalsIgnoreCase("solicitudPendiente")) {
                                holder.botAceptarSolicitud.setVisibility(View.VISIBLE);
                                holder.botAmigo.setVisibility(View.GONE);
                                holder.botEnviado.setVisibility(View.GONE);
                                holder.botAniadir.setVisibility(View.GONE);
                                holder.barraProgreso.setVisibility(View.GONE);
                            }
                        }
                    }
                }else{
                        holder.botAniadir.setVisibility(View.VISIBLE);
                        holder.botAceptarSolicitud.setVisibility(View.GONE);
                        holder.botAmigo.setVisibility(View.GONE);
                        holder.botEnviado.setVisibility(View.GONE);
                        holder.barraProgreso.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Evento al pulsar el botón añadir amigo
        holder.botAniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final DatabaseReference db_ref = baseDatos.getReference("EstadoSolicitudes")
                                                .child(fUser.getUid());

                db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        Solicitudes sol = new Solicitudes("enviado","");

                            db_ref.child(usuarios.getId()).setValue(sol);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                final DatabaseReference db_ref2 = baseDatos.getReference("EstadoSolicitudes").
                        child(usuarios.getId());

                db_ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Solicitudes sol = new Solicitudes("solicitudPendiente","");
                            db_ref2.child(fUser.getUid()).setValue(sol);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                DatabaseReference contadorSolicitud = baseDatos.getReference("contador")
                        .child(usuarios.getId());

                contadorSolicitud.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Integer valor = snapshot.getValue(Integer.class);
                            if(valor == 0){
                                contadorSolicitud.setValue(1);
                            }else{
                                contadorSolicitud.setValue(++valor);
                            }
                        }else{
                            contadorSolicitud.setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });

        //Así se hacen amigos, aquí están las referencias, véease que va al revés Uid, Id (uno "señala" a otro)

        holder.botAceptarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idchat = ref_botones.push().getKey();

                final DatabaseReference db_ref = baseDatos.getReference("EstadoSolicitudes")
                        .child(usuarios.getId())
                        .child(fUser.getUid());

                db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Solicitudes sol = new Solicitudes("amigos",idchat);
                        db_ref.setValue(sol);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                final DatabaseReference db_ref2 = baseDatos.getReference("EstadoSolicitudes")
                        .child(fUser.getUid())
                        .child(usuarios.getId());

                db_ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Solicitudes sol = new Solicitudes("amigos",idchat);
                        db_ref2.setValue(sol);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });

        holder.botAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sPref = v.getContext().getSharedPreferences("usuario_sp",Context.MODE_PRIVATE);

                final SharedPreferences.Editor editor = sPref.edit();

                final DatabaseReference db_ref = baseDatos.getReference("EstadoSolicitudes")
                        .child(fUser.getUid())
                        .child(usuarios.getId())
                        .child("idchat");

                db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String id_unico = snapshot.getValue(String.class);
                        if(snapshot.exists()){
                            Intent intent = new Intent(v.getContext(), Mensajes.class);
                            intent.putExtra("nombre",usuarios.getNombre());
                            //intent.putExtra("img_user",usuarios.getFoto());
                            intent.putExtra("id_user",usuarios.getId());
                            intent.putExtra("id_unico",id_unico);
                            editor.putString("usuario_sp",usuarios.getId());
                            editor.apply();
                            v.getContext().startActivity(intent);

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
    public int getItemCount() {
        return usuarioList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {

        TextView nomUser;
      //  CircleImageView imagenPerfil;
        CardView cardView;
        Button botAniadir, botEnviado, botAceptarSolicitud, botAmigo;
        ProgressBar barraProgreso;

        public viewHolderAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            nomUser = itemView.findViewById(R.id.nombreUserRow);
           // imagenPerfil = itemView.findViewById(R.id.imagenPerfilRow);
            cardView = itemView.findViewById(R.id.cardUserRow);
            botAniadir = itemView.findViewById(R.id.botAgregar);
            botEnviado = itemView.findViewById(R.id.botEnviado);
            botAceptarSolicitud = itemView.findViewById(R.id.botSolicitudPendiente);
            botAmigo = itemView.findViewById(R.id.botAmistades);
            barraProgreso = itemView.findViewById(R.id.barraProgreso);
        }
    }
}
