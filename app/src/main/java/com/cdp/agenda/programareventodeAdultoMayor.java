package com.cdp.agenda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdp.agenda.db.DbContactos;
import com.cdp.agenda.entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class programareventodeAdultoMayor extends AppCompatActivity {

    EditText txtTitulo, txtDireccion, txtDescripcion;
    TextView eHora,eFecha;

    Activity actividad;
    Button btnGuarda,aHora,aFecha;
    //FloatingActionButton fabEditar, fabEliminar;
    boolean correcto = false;
    Contactos contacto;
    int id = 0;
    private int dia, mes, anio, hora, minutos;
    private String titulo,time,fecha,direccion,descripcion;
    RequestQueue requestQueue;

    private int alarmID = 1; //para notificaciones

    private int HORA, MINUTO, DIA, MES, GESTION;
    @SuppressLint("RestrictedApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programareventode_adulto_mayor);
        requestQueue= Volley.newRequestQueue(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//------------------------DARWYN
        aHora=(Button)findViewById(R.id.Hora);
        aFecha=(Button)findViewById(R.id.Fecha);
        eHora= findViewById(R.id.editTextTextPersonName2);
        eFecha= findViewById(R.id.editTextTextPersonName3);
        aHora.setOnClickListener(this::onClick);
        aFecha.setOnClickListener(this::onClick);
//----------------------------------


        txtTitulo = findViewById(R.id.txtTitulo2);
        txtDireccion = findViewById(R.id.editTextTextPersonName4);
        txtDescripcion = findViewById(R.id.editTextTextPersonName5);
        btnGuarda = (Button) findViewById(R.id.Guardar);
        /*fabEditar = findViewById(R.id.fabEditar);
        fabEditar.setVisibility(View.INVISIBLE);
        fabEliminar = findViewById(R.id.fabEliminar);
        fabEliminar.setVisibility(View.INVISIBLE);*/

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt("0"); //Cambiando de null a 0
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        final DbContactos dbContactos = new DbContactos(programareventodeAdultoMayor.this);
        contacto = dbContactos.verContacto(id);

        if (contacto != null) {
            txtTitulo.setText(contacto.getTitulo());
            eHora.setText(contacto.getHora());
            eFecha.setText(contacto.getFecha());
            txtDireccion.setText(contacto.getDireccion());
            txtDescripcion.setText(contacto.getDescripcion());
            //-----------------DARWYN-----
            eHora.setInputType(InputType.TYPE_NULL);
            eFecha.setInputType(InputType.TYPE_NULL);
          //---------------------------
        }

        btnGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titulo=txtTitulo.getText().toString().trim();
                fecha=eFecha.getText().toString().trim();
                time=eHora.getText().toString().trim();
                descripcion=txtDescripcion.getText().toString().trim();
                direccion=txtDireccion.getText().toString().trim();


                if (!txtTitulo.getText().toString().equals("") && !eHora.getText().toString().equals("") &&
                        !eFecha.getText().toString().equals("")) {
                    /*correcto = dbContactos.editarContacto(id, txtTitulo.getText().toString(), eHora.getText().toString(), eFecha.getText().toString(),
                            txtDireccion.getText().toString(), txtDescripcion.getText().toString()
                    );*/
                    registrarActivity("https://bdconandroidstudio.000webhostapp.com/registrar.php");
                    String[] parts = txtTitulo.getText().toString().split("");
                    String primero  =parts[0];
                    if (primero.equals(" ")){
                        Toast.makeText(programareventodeAdultoMayor.this, "Llenar campo de Tìtulo", Toast.LENGTH_LONG).show();
                        return;
                    }

                    id = (int) dbContactos.insertarContacto(txtTitulo.getText().toString(), eHora.getText().toString(), eFecha.getText().toString(),
                            txtDireccion.getText().toString(), txtDescripcion.getText().toString()
                    );

                    Calendar today = Calendar.getInstance();

                    today.set(GESTION, MES, DIA, HORA, MINUTO, 0);

                    Utils.setAlarm(alarmID, today.getTimeInMillis(), programareventodeAdultoMayor.this, txtTitulo.getText().toString(), txtDescripcion.getText().toString(), eFecha.getText().toString(), eHora.getText().toString(),txtDireccion.getText().toString());

                    Toast.makeText(programareventodeAdultoMayor.this, ""+today.getTimeInMillis(), Toast.LENGTH_LONG).show();

                    if(id!=0){
                        Toast.makeText(programareventodeAdultoMayor.this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                        verRegistro();
                        Intent intent = new Intent(actividad, mainAdulto2.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(programareventodeAdultoMayor.this, "ERROR AL REGISTRAR ACTIVIDAD", Toast.LENGTH_LONG).show();
                    }
                } else {
                    eHora.setError("");
                    eFecha.setError("");
                    txtTitulo.setError("");
                    Toast.makeText(programareventodeAdultoMayor.this, "Error complete los datos obligatorios", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void verRegistro(){
        Intent intent = new Intent(this, VerActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }
    //----------------------------------------------------------DARWYN
    public void onClick(View v) {

        if (v == aFecha) {
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
                        Toast.makeText(programareventodeAdultoMayor.this, "NO SE PUEDEN REGISTRAR FECHAS ANTERIORES", Toast.LENGTH_LONG).show();
                    }else if(year == actualYear){
                        if((monthOfYear+1)>actualMonth){
                            eFecha.setText(year+ "-" + mes + "-" + dia);
                        }else if((monthOfYear+1)<actualMonth){
                            Toast.makeText(programareventodeAdultoMayor.this, "NO SE PUEDEN REGISTRAR FECHAS ANTERIORES", Toast.LENGTH_LONG).show();
                        }else if((monthOfYear+1)==actualMonth){
                            if(dayOfMonth>=actualDay){
                                eFecha.setText(year+ "-" + mes + "-" + dia);
                            }else Toast.makeText(programareventodeAdultoMayor.this, "NO SE PUEDEN REGISTRAR FECHAS ANTERIORES", Toast.LENGTH_LONG).show();
                        }
                    }
                    GESTION = year;
                    MES = monthOfYear;
                    DIA = dayOfMonth;
                }

            }, 2022, mes, dia);

            datePickerDialog.show();

        }
        if (v == aHora) {
            final Calendar c = Calendar.getInstance();
            int hora = c.get(Calendar.HOUR_OF_DAY);
            int minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String finalHour, finalMinute; //notificaciones

                    eHora.setText(String.format("%02d:%02d", hourOfDay, minute));

                    finalHour = "" + hourOfDay;//notificaciones
                    finalMinute = "" + minute; //notificaciones
                    if(hourOfDay < 10) finalHour = "0" + hourOfDay; //notificaciones
                    if(minute < 10) finalMinute = "0" + minute; //notificaciones

                    HORA = hourOfDay;
                    MINUTO = minute;
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        }
    }

    public void registrarActivity(String URL){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
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
                parametros.put("titulo",titulo);
                parametros.put("hora",time);
                parametros.put("fecha",fecha);
                parametros.put("descripcion",descripcion);
                parametros.put("direccion",direccion);

                return parametros;
            }
        };

        requestQueue.add(stringRequest);

    }
}