package com.cdp.agenda;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class asociarAdultoActivity extends AppCompatActivity {
    private EditText txtNombreAdulto,txtClaveCnx;
    private Button btnAsociar;
    private RequestQueue requestQueue;
    private String nomAdulto;
    private String claveCnx;
    private String nomResponsable;
    private ArrayList<String> listaAdultos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asociar_adulto);
        txtNombreAdulto = findViewById(R.id.txtNomAdulto);
        txtClaveCnx = findViewById(R.id.txtClaveCnx);
        btnAsociar = findViewById(R.id.btnAsociar);
       listaAdultos=(ArrayList<String>) getIntent().getStringArrayListExtra("listaAdultos");
        nomResponsable=getIntent().getStringExtra("nombreResp");
        configurarPantalla();
        btnAsociar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    verificarNomUsuario();

            }
        });
    }

    private void configurarPantalla(){
        DisplayMetrics medidasVentana= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);
        requestQueue= Volley.newRequestQueue(this);
        int ancho = medidasVentana.widthPixels;
        int alto  = medidasVentana.widthPixels;

        getWindow().setLayout((int)(ancho*0.85),(int)(alto * 0.75));

    }
    private void verificarNomUsuario(){
        nomAdulto = txtNombreAdulto.getText().toString().trim();
        claveCnx = txtClaveCnx.getText().toString().trim();

        if(!listaAdultos.contains(nomAdulto)) {
            Toast.makeText(getApplicationContext(), "Buscando...", Toast.LENGTH_SHORT).show();
            String URL = "https://bdconandroidstudio.000webhostapp.com/verSoloNomAdulJson.php?nombre_a=" + nomAdulto;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String claveConexion = "";
                            try {
                                claveConexion = response.getString("clave_con");
                                if (claveConexion.equals(claveCnx)) {
                                    asociar();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Nombre incorrecto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Usuario, contraseña o rol incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }else{
            Toast.makeText(getApplicationContext(), "Ya tienes agregado a " + nomAdulto, Toast.LENGTH_LONG).show();
        }
    }

    public void asociar(){
        String URL="https://bdconandroidstudio.000webhostapp.com/asociar.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Dispositivos:"+nomResponsable+" y "+nomAdulto+" asociado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("r_nombre",nomResponsable);
                parametros.put("a_nombre",nomAdulto);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);

     }
    }