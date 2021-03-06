package com.example.proyectorol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectorol.adapters.AdaptadorChats;
import com.example.proyectorol.pojos.Chat;
import com.example.proyectorol.pojos.Estado;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Mensajes extends AppCompatActivity {
    CircleImageView img_user;
    TextView userName;
    ImageView ic_conectado, ic_desconectado;
    EditText txt_Mensaje;
    ImageButton botonEnviarMensaje;
    SharedPreferences sPref;
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
    DatabaseReference ref_estados = baseDatos.getReference("estado").child(fUser.getUid());
    DatabaseReference ref_chat = baseDatos.getReference("chats");


    //Chat global referencias
    String id_chat_global;
    boolean estaConectado;

    //Rv
    RecyclerView rv_chats;
    AdaptadorChats adaptadorChats;
    ArrayList<Chat> listaChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mensajes);

        userName = findViewById(R.id.nombreUser);
        ic_conectado = findViewById(R.id.icon_conectado);
        ic_desconectado = findViewById(R.id.icon_desconectado);
        txt_Mensaje = findViewById(R.id.txt_Mensaje);
        botonEnviarMensaje = findViewById(R.id.enviarMensaje);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sPref = getApplicationContext().getSharedPreferences("usuario_sp",MODE_PRIVATE);

        String usuario = getIntent().getExtras().getString("nombre");
        String id_user = getIntent().getExtras().getString("id_user");
        id_chat_global = getIntent().getExtras().getString("id_unico");

        userName.setText(usuario);

        final String id_user_sp = sPref.getString("usuario_sp","");
        final DatabaseReference ref = baseDatos.getReference("estado").child(id_user_sp).child("chatcon");
        final DatabaseReference ref_chat = baseDatos.getReference("chats");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String chatcon = snapshot.getValue(String.class);
                //Muestra si est?? conectado (en la misma activity de mensajes que el otro usuario)
                //Si no, est?? en otra activity (hablando con otro usuario, etc)
                if(snapshot.exists()){
                    if(chatcon.equals(fUser.getUid())){
                        estaConectado = true;
                        ic_conectado.setVisibility(View.VISIBLE);
                        ic_desconectado.setVisibility(View.GONE);
                        actualizaMensajeVisto();
                    }else{
                        estaConectado = false;
                        ic_conectado.setVisibility(View.GONE);
                        ic_desconectado.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Para el bot??n enviar
        botonEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = txt_Mensaje.getText().toString();
                //Comprobamos que el mensaje a enviar no est?? vac??o
                if(!mensaje.isEmpty()){
                    final GregorianCalendar calendario = new GregorianCalendar();
                    final SimpleDateFormat formatTiempo = new SimpleDateFormat("HH:mm");
                    final SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");

                    String idPush = ref_chat.push().getKey();

                    if(estaConectado){
                        Chat chatMensaje = new Chat(
                                idPush,
                                fUser.getUid(),
                                id_user,mensaje,
                                formatFecha.format(calendario.getTime()),
                                formatTiempo.format(calendario.getTime()),
                                estaConectado);
                        ref_chat.child(id_chat_global).child(idPush).setValue(chatMensaje);
                        Toast.makeText(Mensajes.this,
                                "Mensaje enviado", Toast.LENGTH_SHORT).show();
                        //Reiniciamos el EditText para enviar el siguiente mensaje
                        txt_Mensaje.setText("");
                    }else{
                        Chat chatMensaje = new Chat(
                                idPush,
                                fUser.getUid(),
                                id_user,mensaje,
                                formatFecha.format(calendario.getTime()),
                                formatTiempo.format(calendario.getTime()),
                                estaConectado);
                        ref_chat.child(id_chat_global).push().setValue(chatMensaje);
                        Toast.makeText(Mensajes.this,
                                "Mensaje enviado", Toast.LENGTH_SHORT).show();
                        //Reiniciamos el EditText para enviar el siguiente mensaje
                        txt_Mensaje.setText("");
                    }
                }
            }
        });

        rv_chats = findViewById(R.id.rv1);
        rv_chats.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rv_chats.setLayoutManager(layoutManager);

        listaChats = new ArrayList<>();
        adaptadorChats = new AdaptadorChats(listaChats,this);
        rv_chats.setAdapter(adaptadorChats);

        LeerMensajes();

    }

    private void actualizaMensajeVisto() {
        ref_chat.child(id_chat_global).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    listaChats.removeAll(listaChats);
                    for (DataSnapshot dShot : snapshot.getChildren()){
                        Chat chat = dShot.getValue(Chat.class);
                        if(estaConectado) chat.setVisto(true);
                        listaChats.add(chat);
                    }
                    adaptadorChats.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    //M??todo que lee los mensajes
    private void LeerMensajes() {
        ref_chat.child(id_chat_global).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    listaChats.removeAll(listaChats);
                    for (DataSnapshot dShot : snapshot.getChildren()){
                        Chat chat = dShot.getValue(Chat.class);
                        if(estaConectado) chat.setVisto(true);
                        listaChats.add(chat);
                        setScroll();
                    }
                    adaptadorChats.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void setScroll() {
        rv_chats.scrollToPosition(adaptadorChats.getItemCount()-1);
    }

    @Override
    protected void onResume(){
        super.onResume();
        estadoUsuario("conectado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        estadoUsuario("desconectado");
        //Necesitamos generar una fecha de la ??ltima conexi??n
        getUltimaFecha();
    }
    //Obtenemos la ??ltima fecha (horas,min)
    private void getUltimaFecha() {
        final GregorianCalendar calendario = new GregorianCalendar();
        final SimpleDateFormat tiempo = new SimpleDateFormat(("HH:mm"));
        final SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");

        ref_estados.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ref_estados.child("fecha").setValue(fecha.format(calendario.getTime()));
                ref_estados.child("hora").setValue(tiempo.format(calendario.getTime()));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    //Para modificar los estados del usuario
    private void estadoUsuario(String estado){
        ref_estados.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                final String id_user_sp = sPref.getString("usuario_sp","");
                Estado est = new Estado(estado,"","",id_user_sp);
                ref_estados.setValue(est);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}