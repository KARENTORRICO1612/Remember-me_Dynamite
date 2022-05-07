package com.cdp.agenda;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
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

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    EditText iNombre, iContra, iConfiContra;
    Button btnRegistrar;

    CheckBox cbMostrarConta, cbMostrarConfiContra;

    Spinner spiRol;

    RequestQueue requestQueue;
    private static final String URL1 = "https://coco117.000webhostapp.com/in.php";
    private static final String URL2 = "https://coco117.000webhostapp.com/regUsuario.php";
    private static final String URL3 = "https://bdconandroidstudio.000webhostapp.com/registrar_adulto.php";
    private static final String URL4 = "https://bdconandroidstudio.000webhostapp.com/registrar_resp.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//para bloquear el giro de pantalla
        requestQueue = Volley.newRequestQueue(this);

        initUi();

        btnRegistrar.setOnClickListener(this);
    }

    private void initUi() {
        iNombre = findViewById(R.id.inputnombre);
        iContra = findViewById(R.id.inputcontra);
        iConfiContra = findViewById(R.id.inputconficontra);

        spiRol = findViewById(R.id.spirol);

        btnRegistrar = findViewById(R.id.btnregistrar);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btnregistrar){
            String nom = iNombre.getText().toString().trim();
            String con = iContra.getText().toString().trim();
            String confiCon = iConfiContra.getText().toString().trim();
            String r = String.valueOf(spiRol.getSelectedItem());
            if(!nom.equals("") && !con.equals("") && !confiCon.equals("")){
                if(nom.length()>= 6 && con.length()>= 6){
                    if(nom.length()<= 20 && con.length()<=20 ){
                        if(con.equals(confiCon)){
                            if(r.equals("Adulto")){
                                registrarAdulto(nom, con);
                            }
                            if(r.equals("Responsable")){
                                registrarResponsable(nom, con);
                            }
                        }else{
                            Toast.makeText(RegistroActivity.this, "Las contraseñas no Coinciden", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegistroActivity.this, "Nombre de Usuario y Contraseña deben tener menos de 20 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegistroActivity.this, "Nombre de Usuario y Contraseña deben tener por lo menos 6 caracteres", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegistroActivity.this, "Debe llenar todo los campos.", Toast.LENGTH_SHORT).show();
            }




        }

    }

    private void registrar(final String nom, final String con, final String r) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegistroActivity.this, "Registrado", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nom);
                params.put("contra",con);
                params.put("rol", r);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void registrarAdulto(final String nom, final String con) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegistroActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(RegistroActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre_a", nom);
                params.put("contrasenia",con);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void registrarResponsable(final String nom, final String con) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegistroActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre_r", nom);
                params.put("contrasenia",con);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void volverLogin(View view){
        Intent intent= new Intent(RegistroActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}