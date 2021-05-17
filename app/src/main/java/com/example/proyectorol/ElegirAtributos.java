package com.example.proyectorol;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectorol.ficha.Atributos.AtributosFisicos;
import com.example.proyectorol.ficha.Atributos.AtributosMentales;
import com.example.proyectorol.ficha.Atributos.AtributosSociales;
import com.example.proyectorol.ficha.ListaClases;

//Autor: Manuel Vázquez del Pino
public class ElegirAtributos extends AppCompatActivity implements View.OnKeyListener {
    private AtributosFisicos atributosFisicos;
    private AtributosSociales atributosSociales;
    private AtributosMentales atributosMentales;
    private TextView viewPuntos;
    private final String textoPuntos = "Puntos sin gastar: ";
    private int totalPuntos = 15;
    private EditText[] arrayCampos;
    private EditText campoFuerza, campoDestreza, campoResistencia;
    private EditText campoCarisma, campoManipulacion, campoApariencia;
    private EditText campoPercepcion, campoInteligencia, campoAstucia;
    private int puntosSinCambio;
    private ListaClases ficha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atributos);
        //Asignamos los elementos de la UI a utilizar
        viewPuntos = findViewById(R.id.textoPuntos);
        viewPuntos.setText(textoPuntos + totalPuntos);
        //Array que contiene todos los campos para facilitar cambios simultáneos
        this.arrayCampos = new EditText[9];
        //Inicializamos los campos de los EditText
        inicializaCampos();
        //Añadimos los eventos
        inicializaEventos();
        //Instanciamos los atributos
        atributosFisicos = new AtributosFisicos();
        atributosSociales = new AtributosSociales();
        atributosMentales = new AtributosMentales();
        this.ficha = (ListaClases) getIntent().getSerializableExtra("Ficha");
    }

    private void inicializaCampos() {
        campoFuerza = findViewById(R.id.campoFuerza);
        campoDestreza = findViewById(R.id.campoDesttreza);
        campoResistencia = findViewById(R.id.campoResistencia);
        campoCarisma = findViewById(R.id.campoCarisma);
        campoManipulacion = findViewById(R.id.campoManipulacion);
        campoApariencia = findViewById(R.id.campoApariencia);
        campoPercepcion = findViewById(R.id.campoPercepcion);
        campoInteligencia = findViewById(R.id.campoInteligencia);
        campoAstucia = findViewById(R.id.campoAstucia);
        //Añadimos los campos al array
        this.arrayCampos[0] = campoFuerza;
        this.arrayCampos[1] = campoDestreza;
        this.arrayCampos[2] = campoResistencia;
        this.arrayCampos[3] = campoCarisma;
        this.arrayCampos[4] = campoManipulacion;
        this.arrayCampos[5] = campoApariencia;
        this.arrayCampos[6] = campoPercepcion;
        this.arrayCampos[7] = campoInteligencia;
        this.arrayCampos[8] = campoAstucia;
    }

    private void inicializaEventos() {
        //Eventos de pulsación tecla:
        campoFuerza.setOnKeyListener(this);
        campoDestreza.setOnKeyListener(this);
        campoResistencia.setOnKeyListener(this);
        campoCarisma.setOnKeyListener(this);
        campoManipulacion.setOnKeyListener(this);
        campoApariencia.setOnKeyListener(this);
        campoPercepcion.setOnKeyListener(this);
        campoInteligencia.setOnKeyListener(this);
        campoAstucia.setOnKeyListener(this);

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

    //Restablece un campo al valor original: rojo, con valor = 0
    private void restableceCampo(EditText txt) {
        txt.setTextColor(Color.rgb(253, 5, 5));
        txt.setText("0");
    }

    //Este método establece el nuevo valor del atributo
    private void estableceAtributo(EditText campoAtributo, byte puntoAtributo) {

        switch (campoAtributo.getId()) {
            case R.id.campoFuerza:
                atributosFisicos.setFuerza(puntoAtributo);
                break;
            case R.id.campoResistencia:
                atributosFisicos.setResistencia(puntoAtributo);
                break;
            case R.id.campoDesttreza:
                atributosFisicos.setDestreza(puntoAtributo);
                break;
            case R.id.campoCarisma:
                atributosSociales.setCarisma(puntoAtributo);
                break;
            case R.id.campoManipulacion:
                atributosSociales.setManipulacion(puntoAtributo);
                break;
            case R.id.campoApariencia:
                atributosSociales.setApariencia(puntoAtributo);
                break;
            case R.id.campoPercepcion:
                atributosMentales.setPercepcion(puntoAtributo);
                break;
            case R.id.campoInteligencia:
                atributosMentales.setInteligencia(puntoAtributo);
                break;
            case R.id.campoAstucia:
                atributosMentales.setAstucia(puntoAtributo);
                break;
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
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
                        Toast.makeText(this,
                                "Ya no puedes gastar más puntos", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                actualizaContadorPuntos(puntosSinCambio, 0);
                restableceCampo(txt);
            }
        }
        return true;
    }

    //Reinicia todos los atributos a 0
    //Incluir alertDialog?
    //TODO: Reiniciar puntos
    public void reiniciar(View view) {
        ficha.getListaAtributos().remove(atributosFisicos);
        ficha.getListaAtributos().remove(atributosMentales);
        ficha.getListaAtributos().remove(atributosSociales);
        int puntosSinCambio = 0;

        for (EditText txt : this.arrayCampos) {
            puntosSinCambio = Integer.parseInt(txt.getText().toString());
            restableceCampo(txt);
            actualizaContadorPuntos(puntosSinCambio, 0);
        }
        atributosFisicos.setFuerza((byte) 0);
        atributosFisicos.setResistencia((byte) 0);
        atributosFisicos.setDestreza((byte) 0);
        atributosSociales.setCarisma((byte) 0);
        atributosSociales.setApariencia((byte) 0);
        atributosSociales.setManipulacion((byte) 0);
        atributosMentales.setAstucia((byte) 0);
        atributosMentales.setInteligencia((byte) 0);
        atributosMentales.setPercepcion((byte) 0);

        Toast.makeText(this,
                "Los atributos han sido restablecidos", Toast.LENGTH_SHORT).show();
    }

    //Añadimos los atributos a la lista estática (se mantiene en memoria)
    public void aplicar(View view) {
        //TODO: Preguntar Ángel
        //Comprobamos si ha vuelto a esta pantalla despúes de haber estado en otro activity
        //Limpiamos para que siempre haya el numero exacto de clases
        ficha.getListaAtributos().add(atributosFisicos);
        ficha.getListaAtributos().add(atributosMentales);
        ficha.getListaAtributos().add(atributosSociales);

        Intent intent = new Intent(this, ElegirHabilidades.class);
        intent.putExtra("Ficha",ficha);
        startActivity(intent);
    }
}

