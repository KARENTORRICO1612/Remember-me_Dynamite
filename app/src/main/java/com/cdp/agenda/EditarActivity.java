package com.cdp.agenda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
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

public class EditarActivity extends AppCompatActivity {

    EditText txtTitulo, txtDireccion, txtDescripcion;
    TextView eHora,eFecha;
    Activity actividad;
    Button btnGuarda;
    TextView aHora;
    TextView aFecha;
    RequestQueue requestQueue;
    FloatingActionButton fabEditar, fabEliminar;
    boolean correcto = false;
    String titulo,hora,fecha,direccion,descripcion;
    String nomAdulto;
    String ID;
    Contactos contacto;
    int id = 0;
    private int dia, mes, anio, minutos;

    private int alarmID = 2;

    private int HORA, MINUTO, DIA, MES, GESTION;

    Bundle extras;

    @SuppressLint({"RestrictedApi", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ID=getIntent().getStringExtra("ID");
        titulo= getIntent().getStringExtra("titulo");
        hora=   getIntent().getStringExtra("hora");
        fecha= getIntent().getStringExtra("fecha");
        direccion=getIntent().getStringExtra("direccion");
        descripcion=getIntent().getStringExtra("descripcion");
        nomAdulto=getIntent().getStringExtra("usuarioLogin");
        requestQueue = Volley.newRequestQueue(this);
        actividad=this;


        aHora = (TextView) findViewById(R.id.aHora);
        aFecha = (TextView) findViewById(R.id.aFecha);
        eHora= findViewById(R.id.eHora);
        eFecha= findViewById(R.id.eFecha);
        //para bug 11 y 12
        aHora.setBackgroundColor(Color.parseColor("#005F73"));
        aHora.setTextColor(Color.parseColor("#ffffff"));
        aFecha.setBackgroundColor(Color.parseColor("#005F73"));
        aFecha.setTextColor(Color.parseColor("#ffffff"));
        aHora.setGravity(Gravity.CENTER_HORIZONTAL);
        aFecha.setGravity(Gravity.CENTER_HORIZONTAL);
        //fin bug fix
        aHora.setOnClickListener(this::onClick);
        aFecha.setOnClickListener(this::onClick);


        txtTitulo = findViewById(R.id.txtTitulo);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnGuarda = findViewById(R.id.btnGuarda);
        fabEditar = findViewById(R.id.fabEditar);
        fabEditar.setVisibility(View.INVISIBLE);
        fabEliminar = findViewById(R.id.fabEliminar);
        fabEliminar.setVisibility(View.INVISIBLE);

       /* if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id =   extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        */

      //  final DbContactos dbContactos = new DbContactos(EditarActivity.this);
      //  contacto = dbContactos.verContacto(id);

        //if (contacto != null) {
            txtTitulo.setText(titulo);
            eHora.setText(hora);
            eFecha.setText(fecha);
            txtDireccion.setText(direccion);
            txtDescripcion.setText(descripcion);
            eHora.setInputType(InputType.TYPE_NULL);
            eFecha.setInputType(InputType.TYPE_NULL);
        //}

        btnGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtTitulo.getText().toString().trim().isEmpty()  && !eHora.getText().toString().equals("") &&
                        !eFecha.getText().toString().equals("")) {

                    titulo=txtTitulo.getText().toString().trim();
                    fecha=eFecha.getText().toString().trim();
                    hora=eHora.getText().toString().trim();
                    descripcion=txtDescripcion.getText().toString().trim();
                    direccion=txtDireccion.getText().toString().trim();
                    modificarActivity();

                    String[] parts = txtTitulo.getText().toString().split("");
                    String primero  =parts[0];
                    if (primero.equals(" ")){
                        Toast.makeText(EditarActivity.this, "Llenar campo de Tìtulo", Toast.LENGTH_LONG).show();
                        return;
                    }
                   // correcto = dbContactos.editarContacto(id, txtTitulo.getText().toString(), eHora.getText().toString(), eFecha.getText().toString(),
                     //       txtDireccion.getText().toString(), txtDescripcion.getText().toString()
                       //     );

                    /*Calendar today = Calendar.getInstance();

                    today.set(GESTION, MES, DIA, HORA, MINUTO, 0);

                    Utils.setAlarm(alarmID, today.getTimeInMillis(), EditarActivity.this, txtTitulo.getText().toString(), txtDescripcion.getText().toString(), eFecha.getText().toString(), eHora.getText().toString(),txtDireccion.getText().toString());*/

                   // Toast.makeText(EditarActivity.this, ""+today.getTimeInMillis(), Toast.LENGTH_LONG).show();

                    //if(correcto){
                        Toast.makeText(EditarActivity.this, "REGISTRO MODIFICADO", Toast.LENGTH_LONG).show();
                        verRegistro();
                        Intent intent = new Intent(actividad, mainAdulto2.class);
                        //Intent intent = new Intent(actividad, VerActivity.class);
                        startActivity(intent);
                        //finish();
                    //} else {
                        //Toast.makeText(EditarActivity.this, "ERROR AL MODIFICAR REGISTRO", Toast.LENGTH_LONG).show();
                    //}
                } else {
                    eHora.setError("");
                    eFecha.setError("");
                    txtTitulo.setError("");
                    Toast.makeText(EditarActivity.this, "Error complete los datos obligatorios", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void verRegistro(){
        Intent intent = new Intent(this, VerActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }
    public void onClick(View v) {
        if (v == aFecha) {
            final Calendar now = Calendar.getInstance();
            int actualDay = now.get(Calendar.DAY_OF_MONTH);
            int actualMonth = now.get(Calendar.MONTH)+1;
            int actualYear = now.get(Calendar.YEAR);

            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            anio = c.get(Calendar.YEAR);

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
                        Toast.makeText(EditarActivity.this, "NO SE PUEDEN REGISTRAR FECHAS ANTERIORES", Toast.LENGTH_LONG).show();
                    }else if(year == actualYear){
                        if((monthOfYear+1)>actualMonth){
                            eFecha.setText(year+ "-" + mes + "-" + dia);
                        }else if((monthOfYear+1)<actualMonth){
                            Toast.makeText(EditarActivity.this, "NO SE PUEDEN REGISTRAR FECHAS ANTERIORES", Toast.LENGTH_LONG).show();
                        }else if((monthOfYear+1)==actualMonth){
                            if(dayOfMonth>=actualDay){
                                eFecha.setText(year+ "-" + mes + "-" + dia);
                            }else Toast.makeText(EditarActivity.this, "NO SE PUEDEN REGISTRAR FECHAS ANTERIORES", Toast.LENGTH_LONG).show();
                        }
                    }
                    //para bug 9 horas pasadas
                    GESTION = year;
                    MES = monthOfYear;
                    DIA = dayOfMonth;
                }
            }, 2022, mes, dia);

            datePickerDialog.show();

        }
        if (v == aHora) {
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
                            }else{
                                Toast.makeText(EditarActivity.this, "NO SE PUEDEN REGISTRAR HORAS PASADAS", Toast.LENGTH_SHORT).show();
                            }


                        }else if(horaActual<hourOfDay){
                            eHora.setText(String.format("%02d:%02d", hourOfDay, minute));

                            finalHour = "" + hourOfDay;//notificaciones
                            finalMinute = "" + minute; //notificaciones
                            if(hourOfDay < 10) finalHour = "0" + hourOfDay; //notificaciones
                            if(minute < 10) finalMinute = "0" + minute; //notificaciones
                        }else{
                            Toast.makeText(EditarActivity.this, "NO SE PUEDEN REGISTRAR HORAS PASADAS", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        eHora.setText(String.format("%02d:%02d", hourOfDay, minute));

                        finalHour = "" + hourOfDay;//notificaciones
                        finalMinute = "" + minute; //notificaciones
                        if(hourOfDay < 10) finalHour = "0" + hourOfDay; //notificaciones
                        if(minute < 10) finalMinute = "0" + minute; //notificaciones
                    }
                }
            }, horaActual, minutosActual, false);
            timePickerDialog.show();
        }
    }


    public void modificarActivity(){
        String URL="https://bdconandroidstudio.000webhostapp.com/modificarEvento.php";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "MODIFICACION EXITOSA", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(actividad, mainAdulto2.class);
                intent.putExtra("usuarioLogin",nomAdulto);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //finish();
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
                parametros.put("id",ID);
                parametros.put("titulo",titulo);
                parametros.put("hora",hora);
                parametros.put("fecha",fecha);
                parametros.put("descripcion",descripcion);
                parametros.put("direccion",direccion);
                return parametros;
            }
        };

        requestQueue.add(stringRequest);

    }

}

