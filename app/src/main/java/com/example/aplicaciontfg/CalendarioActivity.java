package com.example.aplicaciontfg;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalendarioActivity extends AppCompatActivity {
    private TextView textViewMes;
    private CalendarView calendarView;
    private EditText editTextDescripcion;
    private Button buttonGuardar;
    private Button buttonVolver;
    private TableLayout tableLayout;
    private String fechaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        textViewMes = findViewById(R.id.textViewMes);
        calendarView = findViewById(R.id.calendarView);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonVolver = findViewById(R.id.buttonVolver);
        tableLayout = findViewById(R.id.tableLayout);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        fechaSeleccionada = year + "-" + (month + 1) + "-" + dayOfMonth;

        cargarEventos();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                fechaSeleccionada = year + "-" + (month + 1) + "-" + dayOfMonth;
                textViewMes.setText(obtenerNombreMes(month) + " " + year);
                editTextDescripcion.setVisibility(View.VISIBLE);
                buttonGuardar.setVisibility(View.VISIBLE);
                obtenerDescripcionEvento(fechaSeleccionada);
            }
        });

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDescripcionEvento();
            }
        });

        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //carga los eventos del calendario
    private void cargarEventos() {
        String url = "http://192.168.56.1/Proyecto_Conexiones/obtener_eventos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String fecha = jsonObject.getString("fecha");
                                String descripcion = jsonObject.getString("descripcion");
                                actualizarTabla(fecha, descripcion);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //obtiene la descripcion del evento
    private void obtenerDescripcionEvento(String fecha) {
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_descripcion.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.startsWith("<br")) {
                                return;
                            }

                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            if (success) {
                                String descripcionEvento = jsonObject.getString("descripcion_evento");
                                editTextDescripcion.setText(descripcionEvento);
                                actualizarTabla(fecha, descripcionEvento);
                            } else {
                                Toast.makeText(CalendarioActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fecha", fecha);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //metodo que obtiene los nombres de los meses
    private String obtenerNombreMes(int month) {
        String[] nombresMes = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return nombresMes[month];
    }
    //metodo que actualiza la tabla
    private void actualizarTabla(String fecha, String descripcion) {
        TableRow tableRow = new TableRow(this);

        TextView textViewFecha = new TextView(this);
        textViewFecha.setText(fecha);
        textViewFecha.setPadding(5, 5, 5, 5);

        TextView textViewDescripcion = new TextView(this);
        textViewDescripcion.setText(descripcion);
        textViewDescripcion.setPadding(5, 5, 5, 5);

        tableRow.addView(textViewFecha);
        tableRow.addView(textViewDescripcion);

        tableLayout.addView(tableRow);
    }
    //metodo que guarda la descripcion del evento
    private void guardarDescripcionEvento() {
        String nuevaDescripcion = editTextDescripcion.getText().toString().trim();

        if (nuevaDescripcion.isEmpty()) {
            Toast.makeText(CalendarioActivity.this, "Datos incompletos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_caledarios.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            Toast.makeText(CalendarioActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (success) {
                                String descripcionEvento = jsonObject.getString("descripcion_evento");
                                actualizarTabla(fechaSeleccionada, descripcionEvento);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CalendarioActivity.this, "Error respuesta servidor", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fecha", fechaSeleccionada);
                params.put("descripcion", nuevaDescripcion);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        editTextDescripcion.setText("");
    }
}