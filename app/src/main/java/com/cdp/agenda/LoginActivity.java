package com.cdp.agenda;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    private static final int REQUEST_PERMISSION_LOCATION = 100;

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
        solicitarPermisosUbicacion();

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
                        String userr="";
                        try {

                            contrasenia= response.getString("contrasenia");

                                    if(rol.equals("adulto")){
                                        userr=response.getString("nombre_a");
                                    }else{
                                        userr=response.getString("nombre_r");
                                    }



                            if (contrasenia.equals(contra)) {
                                usuario=userr;
                                if(rol.equals("adulto")) {
                                    tUser="adulto";
                                    guardarSesion("Adulto");
                                    Intent intent = new Intent(LoginActivity.this, mainAdulto2.class);
                                    intent.putExtra("tipoDeUsuario",tUser);
                                    intent.putExtra("usuarioLogin",usuario);
                                    intent.putExtra("contraseniaLogin",contra);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "Bienvenido "+userr, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(LoginActivity.this, "Bienvenido "+userr, Toast.LENGTH_SHORT).show();
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

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.ayudaL:
                tutorialU();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void tutorialU(){
        Intent intent=new Intent(LoginActivity.this,ayudaUsuario.class);
        startActivity(intent);
    }

    public void solicitarPermisosUbicacion(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(this, "La ubicación está habilitada", Toast.LENGTH_SHORT).show();
            verificarSesion();
        }else{
            Log.i("TAG", "API >= 23");
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Log.i("TAG", "Permission granted");
                Toast.makeText(this, "La ubicación está habilitada", Toast.LENGTH_SHORT).show();

            }else{
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                    Log.i("TAG", "the user previously rejected the request");
                }else{
                    Log.i("TAG", "Request permission");
                }
                ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION_LOCATION);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_LOCATION){
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i("TAG", "Permission granted(request)");
                //
                Toast.makeText(this, "Permiso de ubicación habilitado", Toast.LENGTH_SHORT).show(); //ubicación habilitada
                verificarSesion();
            }else{
                Log.i("TAG", "Permission denied(request)");
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){
                    new AlertDialog.Builder(this).setMessage("Necesita habilitar el permiso para que la aplicacion funcione de manera correcta")
                            .setPositiveButton("Intentar Nuevamente", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION_LOCATION);
                                }
                            })
                            .setNegativeButton("No gracias", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //salir
                                    Log.i("TAG", "Leave?");
                                }
                            }).show();
                }else{
                    Toast.makeText(this,"Necesitas habilitar el permiso manualmente para que pueda ser ubicado", Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}