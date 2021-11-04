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
import com.example.proyectorol.pojos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorChatList extends RecyclerView.Adapter<AdaptadorChatList.viewHolderAdapterChatList> {

     List<Usuario> usuarioList;
     Context context;
     FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
     FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
     SharedPreferences sPref;
    public AdaptadorChatList(List<Usuario> usuarioList, Context context) {
        this.usuarioList = usuarioList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolderAdapterChatList onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chatlist,parent,false);
        return new viewHolderAdapterChatList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdaptadorChatList.viewHolderAdapterChatList holder, int position) {
       final Usuario usuarios = usuarioList.get(position);

        holder.txt_Usuario.setText(usuarios.getNombre());
        //TODO imagen

        if(usuarios.getFoto().equalsIgnoreCase("empty")){
            Picasso.get().load(R.drawable.vampirito).into(holder.img_user);
        }else{
            Picasso.get().load(usuarios.getFoto()).into(holder.img_user);
        }

        DatabaseReference ref_solicitudes = baseDatos.getReference("EstadoSolicitudes")
                                            .child(fUser.getUid())
                                            .child(usuarios.getId());

        ref_solicitudes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                if(snapshot.exists()){
                    if(estado.equals("amigos")){
                        holder.cardView.setVisibility(View.VISIBLE);
                    }else{
                        holder.cardView.setVisibility(View.GONE);
                    }
                }else{
                    holder.cardView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        final GregorianCalendar calendario = new GregorianCalendar();

        final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        DatabaseReference ref_Estado = baseDatos.getReference("estado").child(usuarios.getId());
        //Escuchamos los cambios (conectado, desconectado)
        ref_Estado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                String fecha = snapshot.child("fecha").getValue(String.class);
                String hora = snapshot.child("hora").getValue(String.class);

                if(snapshot.exists()){
                   if(estado.equalsIgnoreCase("conectado")){
                       holder.txt_Conectado.setVisibility(View.VISIBLE);
                       holder.iconoConectado.setVisibility(View.VISIBLE);
                       holder.txt_Desconectado.setVisibility(View.GONE);
                       holder.iconoDesconectado.setVisibility(View.GONE);
                   }else{
                       holder.txt_Desconectado.setVisibility(View.VISIBLE);
                       holder.iconoDesconectado.setVisibility(View.VISIBLE);
                       holder.txt_Conectado.setVisibility(View.GONE);
                       holder.iconoConectado.setVisibility(View.GONE);

                       //Consultamos la última conexión, una vez desconectado
                       if(fecha.equals(formatoFecha.format(calendario.getTime()))){
                            holder.txt_Desconectado.setText("hoy a las "+hora);
                       }else{
                           holder.txt_Desconectado.setText(fecha+" a las "+hora);
                       }
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
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
                            intent.putExtra("fotoPerfil",usuarios.getFoto());
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

    public class viewHolderAdapterChatList extends RecyclerView.ViewHolder {

        TextView txt_Usuario;
        //ImageView img_user;
        CardView cardView;
        TextView txt_Conectado, txt_Desconectado;
        ImageView iconoConectado, iconoDesconectado;
        CircleImageView img_user;
        public viewHolderAdapterChatList(@NonNull @NotNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardChatList);
            txt_Usuario = itemView.findViewById(R.id.nombreUserChatList);
            img_user = itemView.findViewById(R.id.imagenPerfilChatRow);
            txt_Conectado = itemView.findViewById(R.id.txt_conectado);
            txt_Desconectado = itemView.findViewById(R.id.txt_desconectado);
            iconoConectado = itemView.findViewById(R.id.estadoConectado);
            iconoDesconectado = itemView.findViewById(R.id.estadoDesconectado);
        }
    }
}
