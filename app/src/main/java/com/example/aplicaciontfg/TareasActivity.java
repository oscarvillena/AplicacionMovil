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

public class TareasActivity extends AppCompatActivity {

    private TableLayout tableLayout_tareas;
    private EditText etIDTarea, etNombreTarea, etDescripcionTarea, etFechaTarea, etEstadoTarea, etIDUsuario_tareas;
    private Button btnCrear_tarea, btnModificar_tarea, btnEliminar_tarea, btnVolver_tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        tableLayout_tareas = findViewById(R.id.tableLayout);
        etIDTarea = findViewById(R.id.etID_tarea);
        etNombreTarea = findViewById(R.id.etNombre_tarea);
        etDescripcionTarea = findViewById(R.id.etDescripcion_tarea);
        etFechaTarea = findViewById(R.id.etFecha_tarea);
        etEstadoTarea = findViewById(R.id.etEstado_tarea);
        etIDUsuario_tareas = findViewById(R.id.etID_usuario);

        btnCrear_tarea = findViewById(R.id.btnCrear);
        btnModificar_tarea = findViewById(R.id.btnModificar);
        btnEliminar_tarea = findViewById(R.id.btnEliminar);
        btnVolver_tarea = findViewById(R.id.btnVolver);

        btnCrear_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearTarea();
            }
        });
        btnModificar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarTarea();
            }
        });
        btnEliminar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarTarea();
            }
        });

        btnVolver_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cargarTareas();
    }
    //metodo para cargar las tareas
    private void cargarTareas() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_tareas.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            limpiarTabla();

                            JSONArray tareas = new JSONArray(response);
                            for (int i = 0; i < tareas.length(); i++) {
                                JSONObject tarea = tareas.getJSONObject(i);
                                TableRow fila = new TableRow(TareasActivity.this);
                                fila.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                TextView idTarea = new TextView(TareasActivity.this);
                                idTarea.setText(String.valueOf(tarea.getInt("ID_tarea")));
                                idTarea.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView nombreTarea = new TextView(TareasActivity.this);
                                nombreTarea.setText(tarea.getString("Nombre"));
                                nombreTarea.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView descripcionTarea = new TextView(TareasActivity.this);
                                descripcionTarea.setText(tarea.getString("Descripcion"));
                                descripcionTarea.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView fechaTarea = new TextView(TareasActivity.this);
                                fechaTarea.setText(tarea.getString("Fecha_Tarea"));
                                fechaTarea.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView estadoTarea = new TextView(TareasActivity.this);
                                estadoTarea.setText(tarea.getString("Estado"));
                                estadoTarea.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView idUsuarioTarea = new TextView(TareasActivity.this);
                                idUsuarioTarea.setText(String.valueOf(tarea.getInt("ID_Usuario")));
                                idUsuarioTarea.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                fila.addView(idTarea);
                                fila.addView(nombreTarea);
                                fila.addView(descripcionTarea);
                                fila.addView(fechaTarea);
                                fila.addView(estadoTarea);
                                fila.addView(idUsuarioTarea);

                                tableLayout_tareas.addView(fila);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(TareasActivity.this, "Error en cargar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TareasActivity.this, "Error en cargar", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
    //metodod para que borre si queda elgo en la tabla
    private void limpiarTabla() {
        int childCount = tableLayout_tareas.getChildCount();
        if (childCount > 1) {
            tableLayout_tareas.removeViews(1, childCount - 1);
        }
    }
    //metodo para cargar las tareas
    private void crearTarea() {
        final String nombre = etNombreTarea.getText().toString().trim();
        final String descripcion = etDescripcionTarea.getText().toString().trim();
        final String fechaTarea = etFechaTarea.getText().toString().trim();
        final String estadoTarea = etEstadoTarea.getText().toString().trim();
        final String idUsuario = etIDUsuario_tareas.getText().toString().trim();

        if (nombre.isEmpty() || descripcion.isEmpty() || fechaTarea.isEmpty() || estadoTarea.isEmpty() || idUsuario.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_tareas.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(TareasActivity.this, response, Toast.LENGTH_SHORT).show();
                        cargarTareas();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TareasActivity.this, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("descripcion", descripcion);
                params.put("fecha_tarea", fechaTarea);
                params.put("estado_tarea", estadoTarea);
                params.put("id_usuario", idUsuario);
                return params;
            }
        };

        queue.add(postRequest);
    }

    //metodo para modificar la tarea
    private void modificarTarea() {
        final String idTarea = etIDTarea.getText().toString().trim();
        final String nombre = etNombreTarea.getText().toString().trim();
        final String descripcion = etDescripcionTarea.getText().toString().trim();
        final String fechaTarea = etFechaTarea.getText().toString().trim();
        final String estadoTarea = etEstadoTarea.getText().toString().trim();
        final String idUsuario = etIDUsuario_tareas.getText().toString().trim();

        if (idTarea.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || fechaTarea.isEmpty() || estadoTarea.isEmpty() || idUsuario.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_tareas.php";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(TareasActivity.this, response, Toast.LENGTH_SHORT).show();
                        cargarTareas();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TareasActivity.this, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_tarea", idTarea);
                params.put("nombre", nombre);
                params.put("descripcion", descripcion);
                params.put("fecha_tarea", fechaTarea);
                params.put("estado_tarea", estadoTarea);
                params.put("id_usuario", idUsuario);
                return params;
            }
        };

        queue.add(putRequest);
    }

    //metodo para eliminar una tarea
    private void eliminarTarea() {
        final String idTarea = etIDTarea.getText().toString().trim();

        if (idTarea.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce el ID de la tarea a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_tareas.php?id_tarea=" + idTarea;
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(TareasActivity.this, response, Toast.LENGTH_SHORT).show();
                        cargarTareas();

                        if (getParent() instanceof PuntosActivity) {
                            ((PuntosActivity) getParent()).cargarUsuarios();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TareasActivity.this, "Error en la solicitud" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(deleteRequest);
    }
}