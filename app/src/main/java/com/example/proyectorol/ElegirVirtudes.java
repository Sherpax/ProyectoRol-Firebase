package com.example.proyectorol;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectorol.Ficha.Virtudes.Virtudes;

public class ElegirVirtudes extends AppCompatActivity implements View.OnKeyListener {
    private Virtudes virtudes;
    private TextView viewPuntos;
    private final String textoPuntos = "Puntos sin gastar: ";
    private int totalPuntos = 7;
    private EditText campoConciencia, campoAutocontrol, campoCoraje;
    private int puntosSinCambio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_virtudes);
        //Asignamos los elementos de la UI a utilizar
        viewPuntos = findViewById(R.id.textoPuntosVirtud);
        viewPuntos.setText(textoPuntos + totalPuntos);
        //Inicializamos los campos de los EditText
        campoConciencia = findViewById(R.id.campoConciencia);
        campoAutocontrol = findViewById(R.id.campoAutocontrol);
        campoCoraje = findViewById(R.id.campoCoraje);
        //Añadimos los eventos
        //Eventos de pulsación tecla:
        campoConciencia.setOnKeyListener(this);
        campoAutocontrol.setOnKeyListener(this);
        campoCoraje.setOnKeyListener(this);
        //Instanciamos los atributos
        virtudes = new Virtudes();
    }

    private void actualizaContadorPuntos(int numAntes, int numNuevo) {
        //Si el nuevo número es mayor que el introducido antes, se resta del total
        if (numNuevo >= numAntes) {
            totalPuntos -= (numNuevo - numAntes);
        } else {
            //Si el nuevo número es menor que el introducido antes, se suma al total
            totalPuntos += (numAntes - numNuevo);
        }
        viewPuntos.setText(textoPuntos + totalPuntos);
    }

    //Este método establece el nuevo valor del atributo
    private void estableceAtributo(EditText campoAtributo, byte puntoAtributo) {

        switch (campoAtributo.getId()) {
            case R.id.campoInformatica:
                virtudes.setAutocontrol(puntoAtributo);
                break;
            case R.id.campoMedicina:
                virtudes.setConciencia(puntoAtributo);
                break;
            case R.id.campoTecnologia:
                virtudes.setCoraje(puntoAtributo);
                break;
        }
    }

    //Restablece un campo al valor original: rojo, con valor = 0
    private void restableceCampo(EditText txt) {
        txt.setTextColor(Color.rgb(253, 5, 5));
        txt.setText("0");
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (view instanceof EditText) {
            EditText txt = (EditText) view;
            int puntosConCambio = 0;
            if (!txt.getText().toString().isEmpty()) {
                char num = keyEvent.getNumber();
                //Comprobamos que solo se puedan meter números entre 1 y 5
                if (txt.hasFocus() && num >= '0' && num <= '5') {
                    //Comprobamos que el número introducido no haga que sea menor de 0 sobre el total de puntos
                    if (totalPuntos - (Byte.parseByte(String.valueOf(num)) - Byte.parseByte(txt.getText().toString())) >= 0) {
                        //Establecemos los atributos
                        estableceAtributo(txt, Byte.parseByte(String.valueOf(num)));
                        //Hacemos el cálculo de puntos totales restantes
                        puntosSinCambio = Integer.parseInt(txt.getText().toString());
                        txt.setText(String.valueOf(keyEvent.getNumber()));
                        puntosConCambio = Integer.parseInt(txt.getText().toString());
                        //Actualizamos el contador
                        actualizaContadorPuntos(puntosSinCambio, puntosConCambio);
                        if (!txt.getText().toString().equals("0") && !txt.getText().toString().isEmpty()) {
                            txt.setTextColor(Color.GREEN);
                        } else {
                            restableceCampo(txt);
                        }
                    } else {
                        Toast.makeText(this, "Ya no puedes gastar más puntos", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                actualizaContadorPuntos(puntosSinCambio, 0);
                restableceCampo(txt);
            }
        }
        return true;
    }
}
