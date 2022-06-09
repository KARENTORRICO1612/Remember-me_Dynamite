package com.cdp.agenda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager; //notificaciones
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.cdp.agenda.db.DbContactos;
import com.cdp.agenda.entidades.Contactos;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NuevoActivity extends AppCompatActivity {

    EditText txtTitulo, txtDireccion, txtDescripcion;
    TextView eHora,eFecha;
    Button btnGuarda, btnBorrar, bFecha, bHora,fijarDirec;
    Activity actividad;

    private String titulo,time,fecha,direccion,descripcion;
    private String nomAdulto;
    private ArrayList<String > misevent;
    RequestQueue requestQueue;

    private int dia, mes, anio, hora, minutos;
    private int alarmID = 1; //para notificaciones
    private SharedPreferences settings; //notificaciones

    private int HORA, MINUTO, DIA, MES, GESTION;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);
        requestQueue= Volley.newRequestQueue(this);
        nomAdulto=getIntent().getStringExtra("nombreAdulto");
        misevent=getIntent().getStringArrayListExtra("misevent");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);//notificaciones

        bFecha = (Button) findViewById(R.id.bFecha);
        bHora = (Button) findViewById(R.id.bHora);
        eFecha = (TextView) findViewById(R.id.eFecha);
        eHora = (TextView) findViewById(R.id.eHora);
        //bFecha.setOnClickListener((View.OnClickListener) this);
        //bHora.setOnClickListener((View.OnClickListener) this);

        txtTitulo = findViewById(R.id.txtTitulo);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnGuarda = findViewById(R.id.btnGuarda);
        btnBorrar = findViewById(R.id.btnBorrar);
        fijarDirec=findViewById(R.id.btndireccionfijar);
        actividad=this;
        btnGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titulo=txtTitulo.getText().toString().trim();
                fecha=eFecha.getText().toString().trim();
                time=eHora.getText().toString().trim();
                descripcion=txtDescripcion.getText().toString().trim();
                direccion=txtDireccion.getText().toString().trim();



                if (!txtTitulo.getText().toString().trim().isEmpty()
                        && !eFecha.getText().toString().equals("")&& !eHora.getText().toString().equals("")) {
                    if(existeEvento(titulo)==false){
                        registrarActivity(titulo,time,fecha,direccion,descripcion,nomAdulto);
                        String[] parts = txtTitulo.getText().toString().split("");
                        String primero  =parts[0];
                        if (primero.equals(" ")){
                            Toast.makeText(NuevoActivity.this, "Llenar campo de Tìtulo", Toast.LENGTH_LONG).show();
                            return;
                        }

                        DbContactos dbContactos = new DbContactos(NuevoActivity.this);

                        //crearAlarma

                        Calendar today = Calendar.getInstance();

                        today.set(GESTION, MES, DIA, HORA, MINUTO, 0);



                        Utils.setAlarm(alarmID, today.getTimeInMillis(), NuevoActivity.this, txtTitulo.getText().toString(), txtDescripcion.getText().toString(), eFecha.getText().toString(), eHora.getText().toString(),txtDireccion.getText().toString());



                        //Toast.makeText(NuevoActivity.this, ""+today.getTimeInMillis(), Toast.LENGTH_LONG).show();

                        //long id = dbContactos.insertarContacto(titulo,time,fecha,direccion,descripcion);

                        //if (id > 0) {
                        //Toast.makeText(NuevoActivity.this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                        //} else {
                        // Toast.makeText(NuevoActivity.this, "ERROR AL GUARDAR REGISTRO", Toast.LENGTH_LONG).show();
                        //}
                    }else{
                        Toast.makeText(NuevoActivity.this, "Nombre de evento ya existe", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    eHora.setError("");
                    eFecha.setError("");
                    txtTitulo.setError("");
                    Toast.makeText(NuevoActivity.this, "Error complete los datos obligatorios", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiar();
            }
        });
        fijarDirec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObtenerCoordendasActual();
            }
        });
    }

    private void limpiar() {
        txtTitulo.setText("");
        eHora.setText("");
        eFecha.setText("");
        txtDireccion.setText("");
        txtDescripcion.setText("");

    }

  //  @Override
    public void onClick(View v) {
        if (v == bFecha) {
            final Calendar now = Calendar.getInstance();
            int actualDay = now.get(Calendar.DAY_OF_MONTH);
            int actualMonth = now.get(Calendar.MONTH)+1;
            int actualYear = now.get(Calendar.YEAR);

            final Calendar c = Calendar.getInstance();
            int dia = c.get(Calendar.DAY_OF_MONTH);
            int mes = c.get(Calendar.MONTH);
            int anio = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String mes = (monthOfYear+1)+ "";
                    String dia= dayOfMonth+"";
                    if((monthOfYear+1)<10){
                        mes = "0"+(monthOfYear+1);
                    }
                    if(dayOfMonth<10){
                        dia = "0"+(dayOfMonth);
                    }
                    if(year > actualYear){
                        eFecha.setText(year+ "-" + mes + "-" + dia);
                    }else if(year < actualYear){
                        Toast.makeText(NuevoActivity.this, "NO SE PUEDEN REGISTRAR FECHAS ANTERIORES", Toast.LENGTH_LONG).show();
                    }else if(year == actualYear){
                        if((monthOfYear+1)>actualMonth){
                            eFecha.setText(year+ "-" + mes + "-" + dia);
                        }else if((monthOfYear+1)<actualMonth){
                            Toast.makeText(NuevoActivity.this, "NO SE PUEDEN REGISTRAR FECHAS ANTERIORES", Toast.LENGTH_LONG).show();
                        }else if((monthOfYear+1)==actualMonth){
                            if(dayOfMonth>=actualDay){
                                eFecha.setText(year+ "-" + mes + "-" + dia);
                            }else Toast.makeText(NuevoActivity.this, "NO SE PUEDEN REGISTRAR FECHAS ANTERIORES", Toast.LENGTH_LONG).show();
                        }
                    }
                    GESTION = year;
                    MES = monthOfYear;
                    DIA = dayOfMonth;
                }
            }, 2022, mes, dia);

            datePickerDialog.show();

        }
        if (v == bHora) {
            final Calendar c = Calendar.getInstance();
            int horaActual = c.get(Calendar.HOUR_OF_DAY);
            int minutosActual = c.get(Calendar.MINUTE);

            //para bug 9 dias pasados
            int diaActual = c.get(Calendar.DAY_OF_MONTH);
            int mesActual = c.get(Calendar.MONTH);
            int anioActual = c.get(Calendar.YEAR);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String finalHour, finalMinute; //notificaciones
                    if(diaActual==DIA && mesActual==MES && anioActual==GESTION ){
                        if(horaActual==hourOfDay){
                            if(minutosActual<minute){
                                eHora.setText(String.format("%02d:%02d", hourOfDay, minute));

                                finalHour = "" + hourOfDay;//notificaciones
                                finalMinute = "" + minute; //notificaciones
                                if(hourOfDay < 10) finalHour = "0" + hourOfDay; //notificaciones
                                if(minute < 10) finalMinute = "0" + minute; //notificaciones
                                HORA = hourOfDay;
                                MINUTO = minute;
                            }else{
                                Toast.makeText(NuevoActivity.this, "No se pueden registrar horas pasadas", Toast.LENGTH_SHORT).show();
                            }


                        }else if(horaActual<hourOfDay){
                            eHora.setText(String.format("%02d:%02d", hourOfDay, minute));

                            finalHour = "" + hourOfDay;//notificaciones
                            finalMinute = "" + minute; //notificaciones
                            if(hourOfDay < 10) finalHour = "0" + hourOfDay; //notificaciones
                            if(minute < 10) finalMinute = "0" + minute; //notificaciones
                            HORA = hourOfDay;
                            MINUTO = minute;
                        }else{
                            Toast.makeText(NuevoActivity.this, "No se pueden registrar horas pasadas", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        eHora.setText(String.format("%02d:%02d", hourOfDay, minute));

                        finalHour = "" + hourOfDay;//notificaciones
                        finalMinute = "" + minute; //notificaciones
                        if(hourOfDay < 10) finalHour = "0" + hourOfDay; //notificaciones
                        if(minute < 10) finalMinute = "0" + minute; //notificaciones
                        HORA = hourOfDay;
                        MINUTO = minute;
                    }
                }
            }, horaActual, minutosActual, false);
            timePickerDialog.show();
        }
    }


    public void registrarActivity(String t,String h,String f, String d, String des,String adul){
        String URL="https://bdconandroidstudio.000webhostapp.com/registrar.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
                limpiar();
                Intent intent = new Intent(actividad, mainAdulto2.class);
                intent.putExtra("usuarioLogin",nomAdulto);
                intent.putExtra("tipoDeUsuario","adulto");
                startActivity(intent);
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
                parametros.put("titulo",t);
                parametros.put("hora",h);
                parametros.put("fecha",f);
                parametros.put("descripcion",des);
                parametros.put("direccion",d);
                parametros.put("adulto_r",adul);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(actividad, mainAdulto2.class);
        intent.putExtra("usuarioLogin",nomAdulto);
        intent.putExtra("tipoDeUsuario","adulto");
        startActivity(intent);
        finish();
    }

    public void ObtenerCoordendasActual() {


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(NuevoActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
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
                    LocationServices.getFusedLocationProviderClient(NuevoActivity.this).removeLocationUpdates(this);
                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                        int latestLocationIndex = locationResult.getLocations().size() - 1;
                        double latitud = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                        double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                        Intent intent= new Intent(NuevoActivity.this,MapsActivity2.class);
                        intent.putExtra("latitud",latitud);
                        intent.putExtra("longitud",longitude);
                        startActivity(intent);

                    }

                }

            }, Looper.myLooper());

        }catch (Exception ex){
            System.out.println("Error es :" + ex);
        }
    }
    public boolean existeEvento(String titu){
        for (int i=0;i<misevent.size();i++){
            if(titu.equals(misevent.get(i))){
                return true;
            }
        }
        return false;
    }
}