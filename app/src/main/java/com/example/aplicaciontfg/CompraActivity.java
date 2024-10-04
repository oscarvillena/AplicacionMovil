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

public class CompraActivity extends AppCompatActivity {

    private TableLayout tableLayout_compra;
    private EditText etIDLista, etIDCompra, etDescripcionCompra, etIDUsuario_compra, etNProductos;
    private Button btnCrear_compra, btnModificar_compra, btnEliminar_compra, btnVolver_compra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        tableLayout_compra = findViewById(R.id.tableLayout);
        etIDLista = findViewById(R.id.etID_lista);
        etIDCompra = findViewById(R.id.etID_compra);
        etDescripcionCompra = findViewById(R.id.etDescripcion);
        etIDUsuario_compra = findViewById(R.id.etID_usuario);
        etNProductos = findViewById(R.id.etN_productos);
        btnCrear_compra = findViewById(R.id.btnCrear);
        btnModificar_compra = findViewById(R.id.btnModificar);
        btnEliminar_compra = findViewById(R.id.btnEliminar);
        btnVolver_compra = findViewById(R.id.btnVolver);

        btnCrear_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearCompra();
            }
        });

        btnModificar_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarCompra();
            }
        });

        btnEliminar_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCompra();
            }
        });

        btnVolver_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cargarCompras();
    }
    //metodo para cargar las compras
    private void cargarCompras() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_compras.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            tableLayout_compra.removeAllViews();
                            agregarCabecera();

                            JSONArray compras = new JSONArray(response);
                            for (int i = 0; i < compras.length(); i++) {
                                JSONObject compra = compras.getJSONObject(i);
                                TableRow fila = new TableRow(CompraActivity.this);
                                fila.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                TextView idLista = new TextView(CompraActivity.this);
                                idLista.setText(String.valueOf(compra.getInt("ID_lista")));
                                idLista.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView idCompra = new TextView(CompraActivity.this);
                                idCompra.setText(String.valueOf(compra.getInt("id_compra")));
                                idCompra.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView descripcionCompra = new TextView(CompraActivity.this);
                                descripcionCompra.setText(compra.getString("Descripcion"));
                                descripcionCompra.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView idUsuario = new TextView(CompraActivity.this);
                                idUsuario.setText(String.valueOf(compra.getInt("ID_usuario")));
                                idUsuario.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                TextView nProductos = new TextView(CompraActivity.this);
                                nProductos.setText(String.valueOf(compra.getInt("N_Productos")));
                                nProductos.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

                                fila.addView(idLista);
                                fila.addView(idCompra);
                                fila.addView(descripcionCompra);
                                fila.addView(idUsuario);
                                fila.addView(nProductos);

                                tableLayout_compra.addView(fila);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CompraActivity.this, "Error en cargar" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CompraActivity.this, "Error en cargar", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
    //metodo para agregar las cabeceras de la tabla
    private void agregarCabecera() {
        TableRow cabecera = new TableRow(this);
        cabecera.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView tvIDLista = new TextView(this);
        tvIDLista.setText("ID Lista");
        tvIDLista.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvIDCompra = new TextView(this);
        tvIDCompra.setText("ID Compra");
        tvIDCompra.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvDescripcion = new TextView(this);
        tvDescripcion.setText("Descripción");
        tvDescripcion.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvIDUsuario = new TextView(this);
        tvIDUsuario.setText("ID Usuario");
        tvIDUsuario.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        TextView tvNProductos = new TextView(this);
        tvNProductos.setText("N Productos");
        tvNProductos.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));

        cabecera.addView(tvIDLista);
        cabecera.addView(tvIDCompra);
        cabecera.addView(tvDescripcion);
        cabecera.addView(tvIDUsuario);
        cabecera.addView(tvNProductos);

        tableLayout_compra.addView(cabecera);
    }
    //metodo para crear la comra
    private void crearCompra() {
        String idLista = etIDLista.getText().toString().trim();
        String idCompra = etIDCompra.getText().toString().trim();
        String descripcion = etDescripcionCompra.getText().toString().trim();
        String idUsuario = etIDUsuario_compra.getText().toString().trim();
        String nProductos = etNProductos.getText().toString().trim();

        if (idLista.isEmpty() || idCompra.isEmpty() || descripcion.isEmpty() || idUsuario.isEmpty() || nProductos.isEmpty()) {
            Toast.makeText(CompraActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_compras.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CompraActivity.this, "Compra creada", Toast.LENGTH_SHORT).show();
                        etIDLista.setText("");
                        etIDCompra.setText("");
                        etDescripcionCompra.setText("");
                        etIDUsuario_compra.setText("");
                        etNProductos.setText("");
                        tableLayout_compra.removeAllViews();
                        cargarCompras();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CompraActivity.this, "Error al crear la compra" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_lista", idLista);
                params.put("id_compra", idCompra);
                params.put("Descripcion", descripcion);
                params.put("ID_usuario", idUsuario);
                params.put("N_Productos", nProductos);
                return params;
            }
        };

        queue.add(stringRequest);
    }
    //metodo para modificar la compra
    private void modificarCompra() {
        String idLista = etIDLista.getText().toString().trim();
        String idCompra = etIDCompra.getText().toString().trim();
        String descripcion = etDescripcionCompra.getText().toString().trim();
        String idUsuario = etIDUsuario_compra.getText().toString().trim();
        String nProductos = etNProductos.getText().toString().trim();

        if (idLista.isEmpty() || idCompra.isEmpty() || descripcion.isEmpty() || idUsuario.isEmpty() || nProductos.isEmpty()) {
            Toast.makeText(CompraActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_compras.php";

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CompraActivity.this, "Compra modificada exitosamente", Toast.LENGTH_SHORT).show();
                        etIDLista.setText("");
                        etIDCompra.setText("");
                        etDescripcionCompra.setText("");
                        etIDUsuario_compra.setText("");
                        etNProductos.setText("");
                        tableLayout_compra.removeAllViews();
                        cargarCompras();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CompraActivity.this, "Error al modificar la compra" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_lista", idLista);
                params.put("id_compra", idCompra);
                params.put("Descripcion", descripcion);
                params.put("ID_usuario", idUsuario);
                params.put("N_Productos", nProductos);
                return params;
            }
        };

        queue.add(stringRequest);
    }
    //metodo para elimiar la compra
    private void eliminarCompra() {
        String idLista = etIDLista.getText().toString().trim();
        String idCompra = etIDCompra.getText().toString().trim();

        if (idLista.isEmpty() || idCompra.isEmpty()) {
            Toast.makeText(CompraActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.56.1/Proyecto_Conexiones/gestion_compras.php?ID_lista=" + idLista + "&id_compra=" + idCompra;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CompraActivity.this, "Compra eliminada", Toast.LENGTH_SHORT).show();
                        etIDLista.setText("");
                        etIDCompra.setText("");
                        tableLayout_compra.removeAllViews();
                        cargarCompras();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CompraActivity.this, "Error al eliminar la compra" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}