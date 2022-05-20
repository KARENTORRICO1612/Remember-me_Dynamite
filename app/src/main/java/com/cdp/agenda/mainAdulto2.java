package com.cdp.agenda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdp.agenda.adaptadores.ListaContactosAdapter;
import com.cdp.agenda.entidades.Contactos;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mainAdulto2 extends AppCompatActivity implements SearchView.OnQueryTextListener, Serializable {
    SearchView txtBuscar;
    RecyclerView listaContactos;
    ArrayList<Contactos> listaArrayContactos;
    FloatingActionButton fabNuevo;
    ListaContactosAdapter adapter;
    RequestQueue requestQueue;
    String tipoDeUsuario="adulto";
    //para recibir los valores de login
    Bundle getUserA,getContraA,getTU;
    //para guardar los valores recibidos de login
    String nameGetA,passwordGetA;
    ///fin

    //para el control de sesion NO TOCAR
    SharedPreferences sp;
    //no tocar

    //para mostrar nombre del usuario en pantalla
    TextView nameuser2;
    public static final int REQUEST_CODE = 1;
    private boolean salio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_adulto2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//para bloquear el giro de pantalla
        requestQueue = Volley.newRequestQueue(this);
        sp = getSharedPreferences("PruebaLogin", Context.MODE_PRIVATE);
      //  tipoDeUsuario=getIntent().getStringExtra("tipoDeUsuario");
        txtBuscar = findViewById(R.id.txtBuscar);
        listaContactos = findViewById(R.id.listaContactos);
        fabNuevo = findViewById(R.id.favNuevo);
        nameuser2=findViewById(R.id.nameUs);
        salio=false;
        listaContactos.setLayoutManager(new LinearLayoutManager(this));

          if(getIntent().getExtras()!=null) {

          }
            //recibimos los valores
            getUserA=getIntent().getExtras();
            getContraA=getIntent().getExtras();
            getTU=getIntent().getExtras();
            //guardamos los valores
            nameGetA=getUserA.getString("usuarioLogin");
            passwordGetA=getContraA.getString("contraseniaLogin");
             tipoDeUsuario = getTU.getString("tipoDeUsuario");

          // Toast.makeText(mainAdulto2.this, tipoDeUsuario+"nombreAdulto: "+nameGetA, Toast.LENGTH_LONG).show();
            //fin, ahora pueden usar las variables nameGetA,passwordGetA como requieran
        if(tipoDeUsuario.equals("responsable")){
            fabNuevo.setVisibility(View.INVISIBLE);//bug
            nameuser2.setText("");
        }else{
            nameuser2.setText(nameGetA);
            ObtenerCoordendasActual();
        }
        //
        //DbContactos dbContactos = new DbContactos(mainAdulto2.this);
        listaArrayContactos = new ArrayList<>();
        //adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        //listaContactos.setAdapter(adapter);
        enListarRecordatorios(nameGetA);
       // insertarUbicacion(nameGetA,156+"",784+"");

        fabNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoRegistro();
            }
        });

        txtBuscar.setOnQueryTextListener(this);
    }
    public void enListarRecordatorios(String nomAdul) {  //muestra una lista con los contacts
        String URL = "https://bdconandroidstudio.000webhostapp.com/recordatoriosDeUnAdulto.php?adulto_r="+nomAdul;
        ArrayList<Contactos> lista = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i=0;i< response.length();i++) {
                    try {

                        Contactos contacto=new Contactos();
                        JSONObject jsonObject = null;
                        jsonObject = response.getJSONObject(i);
                        contacto.setId(Integer.parseInt(jsonObject.getString("id")));
                        contacto.setTitulo(jsonObject.getString("titulo"));
                        contacto.setHora(jsonObject.getString("hora"));
                        contacto.setFecha(jsonObject.getString("fecha"));
                        contacto.setDireccion(jsonObject.getString("direccion"));
                        contacto.setDescripcion(jsonObject.getString("descripcion"));
                        contacto.setAdulto_r(jsonObject.getString("adulto_r"));
                        lista.add(contacto);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                // llenar();
                adapter = new ListaContactosAdapter(lista,tipoDeUsuario,mainAdulto2.this);
                listaContactos.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonArrayRequest);



    }

    public void insertarUbicacion(String nomAdulto,String latitud,String longitud){
        String URL="https://bdconandroidstudio.000webhostapp.com/insertarUbicacion.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), "Ubicación Guardada", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("nombre_a",nomAdulto);
                parametros.put("latitud",latitud);
                parametros.put("longitud",longitud);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);

    }




    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        if(tipoDeUsuario.equals("responsable")){
            //no mostramos ningun menu
        }else{
            inflater.inflate(R.menu.menu_adulto, menu);
        }

        return true;
    }

     public boolean onOptionsItemSelected(MenuItem item){
       //evaluar para que nos traiga el id del elemento del menu selecionado
        switch (item.getItemId()){
            //para adulto
            case R.id.cerrarA:
                cerrarS();
                return true;
            case R.id.crearClave:
                crearClave();
                return true;
            case R.id.ayudaA:
                tutorialA();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void tutorialA(){
        Intent intent=new Intent(mainAdulto2.this,ayudaAdulto.class);
        startActivity(intent);

    }
    public void crearClave(){
        Intent intent=new Intent(mainAdulto2.this,crearClaveActivity.class);
        intent.putExtra("nombreDeUsuario",nameGetA);
        startActivity(intent);
    }
    public void asociarAdulto(){
        Intent intent=new Intent(mainAdulto2.this,asociarAdultoActivity.class);
        startActivity(intent);
    }


    public void cerrarS(){

        //para insertar datos
        SharedPreferences.Editor spe = sp.edit();
        spe.putString("user","null");
        spe.putString("pass","null");
        spe.putString("rol","null");
        spe.commit();
        salio=true;
        Intent intent=new Intent(mainAdulto2.this,LoginActivity.class);
        startActivity(intent);
        salio=true;
        finish();

    }

    private void nuevoRegistro(){
        Intent intent = new Intent(mainAdulto2.this, NuevoActivity.class);
        intent.putExtra("nombreAdulto",nameGetA);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void ObtenerCoordendasActual() {


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(mainAdulto2.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        } else {

            getCoordenada();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCoordenada();
            } else {
                Toast.makeText(this, "Permiso Denegado ..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCoordenada() {

        try {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                @SuppressLint("MissingPermission")
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(mainAdulto2.this).removeLocationUpdates(this);
                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                        int latestLocationIndex = locationResult.getLocations().size() - 1;
                        double latitud = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                        double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                        insertarUbicacion(nameGetA,""+latitud,""+longitude);
                        if(salio==false){
                            countDownTimer();
                        }

                    }

                }

            }, Looper.myLooper());

        }catch (Exception ex){
            System.out.println("Error es :" + ex);
        }
    }

    private void countDownTimer() {
        if(salio==false){
            new CountDownTimer(15000, 1000) {

                public void onTick(long millisUntilFinished) {
                    // Toast.makeText(MapsActivity.this, "Faltan "+millisUntilFinished/1000+" segundos", Toast.LENGTH_SHORT).show();

                }

                public void onFinish() {
                    ObtenerCoordendasActual();
                    Toast.makeText(mainAdulto2.this, "Ubicación actualizada", Toast.LENGTH_SHORT).show();
                }
            }.start();
        }

    }
}
