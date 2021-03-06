package com.cdp.agenda;

import android.annotation.SuppressLint;
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

    Button btnGuarda,aHora,aFecha;
    FloatingActionButton fabEditar, fabEliminar;
    boolean correcto = false;
    Contactos contacto;
    int id = 0;
    private int dia, mes, anio, hora, minutos;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//------------------------DARWYN
        aHora=(Button)findViewById(R.id.aHora);
        aFecha=(Button)findViewById(R.id.aFecha);
        eHora= findViewById(R.id.eHora);
        eFecha= findViewById(R.id.eFecha);
        aHora.setOnClickListener(this::onClick);
        aFecha.setOnClickListener(this::onClick);
//----------------------------------


        txtTitulo = findViewById(R.id.txtTitulo);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnGuarda = findViewById(R.id.btnGuarda);
        fabEditar = findViewById(R.id.fabEditar);
        fabEditar.setVisibility(View.INVISIBLE);
        fabEliminar = findViewById(R.id.fabEliminar);
        fabEliminar.setVisibility(View.INVISIBLE);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
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
            //-----------------DARWYN-----
            eHora.setInputType(InputType.TYPE_NULL);
            eFecha.setInputType(InputType.TYPE_NULL);
//---------------------------
        }

        btnGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtTitulo.getText().toString().equals("") && !eHora.getText().toString().equals("") &&
                        !eFecha.getText().toString().equals("")) {
                    correcto = dbContactos.editarContacto(id, txtTitulo.getText().toString(), eHora.getText().toString(), eFecha.getText().toString(),
                            txtDireccion.getText().toString(), txtDescripcion.getText().toString()
                            );

                    if(correcto){
                        Toast.makeText(EditarActivity.this, "REGISTRO MODIFICADO", Toast.LENGTH_LONG).show();
                        verRegistro();
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
    //----------------------------------------------------------DARWYN
    public void onClick(View v) {

        if (v == aFecha) {
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



                    eFecha.setText(year + "-" + mes + "-" + dia);

                }
            }, 2022, mes, dia);
            datePickerDialog.show();
        }
        if (v == aHora) {
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    eHora.setText(String.format("%02d:%02d", hourOfDay, minute));
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        }
    }
    //----------------------------------------------------

}