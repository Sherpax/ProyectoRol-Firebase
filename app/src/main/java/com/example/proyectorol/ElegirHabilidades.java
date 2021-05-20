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

import com.example.proyectorol.ficha.Habilidades.HabilidadesConocimientos;
import com.example.proyectorol.ficha.Habilidades.HabilidadesTalentos;
import com.example.proyectorol.ficha.Habilidades.HabilidadesTecnicas;
import com.example.proyectorol.ficha.ListaClases;

import java.util.HashMap;

public class ElegirHabilidades extends AppCompatActivity implements View.OnKeyListener {
    private HabilidadesConocimientos habilidadesConocimientos;
    private HabilidadesTalentos habilidadesTalentos;
    private HabilidadesTecnicas habilidadesTecnicas;
    private TextView viewPuntos;
    private final String textoPuntos = "Puntos sin gastar: ";
    private int totalPuntos = 27;
    private EditText[] arrayCampos;
    //Talentos
    private EditText campoAlerta, campoAtletismo, campoIntimidacion;
    //Tecnicas
    private EditText campoSigilo, campoArmasFuego, campoArmasMelee;
    //Conocimientos
    private EditText campoInformatica, campoMedicina, campoTecnologia;
    private int puntosSinCambio;
    private ListaClases ficha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_habilidades);
        //Asignamos los elementos de la UI a utilizar
        viewPuntos = findViewById(R.id.textoPuntosHab);
        viewPuntos.setText(textoPuntos + totalPuntos);
        //Array que contiene todos los campos para facilitar cambios simultáneos
        this.arrayCampos = new EditText[9];
        //Inicializamos los campos de los EditText
        inicializaCampos();
        //Añadimos los eventos
        inicializaEventos();
        //Instanciamos los atributos
        habilidadesConocimientos = new HabilidadesConocimientos();
        habilidadesTalentos = new HabilidadesTalentos();
        habilidadesTecnicas = new HabilidadesTecnicas();
        this.ficha = (ListaClases) getIntent().getSerializableExtra("Ficha");
        if(ficha.getListaHabilidades().size()>0){
            this.recuperarDatos();
        }
    }

    public void recuperarDatos(){
        campoAlerta.setText(Integer.toString(((HabilidadesTalentos)ficha.getListaHabilidades().get(1)).getAlerta()));
        campoAtletismo.setText(Integer.toString(((HabilidadesTalentos)ficha.getListaHabilidades().get(1)).getAtletismo()));
        campoIntimidacion.setText(Integer.toString(((HabilidadesTalentos)ficha.getListaHabilidades().get(1)).getIntimidacion()));
        campoSigilo.setText(Integer.toString(((HabilidadesTecnicas)ficha.getListaHabilidades().get(2)).getSigilo()));
        campoArmasFuego.setText(Integer.toString(((HabilidadesTecnicas)ficha.getListaHabilidades().get(2)).getArmasFuego()));
        campoArmasMelee.setText(Integer.toString(((HabilidadesTecnicas)ficha.getListaHabilidades().get(2)).getArmasMelee()));
        campoInformatica.setText(Integer.toString(((HabilidadesConocimientos)ficha.getListaHabilidades().get(0)).getInformatica()));
        campoMedicina.setText(Integer.toString(((HabilidadesConocimientos)ficha.getListaHabilidades().get(0)).getMedicina()));
        campoTecnologia.setText(Integer.toString(((HabilidadesConocimientos)ficha.getListaHabilidades().get(0)).getTecnologia()));
        totalPuntos-= ((HabilidadesTalentos)ficha.getListaHabilidades().get(1)).getAlerta()
            +((HabilidadesTalentos)ficha.getListaHabilidades().get(1)).getAtletismo()
            +((HabilidadesTalentos)ficha.getListaHabilidades().get(1)).getIntimidacion()
            +((HabilidadesTecnicas)ficha.getListaHabilidades().get(2)).getSigilo()
            +((HabilidadesTecnicas)ficha.getListaHabilidades().get(2)).getArmasFuego()
            +((HabilidadesTecnicas)ficha.getListaHabilidades().get(2)).getArmasMelee()
            +((HabilidadesConocimientos)ficha.getListaHabilidades().get(0)).getInformatica()
            +((HabilidadesConocimientos)ficha.getListaHabilidades().get(0)).getMedicina()
            +((HabilidadesConocimientos)ficha.getListaHabilidades().get(0)).getTecnologia();
        ficha.getListaHabilidades().remove(2);
        ficha.getListaHabilidades().remove(1);
        ficha.getListaHabilidades().remove(0);
        viewPuntos.setText(textoPuntos + totalPuntos);
    }

    private void inicializaCampos() {
        campoAlerta = findViewById(R.id.campoAlerta);
        campoAtletismo = findViewById(R.id.campoAtletismo);
        campoIntimidacion = findViewById(R.id.campoIntimidacion);
        campoSigilo = findViewById(R.id.campoSigilo);
        campoArmasFuego = findViewById(R.id.campoArmasFuego);
        campoArmasMelee = findViewById(R.id.campoArmasMelee);
        campoInformatica = findViewById(R.id.campoInformatica);
        campoMedicina = findViewById(R.id.campoMedicina);
        campoTecnologia = findViewById(R.id.campoTecnologia);
        //Añadimos los campos al array
        this.arrayCampos[0] = campoAlerta;
        this.arrayCampos[1] = campoAtletismo;
        this.arrayCampos[2] = campoIntimidacion;
        this.arrayCampos[3] = campoSigilo;
        this.arrayCampos[4] = campoArmasFuego;
        this.arrayCampos[5] = campoArmasMelee;
        this.arrayCampos[6] = campoInformatica;
        this.arrayCampos[7] = campoMedicina;
        this.arrayCampos[8] = campoTecnologia;
    }

    private void inicializaEventos() {
        //Eventos de pulsación tecla:
        campoAlerta.setOnKeyListener(this);
        campoAtletismo.setOnKeyListener(this);
        campoIntimidacion.setOnKeyListener(this);
        campoSigilo.setOnKeyListener(this);
        campoArmasFuego.setOnKeyListener(this);
        campoArmasMelee.setOnKeyListener(this);
        campoInformatica.setOnKeyListener(this);
        campoMedicina.setOnKeyListener(this);
        campoTecnologia.setOnKeyListener(this);
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
            case R.id.campoInformatica:
                habilidadesConocimientos.setInformatica(puntoAtributo);
                break;
            case R.id.campoMedicina:
                habilidadesConocimientos.setMedicina(puntoAtributo);
                break;
            case R.id.campoTecnologia:
                habilidadesConocimientos.setTecnologia(puntoAtributo);
                break;
            case R.id.campoArmasFuego:
                habilidadesTecnicas.setArmasFuego(puntoAtributo);
                break;
            case R.id.campoArmasMelee:
                habilidadesTecnicas.setArmasMelee(puntoAtributo);
                break;
            case R.id.campoSigilo:
                habilidadesTecnicas.setSigilo(puntoAtributo);
                break;
            case R.id.campoAlerta:
                habilidadesTalentos.setAlerta(puntoAtributo);
                break;
            case R.id.campoAtletismo:
                habilidadesTalentos.setAtletismo(puntoAtributo);
                break;
            case R.id.campoIntimidacion:
                habilidadesTalentos.setIntimidacion(puntoAtributo);
                break;
        }
    }

    public void aplicar(View view) {
        //TODO
        //Comprobamos si ha vuelto a esta pantalla despúes de haber estado en otro activity
        //Limpiamos para que siempre haya el numero exacto de clases
        ficha.getListaHabilidades().add(habilidadesConocimientos);
        ficha.getListaHabilidades().add(habilidadesTalentos);
        ficha.getListaHabilidades().add(habilidadesTecnicas);

        Intent intent = new Intent(this, ElegirVirtudes.class);
        intent.putExtra("Ficha",ficha);
        startActivity(intent);
    }

    public void reiniciar(View view) {
        ficha.getListaHabilidades().remove(habilidadesConocimientos);
        ficha.getListaHabilidades().remove(habilidadesTalentos);
        ficha.getListaHabilidades().remove(habilidadesTecnicas);
        int puntosSinCambio = 0;

        for (EditText txt : this.arrayCampos) {
            puntosSinCambio = Integer.parseInt(txt.getText().toString());
            restableceCampo(txt);
            actualizaContadorPuntos(puntosSinCambio, 0);
        }
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

    public void volver(View view) {
        Intent intent = new Intent(this, ElegirAtributos.class);
        intent.putExtra("Ficha",ficha);
        startActivity(intent);
    }
}