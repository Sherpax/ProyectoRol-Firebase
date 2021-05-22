package com.example.proyectorol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.proyectorol.pojos.Usuario;
import com.example.proyectorol.prueba.AdaptadorChatPrueba;
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

public class chatGrupalPrueba extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;
    AdaptadorChatPrueba adaptadorChatPrueba;
    Usuario u;
    List<ChatGrupo> mensajes;
    public static String uid;
    RecyclerView rv;
    EditText editText;
    ImageButton imageButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_grupal_prueba);
        init();
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        u= new Usuario();
        rv=findViewById(R.id.rvPrueba);
        editText = findViewById(R.id.etMensaje);
        imageButton = findViewById(R.id.btnPrueba);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty()){
                    ChatGrupo nuevo = new ChatGrupo(editText.getText().toString(),u.getNombre(),u.getId());
                    editText.setText("");
                    ref.push().setValue(nuevo);
                }
            }
        });
        mensajes= new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        u.setId(currentUser.getUid());
        u.setNombre(currentUser.getDisplayName());
        chatGrupalPrueba.uid=u.getId();
        ref =database.getReference("chatgrupal");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                ChatGrupo mensaje = snapshot.getValue(ChatGrupo.class);
                mensaje.setKey(snapshot.getKey());
                mensaje.setUid(u.getId());
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
        rv.setLayoutManager(new LinearLayoutManager(chatGrupalPrueba.this));
        adaptadorChatPrueba = new AdaptadorChatPrueba(chatGrupalPrueba.this,mensajes,ref);
        rv.setAdapter(adaptadorChatPrueba);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mensajes=new ArrayList<>();
    }
}