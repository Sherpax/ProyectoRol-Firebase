package com.example.proyectorol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectorol.adapters.Adaptador;
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
import com.google.firebase.database.core.UserWriteRecord;

import org.jetbrains.annotations.NotNull;

import java.util.GregorianCalendar;

public class OpcionesUsuario extends AppCompatActivity {
     FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
     FirebaseDatabase baseDatos = FirebaseDatabase.getInstance();
     DatabaseReference ref_usuario = baseDatos.getReference("Usuarios"); //Esto nos permite controlar las referencias al usuario por ID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_usuario);
      //  img_foto = findViewById(R.id.imagenPerfil);
      //  nombreUsuario = findViewById(R.id.nomUser);

        ref_usuario.setValue(user.getUid());

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
                            tab.setText("Solicitudes");
                            tab.setIcon(R.drawable.ic_solicitudes);
                            BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                            //Añadimos el número de peticiones por aceptar etc
                            badgeDrawable.setBackgroundColor(
                                    ContextCompat.getColor(getApplicationContext(),R.color.colorAccent)
                            );
                            badgeDrawable.setVisible(true);
                            //Modificar esto con el número de solicitudes real
                            badgeDrawable.setNumber(1);
                            break;
                        }
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
            }
        });


        usuarioUnico();

     //   nombreUsuario.setText(user.getDisplayName());


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
                    _user.setNick("Probando");
                    _user.setEmail(user.getEmail());
                    _user.setFoto(user.getPhotoUrl().toString());
                    _user.setEstado("Conectado"); //Modificar
                    _user.setFecha("16/05/2021"); //Modificar
                    _user.setHora("21:12");
                    _user.setNuevosMensajes(1);
                    _user.setSolicitudMensajes(2);
                    //Guardamos los cambios
                    ref_usuario.setValue(_user);
                    Toast.makeText(OpcionesUsuario.this, "Yeah", Toast.LENGTH_SHORT).show();
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
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void irMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}