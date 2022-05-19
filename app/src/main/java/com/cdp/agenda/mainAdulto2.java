package com.cdp.agenda;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
    String tipoDeUsuario;
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
                Toast.makeText(getApplicationContext(), "Guardado", Toast.LENGTH_SHORT).show();
                //poner aqui lo que se quierer ejecutar despues de esta consulta
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
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

        Intent intent=new Intent(mainAdulto2.this,LoginActivity.class);
        startActivity(intent);
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
}
