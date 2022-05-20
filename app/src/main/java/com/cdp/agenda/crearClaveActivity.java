package com.cdp.agenda;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdp.agenda.adaptadores.ListaContactosAdapter;
import com.cdp.agenda.entidades.Contactos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class crearClaveActivity extends AppCompatActivity {
    private String clave;
    private String claveConfirmacion;
    private String nomDeUsuario;
    private String claveConsultada;
    private boolean teniaClave;
    private EditText txtClave,txtConfirmar;
    private Button btnGuardarpopup,btnGenerar,btnReiniciar;
    private RequestQueue requestQueue;
    private  ArrayList<String> lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_clave);
        lista = new ArrayList<>();
        requestQueue= Volley.newRequestQueue(this);
        txtClave= findViewById(R.id.txtClave);
        txtConfirmar=findViewById(R.id.txtConfirmar);
        btnGuardarpopup = findViewById(R.id.btnGuardarpopup);
        btnGenerar=findViewById(R.id.btnGenerar);
        btnReiniciar=findViewById(R.id.btnReiniciar);
        nomDeUsuario=getIntent().getStringExtra("nombreDeUsuario");
        claveConsultada=getIntent().getStringExtra("claveConexion");
        teniaClave=getIntent().getBooleanExtra("teniaClave",Boolean.FALSE);
        configurarPantalla();
        //consultarClave();
        enlistarClaves();
        txtClave.setText(claveConsultada);
        txtConfirmar.setText(claveConsultada);
        btnGuardarpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String claveGenerada = generarClave();
                txtClave.setText(claveGenerada);
                txtConfirmar.setText(claveGenerada);
            }
        });


            btnReiniciar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(teniaClave){
                        cuadroDeDialogo();

                    }
                }
            });

    }
    public void guardar(){
        clave = txtClave.getText().toString().trim();
        claveConfirmacion = txtConfirmar.getText().toString().trim();
        if((clave.length()>10)){
            Toast.makeText(getApplicationContext(),"Clave demasiada larga, máximo 10 caracteres",Toast.LENGTH_LONG).show();
        }else if(clave.length()<8){
            Toast.makeText(getApplicationContext(),"Clave demasiada corta, mínimo 8 caracteres",Toast.LENGTH_LONG).show();
        }else if(!clave.equals(claveConfirmacion)){
            Toast.makeText(getApplicationContext(),"Las claves no coinciden",Toast.LENGTH_SHORT).show();
        }else if(lista.contains(clave)){
            Toast.makeText(getApplicationContext(),"Intente con otra clave",Toast.LENGTH_SHORT).show();
        }else if(clave.length()==0 || claveConfirmacion.length()==0){
            Toast.makeText(getApplicationContext(),"Error: Debe llenar todos los campos",Toast.LENGTH_LONG).show();
        }else {
            modificarClaveUsuario(clave, nomDeUsuario);
        }
    }




    public  void configurarPantalla(){
        DisplayMetrics medidasVentana= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        int alto  = medidasVentana.widthPixels;

        getWindow().setLayout((int)(ancho*0.85),(int)(alto * 1.5));

    }

    public void modificarClaveUsuario(String c,String nomU){
        String URL="https://bdconandroidstudio.000webhostapp.com/insertarClave.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Clave registrada con éxito", Toast.LENGTH_SHORT).show();
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
                parametros.put("clave_con",c);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);

    }


    public void cuadroDeDialogo(){
        AlertDialog.Builder builder= new AlertDialog.Builder(crearClaveActivity.this);
        builder.setTitle("¿Esta Seguro?");
        builder.setMessage("Si Reinicia la clave se dejará de monitorear su dispositivo")
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reiniciar();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }


    public void reiniciar(){
        String URL ="https://bdconandroidstudio.000webhostapp.com/reiniciarClave.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Ingrese su clave nueva", Toast.LENGTH_SHORT).show();
                txtClave.setText("");
                txtConfirmar.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @androidx.annotation.Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("a_nombre",nomDeUsuario);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);

    }
    public void consultarClave(){
       // Toast.makeText(getApplicationContext(), "Buscando...", Toast.LENGTH_SHORT).show();
        String URL = "https://bdconandroidstudio.000webhostapp.com/verSoloNomAdulJson.php?nombre_a="+nomDeUsuario;
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String claveConexion="";
                        try {
                            claveConexion= response.getString("clave_con");
                            setClaveConsultada(claveConexion);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }

    public void enlistarClaves() {
        String URL = "https://bdconandroidstudio.000webhostapp.com/verificarClave.php";

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i=0;i< response.length();i++) {
                    try {
                        String claveCxn=null;
                        claveCxn = response.getString(i);
                        lista.add(claveCxn);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                // llenar();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonArrayRequest);



    }

    public void setClaveConsultada(String c){
        this.claveConsultada=c;

    }

    public  String generarClave(){
        String res="";
        for(int i=0;i<=9;i++){
            int x=(int) (Math.random()*10)+1;
            if(x>=10){
                char c = randomChar();
                res+=c;
            }else{
                if(seRepite((char)(x+'0'),res)>=2){
                    i--;
                }else{
                    res+=x;
                }
            }

        }
        return res;
    }
    public  int seRepite(char c,String cad){
        int cont=0;
        for(int i=0;i<cad.length();i++){
            if(c==cad.charAt(i)){
                cont++;
            }
        }

        return cont;
    }
    public  char randomChar(){
        Random random = new Random();
        String caracteres = "abcdefghijklmnñopqrstuvwxyz";

        int randomInt = random.nextInt(caracteres.length());
        char randomChar = caracteres.charAt(randomInt);

        System.out.println("Random character from string: " + randomChar);
        return randomChar;
    }


}
