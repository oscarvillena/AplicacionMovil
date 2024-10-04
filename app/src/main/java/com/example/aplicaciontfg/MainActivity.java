package com.example.aplicaciontfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout_usuario_normal;
    private ActionBarDrawerToggle actionBarDrawerToggle_usuario_normal;
    private NavigationView navigationView_usuario_normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout_usuario_normal = findViewById(R.id.drawer_layout);
        navigationView_usuario_normal = findViewById(R.id.nav_view);
        actionBarDrawerToggle_usuario_normal = new ActionBarDrawerToggle(this, drawerLayout_usuario_normal, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout_usuario_normal.addDrawerListener(actionBarDrawerToggle_usuario_normal);
        actionBarDrawerToggle_usuario_normal.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView_usuario_normal.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.nav_option1) {
                    startActivity(new Intent(MainActivity.this, TareasActivity.class));
                } else if (itemId == R.id.nav_option2) {
                    startActivity(new Intent(MainActivity.this, CompraActivity.class));
                } else if (itemId == R.id.nav_option3) {
                    startActivity(new Intent(MainActivity.this, CitasActivity.class));
                } else if (itemId == R.id.nav_calendario) {
                    startActivity(new Intent(MainActivity.this, CalendarioActivity.class));
                } else if (itemId == R.id.nav_galeria) {
                    startActivity(new Intent(MainActivity.this, GaleriaActivity.class));
                } else if (itemId == R.id.nav_puntos) {
                    startActivity(new Intent(MainActivity.this, PuntosActivity.class));
                } else if (itemId == R.id.nav_chat) {
                    startActivity(new Intent(MainActivity.this, MensajeActivity.class));
                } else if (itemId == R.id.nav_logout) {
                    logout();
                } else if (itemId == R.id.nav_gestion) {
                    showToast("Acceso para admin");
                }
                drawerLayout_usuario_normal.closeDrawers();
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle_usuario_normal.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //metodo para cerrar sesion
    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    //metodo para mostrar toast de acceso para admin
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}