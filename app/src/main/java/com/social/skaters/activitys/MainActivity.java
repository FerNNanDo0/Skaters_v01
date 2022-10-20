package com.social.skaters.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.social.skaters.R;
import com.social.skaters.firebaseRef.FirebaseRef;
import com.social.skaters.helper.CodificarBase64;
import com.social.skaters.models.User;

import java.util.Objects;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser userAtual;
    private FirebaseAuth userLogado;

    private DatabaseReference db_usuarioRef;
    private ValueEventListener valueEventListenerUsuario;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    RecyclerView recyclerViewMain;

    ConstraintLayout layoutPerfil;

    Intent activityConfig;

    CircleImageView imgNav;
    TextView nomeNav, nivelNav;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_main);
        navigationView = findViewById(R.id.nav_view);;
        recyclerViewMain = findViewById(R.id.recyclerViewMain);

        layoutPerfil = findViewById(R.id.editInfoUser);

        // Obtém a referência da view de cabeçalho
        headerView = navigationView.getHeaderView(0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
            //getSupportActionBar().setTitle("");
        }

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();

        // config adapterMain

        // config RecyclerView Main
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMain.setLayoutManager(layoutManager);
        recyclerViewMain.setHasFixedSize(true);
//        recyclerViewMain.setAdapter();


    }

    public void editarPerfilUser(View view){
        activityConfig = new Intent(MainActivity.this, ActivityConfigUserAtual.class );
        startActivity( activityConfig );

        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart() {
        super.onStart();
        definirInfoUser();
    }

    @Override
    protected void onStop() {
        super.onStop();

        db_usuarioRef.removeEventListener( valueEventListenerUsuario );
    }

    public void definirInfoUser(){

        imgNav = headerView.findViewById(R.id.imageUserNav);
        nomeNav = headerView.findViewById(R.id.nomeNav);
        nivelNav = headerView.findViewById(R.id.textNivelNav);

        userAtual = FirebaseRef.getFirebaseUserLogado();
        db_usuarioRef = FirebaseRef.getFirebaseDatabase()
                .child("usuarios")
                .child( FirebaseRef.getIdUserAtual() );

        valueEventListenerUsuario = db_usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue( User.class );

                nomeNav.setText( user.getNome() );
                nivelNav.setText( user.getNivel() );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if( userAtual.getPhotoUrl() != null ){
            Glide.with(getApplicationContext()).load( userAtual.getPhotoUrl() ).into( imgNav );
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_pistas:
                Toast.makeText(this, "lista de pistas no mapa", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_camps:
                Toast.makeText(this, "noticias de campeonatos ou eventos", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_shareApp:
                Toast.makeText(this, "compartilhar o App", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_info:
                Toast.makeText(this, "Informações do App", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.logout:
                userLogado = FirebaseRef.getFirebaseAuthRef();
                userLogado.signOut();

                Intent logActivity = new Intent(this, LoguinActivity.class);
                startActivity(logActivity);
                finish();
                break;

            case R.id.itemConfigUser:
                Toast.makeText(getApplicationContext(),
                        "configuração do usuário", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}