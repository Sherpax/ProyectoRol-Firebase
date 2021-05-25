package com.example.proyectorol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectorol.R;
import com.example.proyectorol.pojos.ChatGrupo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdaptadorChatGrupal extends RecyclerView.Adapter<AdaptadorChatGrupal.AdaptadorPruebaHolder> {

    Context context;
    List<ChatGrupo> mensajes;
    DatabaseReference ref;
    final static int MENSAJEIZQUIERDA=0;
    final static int MENSAJEDERECHA=1;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public AdaptadorChatGrupal(Context context, List<ChatGrupo> mensajes, DatabaseReference ref) {
        this.context = context;
        this.mensajes = mensajes;
        this.ref = ref;
    }


    @NonNull
    @NotNull
    @Override
    public AdaptadorPruebaHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        TextView tv;
        if(viewType==MENSAJEDERECHA){
            view= LayoutInflater.from(context).inflate(R.layout.chat_mensaje_derecha,parent,false);
            view.findViewById(R.id.txt_fecha).setVisibility(View.GONE);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_mensaje_izquierda, parent, false);
            view.findViewById(R.id.txt_fecha).setVisibility(View.GONE);
        }
        return new AdaptadorPruebaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdaptadorPruebaHolder holder, int position) {
        ChatGrupo mensaje = mensajes.get(position);
        if(mensaje.isMensajeDados()){
            holder.tv.setText(user.getDisplayName() +mensaje.getMensaje());
            if(mensaje.getUid().equals(user.getUid())){
                holder.tv.setText("Tú has "+mensaje.getMensaje());
            }else{
                holder.tv.setText(mensaje.getNombre()+" ha "+mensaje.getMensaje());
            }
        }else{
            if(mensaje.getUid().equals(user.getUid())){
                holder.tv.setText("Tú: "+mensaje.getMensaje());
            }else{
                holder.tv.setText(mensaje.getNombre()+": "+mensaje.getMensaje());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    @Override
    public int getItemViewType(int position) {
        int tipo;
        if(user.getUid().equals(mensajes.get(position).getUid())){
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
