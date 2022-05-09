package com.cdp.agenda;

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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cdp.agenda.adaptadores.ListaContactosAdapter;
import com.cdp.agenda.db.DbContactos;
import com.cdp.agenda.entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class mainAdulto2 extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView txtBuscar;
    RecyclerView listaContactos;
    ArrayList<Contactos> listaArrayContactos;
    FloatingActionButton fabNuevo;
    ListaContactosAdapter adapter;
    RequestQueue requestQueue;

    //para recibir los valores de login
    Bundle getUserA,getContraA;
    //para guardar los valores recibidos de login
    String nameGetA,passwordGetA;
    ///fin

    //para el control de sesion NO TOCAR
    SharedPreferences sp;
    //no tocar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_adulto2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//para bloquear el giro de pantalla
        requestQueue= Volley.newRequestQueue(this);
        sp=getSharedPreferences("PruebaLogin", Context.MODE_PRIVATE);

        txtBuscar = findViewById(R.id.txtBuscar);
        listaContactos = findViewById(R.id.listaContactos);
        fabNuevo = findViewById(R.id.favNuevo);
        listaContactos.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent().getExtras()!=null){
            //recibimos los valores
            getUserA=getIntent().getExtras();
            getContraA=getIntent().getExtras();
            //guardamos los valores
            nameGetA=getUserA.getString("usuarioLogin");
            passwordGetA=getContraA.getString("contraseniaLogin");
            //fin, ahora pueden usar las variables nameGetA,passwordGetA como requieran
        }
        //DbContactos dbContactos = new DbContactos(mainAdulto2.this);
        listaArrayContactos = new ArrayList<>();
        //adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        //listaContactos.setAdapter(adapter);
        enListarRecordatorios(nameGetA);

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
                        lista.add(contacto);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                // llenar();
                adapter = new ListaContactosAdapter(lista);
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




    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

     public boolean onOptionsItemSelected(MenuItem item){
       //evaluar para que nos traiga el id del elemento del menu selecionado
        switch (item.getItemId()){
            case R.id.idCerrar:
                cerrarS();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

}
