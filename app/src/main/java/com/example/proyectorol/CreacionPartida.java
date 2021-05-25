package com.example.proyectorol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class CreacionPartida extends AppCompatActivity {
    private EditText nombrePartida, password;
    private Spinner numJugadores;
    private RadioGroup radioGrupo;
    private RadioButton partidaPublica, partidaPrivada;
    private String error;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_partida);
        //Atributos de  los campos de nombre partida, num jugadores y contraseña
        nombrePartida = findViewById(R.id.cNomPartida);
        numJugadores = findViewById(R.id.cJugadores);
        password = findViewById(R.id.cPassword);
        //Grupo que contiene los radio button
        radioGrupo = findViewById(R.id.radioGrupo);
        partidaPublica = findViewById(R.id.rPublico);
        partidaPrivada = findViewById(R.id.rPrivado);
        //Hacemos que esté público seleccionado por defecto
        partidaPublica.setChecked(true);

    }

    public void tipoPartida(View view) {
        //Comprobamos el evento
        boolean opcionSeleccionada = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rPublico:
                if (opcionSeleccionada && password.isEnabled()) {
                    password.setVisibility(View.INVISIBLE);
                    password.setText("");
                }
                break;
            case R.id.rPrivado:
                if (opcionSeleccionada) {
                    password.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //TODO: Longitud registros ?
    private boolean compruebaPartida() {
        boolean datosCorrectos = false;
        boolean esNombreVacio = nombrePartida.getText().toString().isEmpty();

        boolean esPasswordVacia = password.getText().toString().isEmpty();
        if (esNombreVacio) {
            error = "Debes rellenar el nombre";
        } else {
                if (partidaPrivada.isChecked() && esPasswordVacia) {
                    error = "Debes asignar una contraseña";
                } else {
                    datosCorrectos = true;
                }
        }
        return datosCorrectos;
    }

    public void crearPartida(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
        DatabaseReference ref = baseDatos.getReference("usuarios").child(user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                usuario=(Usuario)snapshot.getValue(Usuario.class);
                if (compruebaPartida()) {
                    Partida partida;
                    if(!partidaPublica.isSelected()){
                        partida = new Partida(nombrePartida.getText().toString()+"-"+user.getUid(),
                                nombrePartida.getText().toString(),
                                numJugadores.getSelectedItemPosition()+1,
                                false,
                                password.getText().toString());
                    }else{
                        partida = new Partida(nombrePartida.getText().toString()+"-"+user.getUid(),
                                nombrePartida.getText().toString(),
                                numJugadores.getSelectedItemPosition()+1,
                                true);
                    }
                    DatabaseReference ref_fichas = baseDatos.getReference("partidas");
                    ref_fichas.child(partida.getIdPartida()).setValue(partida);
                    ref_fichas.child(partida.getIdPartida()).child("jugadores").child(usuario.getId()).setValue(usuario);
                    Intent intent = new Intent(CreacionPartida.this,Sesion.class);
                    intent.putExtra("DATOS",partida);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreacionPartida.this,
                            error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
//        usuario=(Usuario)baseDatos.getReference("usuarios").child(user.getUid()).

    }
}