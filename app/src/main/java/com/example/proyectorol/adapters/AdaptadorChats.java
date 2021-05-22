package com.example.proyectorol.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectorol.R;
import com.example.proyectorol.pojos.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class AdaptadorChats extends RecyclerView.Adapter<AdaptadorChats.viewHolderAdapter> {

    ArrayList<Chat> listaChats;
    Context context;
    boolean esSoloDerecha = false;
    public static final int MENSAJE_IZQUIERDA = 0;
    public static final int MENSAJE_DERECHA = 1;

    FirebaseUser fUser;

    public AdaptadorChats(ArrayList<Chat> listaChats, Context context) {
        this.listaChats = listaChats;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == MENSAJE_DERECHA){
             view = LayoutInflater.from(context).inflate(R.layout.chat_mensaje_derecha,parent,false);
        }else{
             view = LayoutInflater.from(context).inflate(R.layout.chat_mensaje_izquierda,parent,false);
        }
        return new AdaptadorChats.viewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdaptadorChats.viewHolderAdapter holder, int position) {

        Chat chats = listaChats.get(position);

        holder.txt_mensaje.setText(chats.getMensaje());

        if(esSoloDerecha){
            if (chats.isVisto()){
                holder.img_entregado.setVisibility(View.GONE);
                holder.img_visto.setVisibility(View.VISIBLE);
            }else{
                holder.img_entregado.setVisibility(View.VISIBLE);
                holder.img_visto.setVisibility(View.GONE);
            }
        }

        final GregorianCalendar calendario = new GregorianCalendar();

        final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        //TODO: Peta
        if(chats.getFecha().equals(formatoFecha.format(calendario.getTime()))){
//            holder.txt_fecha.setText("hoy "+chats.getHora());
        }else{
//            holder.txt_fecha.setText(chats.getFecha()+" "+chats.getHora());
        }

    }

    @Override
    public int getItemCount() {
        return listaChats.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder{
        TextView txt_mensaje, txt_fecha;
        ImageView img_entregado, img_visto;

        public viewHolderAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            txt_mensaje = itemView.findViewById(R.id.mensaje);
//            txt_fecha = itemView.findViewById(R.id.txt_fecha);
            img_entregado = itemView.findViewById(R.id.icon_entregado);
            img_visto = itemView.findViewById(R.id.icon_evisto);
        }
    }

    //Comprobamos aquí qué posición tiene el mensaje (izquierda o derecha)
    //Teniendo en cuenta si lo enviamos nosotros o lo recibimos
    @Override
    public int getItemViewType(int position) {
        int tipoMensaje = -1;
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(listaChats.get(position).getEnvia().equals(fUser.getUid())){
            esSoloDerecha = true;
            tipoMensaje = MENSAJE_DERECHA;
        }else{
            esSoloDerecha = false;
            tipoMensaje = MENSAJE_IZQUIERDA;
        }
        return tipoMensaje;
    }
}
