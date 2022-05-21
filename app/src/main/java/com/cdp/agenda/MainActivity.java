package com.cdp.agenda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cdp.agenda.db.DbContactos;
import com.cdp.agenda.entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {


    private Spinner spinner;
    private Button boton;
    private Button button;
    private Button idverLista;
    private Button verUbicacion;
    private RequestQueue requestQueue;
    private ArrayList<String> listAdultos;
    private boolean actualizado;
    //para control de sesion NOTOCAR
    SharedPreferences sp;
    //notocar

    SearchView txtBuscar;
    //RecyclerView listaContactos;
    ArrayList<Contactos> listaArrayContactos;
    FloatingActionButton fabNuevo;
    //ListaContactosAdapter adapter;


    //para recibir los valores de login
    Bundle getUserR,getContraR;
    String nomAdulto;
    //para guardar los valores recibidos de login
    String nameGetR,passwordGetR;
    ///fin

    //para mostrar nombre del usuario en pantalla despues de logear
    TextView nameuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.programar);
        actualizado=false;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//para bloquear el giro de pantalla
        sp=getSharedPreferences("PruebaLogin", Context.MODE_PRIVATE);

        requestQueue= Volley.newRequestQueue(this);
        listAdultos=new ArrayList<String>();
        if(getIntent().getExtras()!=null){
            //recibimos los valores
            getUserR=getIntent().getExtras();
            getContraR=getIntent().getExtras();
            //guardamos los valores
            nameGetR=getUserR.getString("usuarioLogin");
            passwordGetR=getContraR.getString("contraseniaLogin");
            //fin, ahora pueden usar las variables nameGetA,passwordGetA como requieran

        }

        //inicializar componentes
        spinner = findViewById(R.id.spinner);
        boton = (Button) findViewById(R.id.button2);
        nameuser=findViewById(R.id.nameUser);
        nameuser.setText(nameGetR);
        idverLista= (Button) findViewById(R.id.idverLista);
        verUbicacion=findViewById(R.id.btnVerubic);
        //Acciones del boton
         idverLista.setOnClickListener(new View.OnClickListener() { //actualizar lista
            @Override
            public void onClick(View view) {
                if(spinner.getSelectedItem().toString()!=null) {
                    nomAdulto = spinner.getSelectedItem().toString();
                    Intent intent = new Intent(MainActivity.this, mainAdulto2.class);
                    intent.putExtra("usuarioLogin", nomAdulto);
                    intent.putExtra("tipoDeUsuario", "responsable");
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Seleccione un Adulto", Toast.LENGTH_LONG).show();
                }
            }
         });


           boton.setOnClickListener(new View.OnClickListener() {//boton actualuzar spinner creo
            @Override
            public void onClick(View view) {
                    listAdultos.clear();
                    String URL = "https://bdconandroidstudio.000webhostapp.com/adultosACargoDeResp.php?r_nombre=" + nameGetR;
                    buscarAdultos(URL);

            }
           });


            button = (Button)findViewById(R.id.button);

            button.setOnClickListener(new View.OnClickListener() { //programar evento del adulto mayor
                @Override
                public void onClick(View view) {
                    if(spinner.getSelectedItem().toString()!=null) {
                        nomAdulto = spinner.getSelectedItem().toString();
                        Intent i = new Intent(MainActivity.this, programareventodeAdultoMayor.class);
                        i.putExtra("usuarioLogin", nomAdulto);
                        startActivity(i);
                    }else{
                        Toast.makeText(MainActivity.this, "Seleccione un Adulto", Toast.LENGTH_LONG).show();
                    }
                }
            });
            verUbicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getUbicacion(spinner.getSelectedItem().toString());
                    /*Intent intent= new Intent(MainActivity.this,MapsActivity.class);
                    intent.putExtra("nameA",spinner.getSelectedItem().toString());
                    startActivity(intent);*/
                }
            });
    }


    private void llenarSpinner() {

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listAdultos);
        spinner.setAdapter(adapter1);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,"Adulto seleccionado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtBuscar = findViewById(R.id.txtBuscar);
        //listaContactos = findViewById(R.id.listaContactos);
        fabNuevo = findViewById(R.id.favNuevo);
        //listaContactos.setLayoutManager(new LinearLayoutManager(this));

        DbContactos dbContactos = new DbContactos(MainActivity.this);

        listaArrayContactos = new ArrayList<>();

        //adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        //listaContactos.setAdapter(adapter);

        //fabNuevo.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        nuevoRegistro();
        //    }
        //});
        //txtBuscar.setOnQueryTextListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_resp, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        //evaluar para que nos traiga el id del elemento del menu selecionado
        switch (item.getItemId()){
            case R.id.cerrarR:
                cerrarSR();
                return true;

            case R.id.asociarAdulto:
                asociarAdulto();
                return true;
            case R.id.ayudaR:
                tutorialR();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void tutorialR(){
        Intent intent=new Intent(MainActivity.this,ayudaResponsable.class);
        startActivity(intent);

    }

    public void asociarAdulto(){
        Intent intent=new Intent(MainActivity.this,asociarAdultoActivity.class);
        intent.putExtra("nombreResp",nameGetR);
        startActivity(intent);
    }
    public void cerrarSR(){

        //para insertar datos
        SharedPreferences.Editor spe = sp.edit();
        spe.putString("user","null");
        spe.putString("pass","null");
        spe.putString("rol","null");
        spe.commit();

        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();

    }

    private void buscarAdultos(String URL){
        //Toast.makeText(getApplicationContext(), "se hizo la consulta1", Toast.LENGTH_SHORT).show();
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for(int i=0;i< response.length();i++) {
                    try {
                        //jsonObject = response.getJSONObject(0);
                        //txtTitulo.setText(response.getString(0));
                        listAdultos.add(response.getString(i));
                      //  Toast.makeText(getApplicationContext(),"ingreso consulta", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                llenarSpinner();// se llena el spiner
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void getUbicacion(String nameA){
        // Toast.makeText(getApplicationContext(), "se hizo la consulta", Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(
                Request.Method.GET,
                "https://bdconandroidstudio.000webhostapp.com/verSoloNomAdulJson.php"+"?nombre_a="+nameA,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getApplicationContext(), "se hizo la consulta", Toast.LENGTH_SHORT).show();
                        String latitud;
                        String longitud;
                        try {
                            latitud= response.getString("latitud").toString();
                            longitud= response.getString("longitud").toString();
                            Intent intent= new Intent(MainActivity.this,MapsActivity.class);
                            intent.putExtra("latitud",latitud);
                            intent.putExtra("longitud",longitud);
                            intent.putExtra("nameA",nameA);
                            startActivity(intent);


                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Error al acceder a la Base de datos", Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //cambio para bug 16 y 17
                        Toast.makeText(getApplicationContext(),"Error al acceder a la Base de datos",Toast.LENGTH_SHORT).show();

                    }
                }
        );


        requestQueue.add(jsonObjectRequest);

    }

}