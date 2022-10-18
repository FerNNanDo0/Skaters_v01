package com.social.skaters.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.social.skaters.R;
import com.social.skaters.firebaseRef.FirebaseRef;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth userLogado;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    RecyclerView recyclerViewMain;

    LinearLayout layoutPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_main);
        navigationView = findViewById(R.id.nav_view);
        layoutPerfil = findViewById(R.id.editInfoUser);
        recyclerViewMain = findViewById(R.id.recyclerViewMain);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
            //getSupportActionBar().setTitle("");
        }

        //abrir tela de config user
        editInfoUser();

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


    public void editInfoUser() {
        layoutPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Editar informações do usuario",
                        Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
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