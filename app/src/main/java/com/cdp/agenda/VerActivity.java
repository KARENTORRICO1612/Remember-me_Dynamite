package com.cdp.agenda;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdp.agenda.db.DbContactos;
import com.cdp.agenda.entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerActivity extends AppCompatActivity {

    EditText txtTitulo, txtDireccion, txtDescripcion;
    Button btnGuarda;
    FloatingActionButton fabEditar, fabEliminar;
    TextView eFecha,eHora;
    String titulo,hora,fecha,direccion,descripcion;
    String nomAdulto,tipoDeUsuario;
    mainAdulto2 pantallaAnt;
    Bundle main2Anterior;
    Contactos contacto;
    int id = 0;
    private RequestQueue requestQueue;

    //para bug 11 y 12 de la 2corrida 2 sprint
    TextView aFecha,aHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestQueue= Volley.newRequestQueue(this);
        nomAdulto = getIntent().getStringExtra("nombreAdulto");
        tipoDeUsuario=getIntent().getStringExtra("tipoDeUsuario");
        main2Anterior = getIntent().getExtras();
        pantallaAnt=new mainAdulto2();
        pantallaAnt= (mainAdulto2) main2Anterior.getSerializable("main2Anterior");
        txtTitulo = findViewById(R.id.txtTitulo);
        eHora = findViewById(R.id.eHora);
        eFecha = findViewById(R.id.eFecha);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtDescripcion = findViewById(R.id.txtDescripcion);

        //para bug 11 y 12
        aHora=findViewById(R.id.aHora);
        aFecha=findViewById(R.id.aFecha);
        aHora.setBackground(null);
        aFecha.setBackground(null);
        aHora.setTextColor(Color.parseColor("#000000"));
        aFecha.setTextColor(Color.parseColor("#000000"));
        //Fecha.fin bug

        fabEditar = findViewById(R.id.fabEditar);
        fabEliminar = findViewById(R.id.fabEliminar);
        btnGuarda = findViewById(R.id.btnGuarda);
        btnGuarda.setVisibility(View.INVISIBLE);

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }


       // final DbContactos dbContactos = new DbContactos(VerActivity.this);
       // contacto = dbContactos.verContacto(id);
        obtenerDatosEvento("https://bdconandroidstudio.000webhostapp.com/informacionEvento.php?id="+id);
        /*
        if(contacto != null){
            txtTitulo.setText(contacto.getTitulo());
            eHora.setText(contacto.getHora());
            eFecha.setText(contacto.getFecha());
            txtDireccion.setText(contacto.getDireccion());
            txtDescripcion.setText(contacto.getDescripcion());

            txtTitulo.setInputType(InputType.TYPE_NULL);
            eHora.setInputType(InputType.TYPE_NULL);
            eFecha.setInputType(InputType.TYPE_NULL);
            txtDireccion.setInputType(InputType.TYPE_NULL);
            txtDescripcion.setInputType(InputType.TYPE_NULL);
        }

         */

        fabEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerActivity.this, EditarActivity.class);
                intent.putExtra("tipoDeUsuario",tipoDeUsuario);
                intent.putExtra("ID", id+"");
                intent.putExtra("titulo",titulo);
                intent.putExtra("hora",hora);
                intent.putExtra("fecha",fecha);
                intent.putExtra("direccion",direccion);
                intent.putExtra("descripcion",descripcion);
                intent.putExtra("usuarioLogin",nomAdulto);
                startActivity(intent);
               finish();
            }
        });

        fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerActivity.this);
                builder.setMessage("¿Desea eliminar este evento?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            eliminarEvento();
                              //  if(dbContactos.eliminarContacto(id)){

                               // }

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });
    }

    private void lista(){
       // ((Activity) pantallaAnt).finish();
        Intent intent = new Intent(VerActivity.this, mainAdulto2.class);
        intent.putExtra("usuarioLogin",nomAdulto);
        intent.putExtra("tipoDeUsuario",tipoDeUsuario);
        if(tipoDeUsuario.equals("adulto")) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
        finish();
    }
    private void obtenerDatosEvento(String URL){
        // Toast.makeText(getApplicationContext(), "se hizo la consulta", Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(

                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            titulo = response.getString("titulo");
                            hora   = response.getString("hora");
                            fecha = response.getString("fecha");
                            direccion=response.getString("direccion");
                            descripcion=response.getString("descripcion");

                            txtTitulo.setText(titulo);
                            eHora.setText(hora);
                            eFecha.setText(fecha);
                            txtDireccion.setText(direccion);
                            txtDescripcion.setText(descripcion);



                        } catch (JSONException e) {
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
    public void eliminarEvento(){
        String URL ="https://bdconandroidstudio.000webhostapp.com/eliminarEvento.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Evento Eliminado", Toast.LENGTH_SHORT).show();
                lista();
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
                parametros.put("id",id+"");
                return parametros;
            }
        };

        requestQueue.add(stringRequest);
    }
}