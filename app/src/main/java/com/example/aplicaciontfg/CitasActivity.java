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

public class CitasActivity extends AppCompatActivity {
    private TableLayout tableLayout_citas;
    private EditText etIDCita, etFechaHora, etMedico, etMotivo, etIDUsuario;
    private Button btnCrearCita, btnModificarCita, btnEliminarCita, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);
        tableLayout_citas = findViewById(R.id.tableLayout);
        etIDCita = findViewById(R.id.etID_cita);
        etFechaHora = findViewById(R.id.etFecha_hora);
        etMedico = findViewById(R.id.etMedico);
        etMotivo = findViewById(R.id.etMotivo);
        etIDUsuario = findViewById(R.id.etID_usuario);
        btnCrearCita = findViewById(R.id.btnCrearCita);
        btnModificarCita = findViewById(R.id.btnModificarCita);
        btnEliminarCita = findViewById(R.id.btnEliminarCita);
        btnVolver = findViewById(R.id.btnVolver);

        btnCrearCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearCita();
            }
        });

        btnModificarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarCita();
            }
        });

        btnEliminarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCita();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cargarCitas();
    }
    //metodo para cargar las citas
    private void cargarCitas() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_citas.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            tableLayout_citas.removeAllViews();
                            agregarCabecera();

                            JSONArray citas = new JSONArray(response);
                            for (int i = 0; i < citas.length(); i++) {
                                JSONObject cita = citas.getJSONObject(i);
                                TableRow fila = new TableRow(CitasActivity.this);
                                fila.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                TextView idCita = new TextView(CitasActivity.this);
                                idCita.setText(String.valueOf(cita.getInt("ID_cita")));
                                idCita.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView fechaHora = new TextView(CitasActivity.this);
                                fechaHora.setText(cita.getString("Fecha_hora"));
                                fechaHora.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView medico = new TextView(CitasActivity.this);
                                medico.setText(cita.getString("Medico"));
                                medico.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView motivo = new TextView(CitasActivity.this);
                                motivo.setText(cita.getString("Motivo"));
                                motivo.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView idUsuario = new TextView(CitasActivity.this);
                                idUsuario.setText(String.valueOf(cita.getInt("ID_usuario")));
                                idUsuario.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                fila.addView(idCita);
                                fila.addView(fechaHora);
                                fila.addView(medico);
                                fila.addView(motivo);
                                fila.addView(idUsuario);

                                tableLayout_citas.addView(fila);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CitasActivity.this, "Error en cargar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CitasActivity.this, "Error en cargar", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
    //metodo para agregar la cabecera de las tablas
    private void agregarCabecera() {
        TableRow cabecera = new TableRow(this);
        cabecera.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView tvID = new TextView(this);
        tvID.setText("ID Cita");
        tvID.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvFechaHora = new TextView(this);
        tvFechaHora.setText("Fecha/Hora");
        tvFechaHora.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvMedico = new TextView(this);
        tvMedico.setText("Medico");
        tvMedico.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvMotivo = new TextView(this);
        tvMotivo.setText("Motivo");
        tvMotivo.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvIDUsuario = new TextView(this);
        tvIDUsuario.setText("ID Usuario");
        tvIDUsuario.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        cabecera.addView(tvID);
        cabecera.addView(tvFechaHora);
        cabecera.addView(tvMedico);
        cabecera.addView(tvMotivo);
        cabecera.addView(tvIDUsuario);

        tableLayout_citas.addView(cabecera);
    }
    //metodo para crear la cita
    private void crearCita() {
        String idCita = etIDCita.getText().toString().trim();
        String fechaHora = etFechaHora.getText().toString().trim();
        String medico = etMedico.getText().toString().trim();
        String motivo = etMotivo.getText().toString().trim();
        String idUsuario = etIDUsuario.getText().toString().trim();

        if (idCita.isEmpty() || fechaHora.isEmpty() || medico.isEmpty() || motivo.isEmpty() || idUsuario.isEmpty()) {
            Toast.makeText(CitasActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_citas.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CitasActivity.this, "Cita creada exitosamente", Toast.LENGTH_SHORT).show();
                        etIDCita.setText("");
                        etFechaHora.setText("");
                        etMedico.setText("");
                        etMotivo.setText("");
                        etIDUsuario.setText("");
                        cargarCitas();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CitasActivity.this, "Error al crear la cita" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_cita", idCita);
                params.put("Fecha_hora", fechaHora);
                params.put("Medico", medico);
                params.put("Motivo", motivo);
                params.put("ID_usuario", idUsuario);
                return params;
            }
        };

        queue.add(stringRequest);
    }
    //metodo para modificar la cita
    private void modificarCita() {
        String idCita = etIDCita.getText().toString().trim();
        String fechaHora = etFechaHora.getText().toString().trim();
        String medico = etMedico.getText().toString().trim();
        String motivo = etMotivo.getText().toString().trim();
        String idUsuario = etIDUsuario.getText().toString().trim();

        if (idCita.isEmpty() || fechaHora.isEmpty() || medico.isEmpty() || motivo.isEmpty() || idUsuario.isEmpty()) {
            Toast.makeText(CitasActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_citas.php";

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CitasActivity.this, "Cita modificada exitosamente", Toast.LENGTH_SHORT).show();
                        etIDCita.setText("");
                        etFechaHora.setText("");
                        etMedico.setText("");
                        etMotivo.setText("");
                        etIDUsuario.setText("");
                        cargarCitas();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CitasActivity.this, "Error al modificar la cita" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_cita", idCita);
                params.put("Fecha_hora", fechaHora);
                params.put("Medico", medico);
                params.put("Motivo", motivo);
                params.put("ID_usuario", idUsuario);
                return params;
            }
        };

        queue.add(stringRequest);
    }
    //metodo para eliminar la cita
    private void eliminarCita() {
        String idCita = etIDCita.getText().toString().trim();

        if (idCita.isEmpty()) {
            Toast.makeText(CitasActivity.this, "Por favor, complete el ID de la cita", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_citas.php?ID_cita=" + idCita;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CitasActivity.this, "Cita eliminada exitosamente", Toast.LENGTH_SHORT).show();
                        etIDCita.setText("");
                        cargarCitas();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CitasActivity.this, "Error al eliminar la cita" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}