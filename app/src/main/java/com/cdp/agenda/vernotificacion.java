package com.cdp.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdp.agenda.entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class vernotificacion extends AppCompatActivity {

    TextView txtTitulo, txtDireccion, txtDescripcion;
    //FloatingActionButton btn1;
    FloatingActionButton fabEliminar;
    TextView eFecha,eHora;


    FloatingActionButton idSalir;

    Contactos contacto; //incluir
    int id ; //incluir id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vernotificacion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //id = (Long.parseLong(this.getIntent().getStringExtra("id"))) ;
        String título= this.getIntent().getStringExtra("título");
        String fecha= this.getIntent().getStringExtra("fecha");
        String hora= this.getIntent().getStringExtra("hora");

        String dirección= this.getIntent().getStringExtra("dirección");
        String descripción= this.getIntent().getStringExtra("descripción");

        txtTitulo = findViewById(R.id.txtTitulo);
        eHora = findViewById(R.id.eHora);
        eFecha = findViewById(R.id.eFecha);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtDescripcion = findViewById(R.id.txtDescripcion);


        /*
        para mostrar
         */
        txtTitulo.setText(título);
        eFecha.setText(fecha);
        eHora.setText(hora);
        txtDireccion.setText(dirección);
        txtDescripcion.setText(descripción);

        fabEliminar = findViewById(R.id.fabEliminar);



        idSalir = findViewById(R.id.idSalir);
        idSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                

                Intent intent =new Intent(Intent.ACTION_MAIN);// llamado al action main
                intent.addCategory(Intent.CATEGORY_HOME );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //se borren todas las actividades
                startActivity(intent);
            }
        });
       
     /*  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtTitulo = findViewById(R.id.txtTitulo);
        eHora = findViewById(R.id.eHora);
        eFecha = findViewById(R.id.eFecha);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtDescripcion = findViewById(R.id.txtDescripcion);

        fabEliminar = findViewById(R.id.fabEliminar);
        //return;

       if(savedInstanceState == null){
           Bundle extras =getIntent().getExtras();

            if(extras == null){
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        final DbContactos dbContactos = new DbContactos(seerecordatorio.this);
        contacto = dbContactos.verContacto(id);

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

        fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(seerecordatorio.this);
                builder.setMessage("¿Desea eliminar este evento?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(dbContactos.eliminarContacto(id)){
                                    lista();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

            }
        });
*/
    }
    /*private void lista(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/


}