package com.example.aplicaciontfg;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class PuntosActivity extends AppCompatActivity {

    private TableLayout tableLayoutPuntos;
    private Button btnVolverPuntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos);
        tableLayoutPuntos = findViewById(R.id.tableLayoutPuntos);
        btnVolverPuntos = findViewById(R.id.btnVolverPuntos);

        btnVolverPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        añadirEncabezados();

        cargarUsuarios();
    }

    private void añadirEncabezados() {
        TableRow filaEncabezados = new TableRow(this);
        filaEncabezados.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView encabezadoID = new TextView(this);
        encabezadoID.setText("ID Usuario");
        encabezadoID.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        encabezadoID.setPadding(16, 16, 16, 16);
        encabezadoID.setTypeface(null, Typeface.BOLD);

        TextView encabezadoNombre = new TextView(this);
        encabezadoNombre.setText("Nombre Usuario");
        encabezadoNombre.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        encabezadoNombre.setPadding(16, 16, 16, 16);
        encabezadoNombre.setTypeface(null, Typeface.BOLD);

        TextView encabezadoPuntos = new TextView(this);
        encabezadoPuntos.setText("Puntos");
        encabezadoPuntos.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        encabezadoPuntos.setPadding(16, 16, 16, 16);
        encabezadoPuntos.setTypeface(null, Typeface.BOLD);

        filaEncabezados.addView(encabezadoID);
        filaEncabezados.addView(encabezadoNombre);
        filaEncabezados.addView(encabezadoPuntos);

        tableLayoutPuntos.addView(filaEncabezados);
    }
    //metodo para cargar usuarios
    public void cargarUsuarios() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_puntos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray usuarios = new JSONArray(response);
                            for (int i = 0; i < usuarios.length(); i++) {
                                JSONObject usuario = usuarios.getJSONObject(i);
                                TableRow fila = new TableRow(PuntosActivity.this);
                                fila.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                TextView idUsuario = new TextView(PuntosActivity.this);
                                idUsuario.setText(String.valueOf(usuario.getInt("ID_usuario")));
                                idUsuario.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView nombreUsuario = new TextView(PuntosActivity.this);
                                nombreUsuario.setText(usuario.getString("Nombre_usuario"));
                                nombreUsuario.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView puntosUsuario = new TextView(PuntosActivity.this);
                                puntosUsuario.setText(String.valueOf(usuario.getInt("puntos")));
                                puntosUsuario.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                fila.addView(idUsuario);
                                fila.addView(nombreUsuario);
                                fila.addView(puntosUsuario);

                                tableLayoutPuntos.addView(fila);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(PuntosActivity.this, "Error al procesar la respuesta JSON" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PuntosActivity.this, "Error en la solicitud" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}