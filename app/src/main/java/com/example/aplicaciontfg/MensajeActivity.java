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

public class MensajeActivity extends AppCompatActivity {

    private EditText editTextMensaje;
    private Button buttonEnviar, btnVolver_mensaje;
    private TableLayout tableLayoutMensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);

        editTextMensaje = findViewById(R.id.editTextMensaje);
        buttonEnviar = findViewById(R.id.buttonEnviar);
        tableLayoutMensajes = findViewById(R.id.tableLayoutMensajes);
        btnVolver_mensaje = findViewById(R.id.btnVolver);

        btnVolver_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensajePrivado();
            }
        });

        obtenerMensajes();
    }
    //metodo para enviar mensaje privado
    private void enviarMensajePrivado() {
        final String mensaje = editTextMensaje.getText().toString().trim();

        if (mensaje.isEmpty()) {
            Toast.makeText(MensajeActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.56.1/Proyecto_Conexiones/enviar_mensaje_privado.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.startsWith("<br")) {
                            Toast.makeText(MensajeActivity.this, "Error inesperado en el servidor", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            Toast.makeText(MensajeActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (success) {
                                editTextMensaje.setText("");
                                obtenerMensajes();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MensajeActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MensajeActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mensaje", mensaje);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //metodo para obtener mensajes del servidor
    private void obtenerMensajes() {
        String url = "http://192.168.56.1/Proyecto_Conexiones/enviar_mensaje_privado.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.startsWith("<br")) {
                            Toast.makeText(MensajeActivity.this, "Error inesperado en el servidor", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                JSONArray messages = jsonObject.getJSONArray("messages");
                                tableLayoutMensajes.removeAllViews();

                                for (int i = 0; i < messages.length(); i++) {
                                    String mensaje = messages.getString(i);

                                    TableRow row = new TableRow(MensajeActivity.this);
                                    row.addView(createTextView(mensaje));

                                    tableLayoutMensajes.addView(row);
                                }
                            } else {
                                Toast.makeText(MensajeActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MensajeActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MensajeActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        return textView;
    }
}