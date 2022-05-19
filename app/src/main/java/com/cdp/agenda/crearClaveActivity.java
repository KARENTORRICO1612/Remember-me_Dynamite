package com.cdp.agenda;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class crearClaveActivity extends AppCompatActivity {
    private String clave;
    private String claveConfirmacion;
    private String nomDeUsuario;
    private EditText txtClave,txtConfirmar;
    private Button btnGuardarpopup,btnGenerar,btnReiniciar;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_clave);
        requestQueue= Volley.newRequestQueue(this);
        txtClave= findViewById(R.id.txtClave);
        txtConfirmar=findViewById(R.id.txtConfirmar);
        btnGuardarpopup = findViewById(R.id.btnGuardarpopup);
        btnGenerar=findViewById(R.id.btnGenerar);
        btnReiniciar=findViewById(R.id.btnReiniciar);
        nomDeUsuario=getIntent().getStringExtra("nombreDeUsuario");
        configurarPantalla();
        btnGuardarpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });
    }

    public void guardar(){
        clave = txtClave.getText().toString().trim();
        claveConfirmacion = txtConfirmar.getText().toString().trim();
        if(clave.equals(claveConfirmacion)){
            modificarActivity(clave,nomDeUsuario);
        }else{
            Toast.makeText(getApplicationContext(),"Claves distintas",Toast.LENGTH_SHORT).show();
        }
    }
    public  void configurarPantalla(){
        DisplayMetrics medidasVentana= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        int alto  = medidasVentana.widthPixels;

        getWindow().setLayout((int)(ancho*0.85),(int)(alto * 1.5));

    }

    public void modificarActivity(String c,String nomU){
        String URL="https://bdconandroidstudio.000webhostapp.com/insertarClave.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Guardado", Toast.LENGTH_SHORT).show();
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
                parametros.put("nombre_a",nomU);
                parametros.put("clave_con",clave);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);

    }




}
