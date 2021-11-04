package com.example.proyectorol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectorol.adapters.Adaptador;
import com.example.proyectorol.pojos.Estado;
import com.example.proyectorol.pojos.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class OpcionesUsuario extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
    private DatabaseReference ref_usuario = baseDatos.getReference("usuarios").child(user.getUid()); //Esto nos permite controlar las referencias al usuario por ID
    private DatabaseReference ref_solicitudes_contador = baseDatos.getReference("contador").child(user.getUid());
    private DatabaseReference ref_estados = baseDatos.getReference("estado").child(user.getUid());
    private EditText txt_nombre;
    private View inflatedView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_usuario);
        //  img_foto = findViewById(R.id.imagenPerfil);

        inflatedView = getLayoutInflater().inflate(R.layout.op_introducir_pass, null, false);
        txt_nombre = inflatedView.findViewById(R.id.txtcambiaNombre);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new Adaptador(this));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: {
                        tab.setText("Usuario");
                        tab.setIcon(R.drawable.ic_usuario);
                        break;
                    }
                    case 1: {
                        tab.setText("Ficha");
                        tab.setIcon(R.drawable.ic_ficha);
                        break;
                    }
                    case 2: {
                        tab.setText("Chats");
                        tab.setIcon(R.drawable.ic_chats);
                        break;
                    }
                    case 3: {
                        tab.setText("Partidas");
                        tab.setIcon(R.drawable.ic_gruposchat);

                        break;
                    }
                    case 4:
                        tab.setText("Jugadores");
                        tab.setIcon(R.drawable.ic_solicitudes);
                        tab.setIcon(R.drawable.ic_jugadores);

                        final BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        //Añadimos el número de peticiones por aceptar etc
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(),R.color.solicitudColor)
                        );
                        //Esto muestra el número de solicitudes pendientes real
                        ref_solicitudes_contador.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Integer valor = snapshot.getValue(Integer.class);
                                    badgeDrawable.setVisible(true);
                                    //No mostramos el icono si no hay solicitudes, lo mostramos solo si hay
                                    if(valor.equals("0")){
                                        badgeDrawable.setVisible(false);
                                    }else{
                                        badgeDrawable.setNumber(valor);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                        break;
                }
            }
        });
        //Añadimos el tab
        tabLayoutMediator.attach();
        //Añadimos un ReciclerView
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //Esto elimina el número de solicitudes cuando pulsamos sobre él
                BadgeDrawable badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
                badgeDrawable.setVisible(false);
                if(position == 4){
                    reiniciarContador();
                }
            }
        });


        usuarioUnico();

        SharedPreferences sPrefs = getSharedPreferences("fLogin",MODE_PRIVATE);

        if(sPrefs.getBoolean("fLogin",true)){
            AlertDialog dialogo;
            AlertDialog.Builder bulder2 = new AlertDialog.Builder(OpcionesUsuario.this);
            bulder2.setTitle("Cambiar Nombre").setView(R.layout.op_cambiar_nombe)
                    .setPositiveButton("Aplicar", null);

            AlertDialog dialog2 = bulder2.create();
            dialog2.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button boton = ((AlertDialog) dialog2).getButton(AlertDialog.BUTTON_POSITIVE);
                    boton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String, Object> hasMap = new HashMap<>();
                            Dialog in = (Dialog)dialog;
                            txt_nombre = in.findViewById(R.id.txtcambiaNombre);
                            if(txt_nombre.getText().toString().length()==0){
                                Toast.makeText(OpcionesUsuario.this,
                                        "El nombre no puede estar vacio",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                hasMap.put("nombre",txt_nombre.getText().toString());
                                ref_usuario.updateChildren(hasMap);
                                Toast.makeText(OpcionesUsuario.this,
                                        "Nombre cambiado con exito", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });
            dialog2.show();
            SharedPreferences.Editor editor;
            editor = sPrefs.edit();
            editor.putBoolean("fLogin",false);
            editor.apply();
        }

        viewPager2.setCurrentItem(getIntent().getIntExtra("RECARGA",0));

    }


    private void reiniciarContador() {
        ref_solicitudes_contador.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ref_solicitudes_contador.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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
        //Necesitamos generar una fecha de la última conexión
        getUltimaFecha();
    }
    //Obtenemos la última fecha (horas,min)
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
                Estado est = new Estado(estado,"","","");
                ref_estados.setValue(est);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    //Aquí hago referencias a la BD una vez hecho el login
    private void usuarioUnico() {
        //Lo hacemos una sola vez para evitar bucle de registros (en caso de usar push)
        ref_usuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //Creamos la referencia si no existe
                if(!snapshot.exists()){
                    Usuario _user = new Usuario();
                    _user.setId(user.getUid());
                    _user.setNombre(user.getDisplayName());
                    _user.setEmail(user.getEmail());
                    _user.setFoto(user.getPhotoUrl().toString());
                    _user.setNuevosMensajes(0);
                    _user.setSolicitudMensajes(0);
                    //Guardamos los cambios
                    ref_usuario.setValue(_user);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cerrarSesion:
                cerrarSesion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Devuelve al mainActivity al cerrar sesión
    private void irMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //Permite cerrar la sesión
    private void cerrarSesion(){
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        finish();
                        Toast.makeText(OpcionesUsuario.this,
                                "Sesión cerrada", Toast.LENGTH_SHORT).show();
                        irMainActivity();
                    }
                });
    }

    //Abre el diálogo que muestra las posibles opciones del usuario al hacer click sobre el cardView
    public void abrirOpcionesUsuario(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(OpcionesUsuario.this);
        builder
                .setTitle("Opciones Perfil")
                .setItems(R.array.OpcionesUsuario, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                AlertDialog dialogo;
                                AlertDialog.Builder bulder2 = new AlertDialog.Builder(OpcionesUsuario.this);
                                bulder2.setTitle("Cambiar Nombre").setView(R.layout.op_cambiar_nombe)
                                        .setPositiveButton("Aplicar", null)
                                        .setNegativeButton("Cancelar",null);

                                AlertDialog dialog2 = bulder2.create();
                                dialog2.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        Button boton = ((AlertDialog) dialog2).getButton(AlertDialog.BUTTON_POSITIVE);
                                        boton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Map<String, Object> hasMap = new HashMap<>();
                                                Dialog in = (Dialog)dialog;
                                                txt_nombre = in.findViewById(R.id.txtcambiaNombre);
                                                if(txt_nombre.getText().toString().length()==0){
                                                    Toast.makeText(OpcionesUsuario.this,
                                                            "El nombre no puede estar vacio",
                                                            Toast.LENGTH_SHORT).show();
                                                }else{
                                                    hasMap.put("nombre",txt_nombre.getText().toString());
                                                    ref_usuario.updateChildren(hasMap);
                                                    Toast.makeText(OpcionesUsuario.this,
                                                            "Nombre cambiado con éxito", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            }
                                        });
                                    }
                                });
                                dialog2.show();
                                break;
                            case 1:
                                cerrarSesion();
                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




}