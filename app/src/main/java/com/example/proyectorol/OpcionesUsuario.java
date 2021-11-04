package com.example.proyectorol;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.proyectorol.adapters.Adaptador;
import com.example.proyectorol.pojos.ChatCon;
import com.example.proyectorol.pojos.Estado;
import com.example.proyectorol.pojos.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpcionesUsuario extends AppCompatActivity {
    Intent mServiceIntent;
    private ComponentName commponent;
    //
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
    private final DatabaseReference ref_usuario = baseDatos.getReference("usuarios").child(user.getUid()); //Esto nos permite controlar las referencias al usuario por ID
    private final DatabaseReference ref_solicitudes_contador = baseDatos.getReference("contador").child(user.getUid());
    private final DatabaseReference ref_estados = baseDatos.getReference("estado").child(user.getUid());
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(user.getUid());
    DatabaseReference ref_NuevoMensaje = baseDatos.getReference("usuarios").child(user.getUid()).child("chatCon");
    private NotificationCompat.Builder builder = null;
    private EditText txt_nombre;
    private View inflatedView;
    private CircleImageView img_foto;
    private final byte PICK_IMAGE = 1;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_usuario);

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

        createNotificationChannel(); //Creamos un canal para la notificación
        ref_NuevoMensaje.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatCon chatCon = snapshot.getValue(ChatCon.class);
                if(snapshot.exists()){
                    builder = new NotificationCompat.Builder(OpcionesUsuario.this, "2");
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                    builder.setAutoCancel(true); //Permite que se cancele al pulsar sobre ella
                    builder.setContentTitle("Mensaje de "+chatCon.getRemitenteNick());
                    builder.setContentText(chatCon.getUltimoMensaje());
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(2, builder.build());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                   // _user.setFoto(user.getPhotoUrl().toString());
                    _user.setFoto("empty");
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

                                Intent fotos = new Intent();
                                fotos.setAction((Intent.ACTION_GET_CONTENT));
                                fotos.setType("image/*");
                                startActivityForResult(Intent.createChooser(fotos,"Seleciona foto perfil"),PICK_IMAGE);


                                break;

                            case 2:
                                cerrarSesion();
                                break;
                        }
                    }

                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {

            //Upload before anything else
            String foto = data.getData().getLastPathSegment();
            StorageReference imagenPerf = storageReference.child(foto);

            Task uploadTask = imagenPerf.putFile(data.getData());

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imagenPerf.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        AlertDialog.Builder bulder3 = new AlertDialog.Builder(OpcionesUsuario.this);
                        bulder3.setTitle("Cambiar Foto Perfil").setView(R.layout.op_cambiar_foto_perfil)
                                .setPositiveButton("Aplicar", null)
                                .setNegativeButton("Cancelar",null);

                        AlertDialog dialog3 = bulder3.create();

                        dialog3.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                Dialog in = (Dialog)dialog;
                                img_foto = in.findViewById(R.id.imgPerfil);
                                Picasso.get().load(downloadUri).into(img_foto);
                                Button boton = ((AlertDialog) dialog3).getButton(AlertDialog.BUTTON_POSITIVE);
                                boton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        actualizaFotoPerfilUsuario(downloadUri);
                                    }
                                });
                            }
                        });
                        dialog3.show();


                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

    private boolean  actualizaFotoPerfilUsuario(Uri urlFoto){
        boolean exito;

         HashMap hashMap = new HashMap<String,String>();
         hashMap.put("foto",urlFoto.toString());
         ref_usuario.updateChildren(hashMap);

        exito = true;
        return exito;
    }
}