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

import androidx.appcompat.app.AppCompatActivity;

import com.cdp.agenda.db.DbContactos;
import com.cdp.agenda.entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class EditarActivity extends AppCompatActivity {

    EditText txtTitulo, txtDireccion, txtDescripcion;
    TextView eHora,eFecha;

    Activity actividad;


    Button btnGuarda;
    TextView aHora;
    TextView aFecha;
//>>>>>>> FinalSprint2_MerN
    FloatingActionButton fabEditar, fabEliminar;
    boolean correcto = false;
    Contactos contacto;
    int id = 0;
    private int dia, mes, anio, hora, minutos;

    private int alarmID = 2;

    private int HORA, MINUTO, DIA, MES, GESTION;

    Bundle extras;

    @SuppressLint({"RestrictedApi", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        actividad=this;


        aHora = (TextView) findViewById(R.id.aHora);
        aFecha = (TextView) findViewById(R.id.aFecha);
// >>>>>>> FinalSprint2_MerN
        eHora= findViewById(R.id.eHora);
        eFecha= findViewById(R.id.eFecha);
        bHora.setOnClickListener(this::onClick);
        bFecha.setOnClickListener(this::onClick);


        txtTitulo = findViewById(R.id.txtTitulo);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnGuarda = findViewById(R.id.btnGuarda);
        fabEditar = findViewById(R.id.fabEditar);
        fabEditar.setVisibility(View.INVISIBLE);
        fabEliminar = findViewById(R.id.fabEliminar);
        fabEliminar.setVisibility(View.INVISIBLE);

        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id =   extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        final DbContactos dbContactos = new DbContactos(EditarActivity.this);
        contacto = dbContactos.verContacto(id);

        if (contacto != null) {
            txtTitulo.setText(contacto.getTitulo());
            eHora.setText(contacto.getHora());
            eFecha.setText(contacto.getFecha());
            txtDireccion.setText(contacto.getDireccion());
            txtDescripcion.setText(contacto.getDescripcion());
            eHora.setInputType(InputType.TYPE_NULL);
            eFecha.setInputType(InputType.TYPE_NULL);
        }

        btnGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtTitulo.getText().toString().equals("") && !eHora.getText().toString().equals("") &&
                        !eFecha.getText().toString().equals("")) {

                    String[] parts = txtTitulo.getText().toString().split("");
                    String primero  =parts[0];
                    if (primero.equals(" ")){
                        Toast.makeText(EditarActivity.this, "Llenar campo de Tìtulo", Toast.LENGTH_LONG).show();
                        return;
                    }
                    correcto = dbContactos.editarContacto(id, txtTitulo.getText().toString(), eHora.getText().toString(), eFecha.getText().toString(),
                            txtDireccion.getText().toString(), txtDescripcion.getText().toString()
                            );

                    Calendar today = Calendar.getInstance();

                    today.set(GESTION, MES, DIA, HORA, MINUTO, 0);

                    Utils.setAlarm(alarmID, today.getTimeInMillis(), EditarActivity.this, txtTitulo.getText().toString(), txtDescripcion.getText().toString(), eFecha.getText().toString(), eHora.getText().toString(),txtDireccion.getText().toString());

                   // Toast.makeText(EditarActivity.this, ""+today.getTimeInMillis(), Toast.LENGTH_LONG).show();

                    if(correcto){
                        Toast.makeText(EditarActivity.this, "REGISTRO MODIFICADO", Toast.LENGTH_LONG).show();
                        verRegistro();
                        Intent intent = new Intent(actividad, mainAdulto2.class);
                        //Intent intent = new Intent(actividad, VerActivity.class);
                        startActivity(intent);
                        //finish();
                    } else {
                        Toast.makeText(EditarActivity.this, "ERROR AL MODIFICAR REGISTRO", Toast.LENGTH_LONG).show();
                    }
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
        if (v == bFecha) {
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

                }
            }, 2022, mes, dia);

            datePickerDialog.show();

        }
        if (v == bHora) {
            final Calendar c = Calendar.getInstance();
            int hora = c.get(Calendar.HOUR_OF_DAY);
            int minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    eHora.setText(String.format("%02d:%02d", hourOfDay, minute));

                    HORA = hourOfDay;
                    MINUTO = minute;
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        }
    }

}