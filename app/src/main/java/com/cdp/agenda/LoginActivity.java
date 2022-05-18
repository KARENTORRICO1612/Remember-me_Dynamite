package com.cdp.agenda;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
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
    String  tUser;

    //para el control de sesion activa NO TOCAR
    SharedPreferences sp;
    //No tocar

    int REQUEST_CODE = 200;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarPermisos();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//para bloquear el giro de pantalla
        verificarSesion();

        requestQueue= Volley.newRequestQueue(this);
        user=(TextInputLayout) findViewById(R.id.usuario);
        contrasenia=(TextInputLayout) findViewById(R.id.contrasenia);
        spiRol = findViewById(R.id.spirol);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void verificarPermisos(){
        int PermisosUbicacion = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (PermisosUbicacion == PackageManager.PERMISSION_GRANTED){
            //MetodoMandar Mensajes
            Toast.makeText(this, "Permiso de ubicación habilitado", Toast.LENGTH_SHORT).show();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    public void registrar(View view){
        Intent intent= new Intent(LoginActivity.this,RegistroActivity.class);
        startActivity(intent);
        finish();
    }
    public void validarDatos(View view){

        usuario=user.getEditText().getText().toString();//cambio para bug 16 y 17
        contra=contrasenia.getEditText().getText().toString();//cambio para bug 16 y 17


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

                                if(rol.equals("adulto")) {
                                    tUser="adulto";
                                    guardarSesion("Adulto");
                                    Intent intent = new Intent(LoginActivity.this, mainAdulto2.class);
                                    intent.putExtra("tipoDeUsuario",tUser);
                                    intent.putExtra("usuarioLogin",usuario);
                                    intent.putExtra("contraseniaLogin",contra);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "Bienvenido "+usuario, Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                if(rol.equals("responsable")){
                                    tUser="responsable";
                                    guardarSesion("Responsable");
                                    Intent intent= new Intent(LoginActivity.this,MainActivity.class);//ventana del responsable
                                    intent.putExtra("tipoDeUsuario",tUser);
                                    intent.putExtra("usuarioLogin",usuario);
                                    intent.putExtra("contraseniaLogin",contra);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "Bienvenido "+usuario, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),"Usuario, contraseña o rol incorrectos",Toast.LENGTH_SHORT).show();
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
            spe.putString("tUser",tUser);
            spe.putString("rol",rols);
            spe.commit();
        }catch (Exception e){

        }
    }
    private void verificarSesion() {
        SharedPreferences sp=getSharedPreferences("PruebaLogin", Context.MODE_PRIVATE);
        String u=sp.getString("user","null");
        String p=sp.getString("pass","null");
        String tU=sp.getString("tUser","null");
        String r=sp.getString("rol","null");
        if(!u.equals("null") && !p.equals("null")){
            if(r.equals("Adulto")){
                Intent intent = new Intent(LoginActivity.this,mainAdulto2.class);
                intent.putExtra("usuarioLogin",u);
                intent.putExtra("contraseniaLogin",p);
                intent.putExtra("tipoDeUsuario",tU);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("usuarioLogin",u);
                intent.putExtra("contraseniaLogin",p);
                intent.putExtra("tipoDeUsuario",tU);
                startActivity(intent);
                finish();
            }

        }else{

        }
    }
}