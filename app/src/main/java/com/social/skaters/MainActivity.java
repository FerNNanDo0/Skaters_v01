package com.social.skaters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_main);
        navigationView = findViewById(R.id.nav_view);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("");
        }

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener( toggle );

        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();

    }


    public void infoUser(View v) {
        Toast.makeText(this, "Editar informações do usuario", Toast.LENGTH_SHORT).show();
        drawer.closeDrawer(GravityCompat.START);
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
}