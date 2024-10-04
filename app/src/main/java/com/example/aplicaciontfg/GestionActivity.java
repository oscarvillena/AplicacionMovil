package com.example.aplicaciontfg;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
public class GestionActivity extends AppCompatActivity {
    private TableLayout tableLayout_gestion;
    private EditText etIDUsuario_gestion, etNombre, etEmail, etContraseña, etEsAdmin;
    private Button btnCrear, btnModificar, btnEliminar, btnVolver_gestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion);
        tableLayout_gestion = findViewById(R.id.tableLayout);
        etIDUsuario_gestion = findViewById(R.id.etID_usuario);
        etNombre = findViewById(R.id.etNombre_usuario);
        etEmail = findViewById(R.id.etCorreo_electronico);
        etContraseña = findViewById(R.id.etContraseña);
        etEsAdmin = findViewById(R.id.etEsAdmin);
        btnCrear = findViewById(R.id.btnCrear);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver_gestion = findViewById(R.id.btnVolver);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearUsuario();
            }
        });
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarUsuario();
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarUsuario();
            }
        });
        btnVolver_gestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cargarUsuarios();
    }
    //metodo para cargar usuario
    private void cargarUsuarios() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_usuarios.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray usuarios = new JSONArray(response);
                            tableLayout_gestion.removeAllViews();
                            agregarCabecera();
                            for (int i = 0; i < usuarios.length(); i++) {
                                JSONObject usuario = usuarios.getJSONObject(i);
                                TableRow fila = new TableRow(GestionActivity.this);
                                fila.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                TextView id_usuario = new TextView(GestionActivity.this);
                                id_usuario.setText(usuario.getString("ID_usuario"));
                                id_usuario.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView nombre_usuario = new TextView(GestionActivity.this);
                                nombre_usuario.setText(usuario.getString("Nombre_usuario"));
                                nombre_usuario.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView correo_electronico = new TextView(GestionActivity.this);
                                correo_electronico.setText(usuario.getString("Correo_electronico"));
                                correo_electronico.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView contraseña = new TextView(GestionActivity.this);
                                contraseña.setText(usuario.getString("Contraseña"));
                                contraseña.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView es_admin = new TextView(GestionActivity.this);
                                es_admin.setText(usuario.getString("EsAdmin"));
                                es_admin.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                fila.addView(id_usuario);
                                fila.addView(nombre_usuario);
                                fila.addView(correo_electronico);
                                fila.addView(contraseña);
                                fila.addView(es_admin);

                                tableLayout_gestion.addView(fila);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(GestionActivity.this, "Error al cargar" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GestionActivity.this, "Error al cargar" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
    //metodo para agregar cabeceras de las tablas
    private void agregarCabecera() {
        TableRow filaCabecera = new TableRow(this);

        TextView tvID = new TextView(this);
        tvID.setText("ID Usuario");
        tvID.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvNombre = new TextView(this);
        tvNombre.setText("Nombre");
        tvNombre.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvEmail = new TextView(this);
        tvEmail.setText("Email");
        tvEmail.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvContraseña = new TextView(this);
        tvContraseña.setText("Contraseña");
        tvContraseña.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvEsAdmin = new TextView(this);
        tvEsAdmin.setText("Es Admin");
        tvEsAdmin.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        filaCabecera.addView(tvID);
        filaCabecera.addView(tvNombre);
        filaCabecera.addView(tvEmail);
        filaCabecera.addView(tvContraseña);
        filaCabecera.addView(tvEsAdmin);

        tableLayout_gestion.addView(filaCabecera);
    }
    //metodo para crear usarios
    private void crearUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contrasena = etContraseña.getText().toString().trim();
        String esAdmin = etEsAdmin.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty() || esAdmin.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_usuarios.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(GestionActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                        etNombre.setText("");
                        etEmail.setText("");
                        etContraseña.setText("");
                        etEsAdmin.setText("");
                        tableLayout_gestion.removeAllViews();
                        cargarUsuarios();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GestionActivity.this, "Error al crear" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("email", email);
                params.put("contrasena", contrasena);
                params.put("es_admin", esAdmin);
                return params;
            }
        };

        queue.add(stringRequest);
    }
    //metodo para modificar usario
    private void modificarUsuario() {
        String id = etIDUsuario_gestion.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contrasena = etContraseña.getText().toString().trim();
        String esAdmin = etEsAdmin.getText().toString().trim();

        if (id.isEmpty() || nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty() || esAdmin.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_usuarios.php";

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(GestionActivity.this, "Usuario modificado", Toast.LENGTH_SHORT).show();
                        etIDUsuario_gestion.setText("");
                        etNombre.setText("");
                        etEmail.setText("");
                        etContraseña.setText("");
                        etEsAdmin.setText("");
                        tableLayout_gestion.removeAllViews();
                        cargarUsuarios();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GestionActivity.this, "Error al modificar" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("nombre", nombre);
                params.put("email", email);
                params.put("contrasena", contrasena);
                params.put("es_admin", esAdmin);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    //metodo de eliminar usuario
    private void eliminarUsuario() {
        btnEliminar.setEnabled(false);

        String id = etIDUsuario_gestion.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(GestionActivity.this, "Por favor, introduce el ID del usuario a eliminar", Toast.LENGTH_SHORT).show();
            btnEliminar.setEnabled(true);
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_usuarios.php?id=" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(GestionActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                        etIDUsuario_gestion.setText("");
                        tableLayout_gestion.removeAllViews();
                        cargarUsuarios();
                        btnEliminar.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GestionActivity.this, "Error al eliminar" + error.getMessage(), Toast.LENGTH_SHORT).show();
                btnEliminar.setEnabled(true);
            }
        });
        queue.add(stringRequest);
    }
}