package com.cdp.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout user,contrasenia;
    RequestQueue requestQueue;
    Spinner spiRol;
    String URL;
    //variables que guardaran lo que escriba es usuario
    String usuario;
    String contra;

    //para el control de sesion activa NO TOCAR
    SharedPreferences sp;
    //No tocar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//para bloquear el giro de pantalla
        verificarSesion();

        requestQueue= Volley.newRequestQueue(this);
        user=(TextInputLayout) findViewById(R.id.usuario);
        contrasenia=(TextInputLayout) findViewById(R.id.contrasenia);
        spiRol = findViewById(R.id.spirol);

    }
    public void registrar(View view){
        Intent intent= new Intent(LoginActivity.this,RegistroActivity.class);
        startActivity(intent);
        finish();
    }
    public void validarDatos(View view){

        usuario=user.getEditText().getText().toString().trim();
        contra=contrasenia.getEditText().getText().toString().trim();

        String r = String.valueOf(spiRol.getSelectedItem());
        if(!user.getEditText().getText().toString().equals("") && !contrasenia.getEditText().getText().toString().equals("")){

            if(r.equals("Adulto")){
                URL = "https://bdconandroidstudio.000webhostapp.com/verificarUserAdulto.php?nombre_a="+usuario+"&contrasenia="+contra;

                    verificarLogin(URL,contra,"adulto");


            }
            if(r.equals("Responsable")){
                URL = "https://bdconandroidstudio.000webhostapp.com/verificarUserResp.php?nombre_r="+usuario+"&contrasenia="+contra;

                verificarLogin(URL,contra,"responsable");

            }

        }else{
            Toast.makeText(LoginActivity.this, "Usuario o contraseña vacíos", Toast.LENGTH_LONG).show();
        }

    }


    private void verificarLogin(String URL,String contra,String rol){
        // Toast.makeText(getApplicationContext(), "se hizo la consulta", Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String contrasenia="";
                        try {
                            contrasenia= response.getString("contrasenia").toString();


                            if (contrasenia.equals(contra)) {
                                Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                if(rol.equals("adulto")) {
                                    guardarSesion("Adulto");
                                    Intent intent = new Intent(LoginActivity.this, mainAdulto2.class);
                                    intent.putExtra("usuarioLogin",usuario);
                                    intent.putExtra("contraseniaLogin",contra);
                                    startActivity(intent);
                                    finish();
                                }
                                if(rol.equals("responsable")){
                                    guardarSesion("Responsable");
                                    Intent intent= new Intent(LoginActivity.this,MainActivity.class);//ventana del responsable
                                    intent.putExtra("usuarioLogin",usuario);
                                    intent.putExtra("contraseniaLogin",contra);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "Usuario, contraseña o rol incorrectos", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        );


        requestQueue.add(jsonObjectRequest);


    }

    private void guardarSesion(String rols) {
        try {
            sp=getSharedPreferences("PruebaLogin", Context.MODE_PRIVATE);
            //para insertar datos
            SharedPreferences.Editor spe = sp.edit();
            spe.putString("user",usuario);
            spe.putString("pass",contra);
            spe.putString("rol",rols);
            spe.commit();
        }catch (Exception e){

        }
    }
    private void verificarSesion() {
        SharedPreferences sp=getSharedPreferences("PruebaLogin", Context.MODE_PRIVATE);
        String u=sp.getString("user","null");
        String p=sp.getString("pass","null");
        String r=sp.getString("rol","null");
        if(!u.equals("null") && !p.equals("null")){
            if(r.equals("Adulto")){
                Intent intent = new Intent(LoginActivity.this,mainAdulto2.class);
                intent.putExtra("usuarioLogin",u);
                intent.putExtra("contraseniaLogin",p);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("usuarioLogin",u);
                intent.putExtra("contraseniaLogin",p);
                startActivity(intent);
                finish();
            }

        }else{

        }
    }
}