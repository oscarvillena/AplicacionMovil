package com.example.aplicaciontfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
public class AdminActivity extends AppCompatActivity {

    private boolean isAdmin = false;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    //menu para acceder a las difirentes funcionalidades
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.nav_option1) {
                    startActivity(new Intent(AdminActivity.this, TareasActivity.class));
                } else if (itemId == R.id.nav_option2) {
                    startActivity(new Intent(AdminActivity.this, CompraActivity.class));
                } else if (itemId == R.id.nav_option3) {
                    startActivity(new Intent(AdminActivity.this, CitasActivity.class));
                } else if (itemId == R.id.nav_calendario) {
                    startActivity(new Intent(AdminActivity.this, CalendarioActivity.class));
                } else if (itemId == R.id.nav_galeria) {
                    startActivity(new Intent(AdminActivity.this, GaleriaActivity.class));
                } else if (itemId == R.id.nav_puntos) {
                    startActivity(new Intent(AdminActivity.this, PuntosActivity.class));
                } else if (itemId == R.id.nav_chat) {
                    startActivity(new Intent(AdminActivity.this, MensajeActivity.class));
                } else if (itemId == R.id.nav_gestion) {
                    startActivity(new Intent(AdminActivity.this, GestionActivity.class));
                }  else if (itemId == R.id.nav_logout) {
                    cerrarSesion();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        invalidateOptionsMenu();
    }
    //desplega menu para seleccionar funcionalidades
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_manage_users) {
            Intent intent = new Intent(this, GestionActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            cerrarSesion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //metodo para cerrar sesion y volver a loginactivity
    private void cerrarSesion() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}