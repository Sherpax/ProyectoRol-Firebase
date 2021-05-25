package com.example.proyectorol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.proyectorol.pojos.Partida;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreacionPartida extends AppCompatActivity {
    private EditText nombrePartida, password;
    private Spinner numJugadores;
    private RadioGroup radioGrupo;
    private RadioButton partidaPublica, partidaPrivada;
    private String error;

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
        if (compruebaPartida()) {
            Partida partida = new Partida(this.nombrePartida.getText().toString()+"-"+user.getUid(),
                    this.nombrePartida.getText().toString(),
                    this.numJugadores.getSelectedItemPosition()+1,
                    this.partidaPublica.isSelected());
            if(this.partidaPrivada.isSelected()){
                partida.setPass(this.password.getText().toString());
            }
            FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
            DatabaseReference ref_fichas = baseDatos.getReference("partidas");
            ref_fichas.child(partida.getIdPartida()).setValue(partida);
            Intent intent = new Intent(this,Sesion.class);
            intent.putExtra("DATOS",partida);
            startActivity(intent);
        } else {
            Toast.makeText(this,
                    this.error, Toast.LENGTH_SHORT).show();
        }
    }
}