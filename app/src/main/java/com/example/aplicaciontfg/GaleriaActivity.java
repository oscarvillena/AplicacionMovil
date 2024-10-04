package com.example.aplicaciontfg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class GaleriaActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private Button buttonSubirImagen;
    private Button buttonSubir;
    private  Button buttonVolver_Galeria;
    private ImageView imageView;
    private EditText editTextNombreImagen;
    private GridView gridViewGaleria;
    private ArrayList<String> imageUrls;
    private GaleriaAdapter adapter;
    private byte[] imagenByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        cargarImagenes();
        buttonVolver_Galeria = findViewById(R.id.buttonVolver);
        buttonSubirImagen = findViewById(R.id.buttonSubirImagen);
        buttonSubir = findViewById(R.id.buttonSubir);
        imageView = findViewById(R.id.imageView);
        editTextNombreImagen = findViewById(R.id.editTextNombreImagen);
        gridViewGaleria = findViewById(R.id.gridViewGaleria);
        imageUrls = new ArrayList<>();
        adapter = new GaleriaAdapter(this, imageUrls);
        gridViewGaleria.setAdapter(adapter);

        buttonVolver_Galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSubirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
            }
        });

        buttonSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreImagen = editTextNombreImagen.getText().toString().trim();
                if (nombreImagen.isEmpty()) {
                    Toast.makeText(GaleriaActivity.this, "Ingrese nombre para la imagen", Toast.LENGTH_SHORT).show();
                } else {
                    subirImagen(imagenByteArray, nombreImagen);
                }
            }
        });
        cargarImagenes();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getData() != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imagenByteArray = stream.toByteArray();
                    Glide.with(this)
                            .load(bitmap)
                            .into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                    editTextNombreImagen.setVisibility(View.VISIBLE);
                    buttonSubir.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al cargar", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //metodo para subir imagen
    private void subirImagen(final byte[] imagen, final String nombreImagen) {
        String url = "http://192.168.56.1/Proyecto_Conexiones/subir_imagen.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        imageView.setVisibility(View.GONE);
                        editTextNombreImagen.setVisibility(View.GONE);
                        buttonSubir.setVisibility(View.GONE);
                        cargarImagenes();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(GaleriaActivity.this, "Error al subir", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String imagenBase64 = Base64.encodeToString(imagen, Base64.DEFAULT);
                Log.d("ImagenBase64", imagenBase64);
                params.put("imagen", imagenBase64);
                params.put("nombre", nombreImagen);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //metodo para cargar las imagens
    private void cargarImagenes() {
        String url = "http://192.168.56.1/Proyecto_Conexiones/obtener_imagenes.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    imageUrls.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String imageName = jsonObject.getString("nombre_imagen");
                        String imageUrl = "http://192.168.56.1/Proyecto_Conexiones/uploads/" + imageName;
                        imageUrls.add(imageUrl);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GaleriaActivity.this, "Error al cargar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(GaleriaActivity.this, "Error al cargar", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}