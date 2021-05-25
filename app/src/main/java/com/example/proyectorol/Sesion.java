package com.example.proyectorol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.proyectorol.pojos.Partida;
import com.example.proyectorol.pojos.Usuario;
import com.example.proyectorol.adapters.AdaptadorChatPrueba;
import com.example.proyectorol.pojos.ChatGrupo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sesion extends AppCompatActivity {

    Partida partida;
    FirebaseDatabase database;
    DatabaseReference ref;
    AdaptadorChatPrueba adaptadorChatPrueba;
    final FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
    List<ChatGrupo> mensajes;
    RecyclerView rv;
    EditText editText;
    ImageButton imageButtonEnviar;
    ImageButton imageButtonDados;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
        partida = (Partida) getIntent().getSerializableExtra("DATOS");
        init();
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        rv=findViewById(R.id.rvPrueba);
        editText = findViewById(R.id.etMensaje);
        imageButtonEnviar = findViewById(R.id.btnEnviarGrupo);
        imageButtonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty()){
                    ChatGrupo nuevo = new ChatGrupo(editText.getText().toString(),u.getDisplayName(),u.getUid());
                    editText.setText("");
                    ref.push().setValue(nuevo);
                }
            }
        });
        imageButtonDados = findViewById(R.id.btnDados);
        imageButtonDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder bulder2 = new AlertDialog.Builder(Sesion.this);
                bulder2.setTitle("Tirar Dados").setView(R.layout.op_cambiar_nombe)
                        .setMessage("Introduce cuantos dados quieres lanzar")
                        .setPositiveButton("Lanzar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dialog in = (Dialog)dialog;
                                int[] dados;
                                EditText txt_nombre = in.findViewById(R.id.txtcambiaNombre);
                                Random rand = new Random();
                                String mensajeDados = "Resultados de la tirada: ";
                                for(int i=0;i<Integer.parseInt(txt_nombre.getText().toString());i++){
                                    if(i<Integer.parseInt(txt_nombre.getText().toString())-1){
                                        mensajeDados+=(rand.nextInt(10)+1)+", ";
                                    }else{
                                        mensajeDados+=(rand.nextInt(10)+1);
                                    }
                                }
                                mensajeDados+="\b\b";
                                ChatGrupo nuevo = new ChatGrupo(mensajeDados,u.getDisplayName(),u.getUid());
                                editText.setText("");
                                ref.push().setValue(nuevo);
                                dialog.cancel();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        });
        mensajes= new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ref =database.getReference("partidas").child(partida.getIdPartida()).child("chat");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                ChatGrupo mensaje = snapshot.getValue(ChatGrupo.class);
                mensaje.setKey(snapshot.getKey());
                mensaje.setUid(u.getUid());
                mensajes.add(mensaje);
                monstrarMensajes(mensajes);
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                ChatGrupo mensaje = snapshot.getValue(ChatGrupo.class);
                mensaje.setKey(snapshot.getKey());
                List<ChatGrupo> nuevosMensajes = new ArrayList<>();
                for(ChatGrupo m : mensajes){
                    if(m.getKey().equals(mensaje.getKey())){
                        nuevosMensajes.add(mensaje);
                    }else{
                        nuevosMensajes.add(m);
                    }
                }
                mensajes=nuevosMensajes;
                monstrarMensajes(mensajes);
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                //NO PERMITIMOS BORRAR MENSAJES
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void monstrarMensajes(List<ChatGrupo> mensajes) {
        rv.setLayoutManager(new LinearLayoutManager(Sesion.this));
        adaptadorChatPrueba = new AdaptadorChatPrueba(Sesion.this,mensajes,ref);
        rv.setAdapter(adaptadorChatPrueba);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mensajes=new ArrayList<>();
    }
}