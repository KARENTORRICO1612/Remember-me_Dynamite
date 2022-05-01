package com.cdp.agenda;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cdp.agenda.adaptadores.ListaContactosAdapter;
import com.cdp.agenda.db.DbContactos;
import com.cdp.agenda.entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity  {


    private Spinner spinner;
    private Button boton;
    private Button button;
    private Button idverLista;
    ImageView idSalir;

    SearchView txtBuscar;
    //RecyclerView listaContactos;
    ArrayList<Contactos> listaArrayContactos;
    FloatingActionButton fabNuevo;
    //ListaContactosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.programar);

        idSalir = (ImageView) findViewById(R.id.idSalir);

        //inicializar componentes
        spinner = findViewById(R.id.spinner);
        boton = findViewById(R.id.button2);
        idverLista= findViewById(R.id.idverLista);

        //Acciones del boton

        idSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        idverLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this,mainAdulto2.class);
                startActivity(intent);

            }
        });

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llenarSpinner();
            }
        });


            button = (Button)findViewById(R.id.button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent( MainActivity.this,programareventodeAdultoMayor.class);
                    startActivity(i);
                }
            });
    }

    private void llenarSpinner() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario(1,"jose","Garcia","lopez"));
        usuarios.add(new Usuario(2,"luis","Pacheco","Hernandez"));
        usuarios.add(new Usuario(3,"macario","Choque","Mento"));
        usuarios.add(new Usuario(4,"maria","Alvarado","Veliz"));

        ArrayAdapter<Usuario> adapter1 = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,usuarios);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,"Realice una seleccion", Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }


    /*public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuNuevo:
                nuevoRegistro();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    /*private void nuevoRegistro(){
        Intent intent = new Intent(this, NuevoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //adapter.filtrado(s);
        return false;
    }
*/

}