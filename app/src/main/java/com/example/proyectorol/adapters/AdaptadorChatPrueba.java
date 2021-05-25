package com.example.proyectorol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectorol.R;
import com.example.proyectorol.Sesion;
import com.example.proyectorol.pojos.ChatGrupo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdaptadorChatPrueba extends RecyclerView.Adapter<AdaptadorChatPrueba.AdaptadorPruebaHolder> {

    Context context;
    List<ChatGrupo> mensajes;
    DatabaseReference ref;
    final static int MENSAJEIZQUIERDA=0;
    final static int MENSAJEDERECHA=1;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public AdaptadorChatPrueba(Context context, List<ChatGrupo> mensajes, DatabaseReference ref) {
        this.context = context;
        this.mensajes = mensajes;
        this.ref = ref;
    }


    @NonNull
    @NotNull
    @Override
    public AdaptadorPruebaHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==MENSAJEDERECHA){
            view= LayoutInflater.from(context).inflate(R.layout.chat_mensaje_derecha,parent,false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_mensaje_izquierda, parent, false);
        }
        return new AdaptadorPruebaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdaptadorPruebaHolder holder, int position) {
        ChatGrupo mensaje = mensajes.get(position);
        if(mensaje.getNombre().equals(user.getDisplayName())){
            holder.tv.setText("Tú: "+mensaje.getMensaje());
        }else{
            holder.tv.setText(mensaje.getNombre()+": "+mensaje.getMensaje());
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    @Override
    public int getItemViewType(int position) {
        int tipo;
        if(user.getDisplayName().equals(mensajes.get(position).getNombre())){
            tipo=MENSAJEDERECHA;
        }else{
            tipo=MENSAJEIZQUIERDA;
        }
        return tipo;
    }

    public class AdaptadorPruebaHolder extends RecyclerView.ViewHolder{

        TextView tv;
        public AdaptadorPruebaHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.mensaje);
        }

    }
}
