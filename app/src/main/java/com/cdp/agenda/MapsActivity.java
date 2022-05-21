package com.cdp.agenda;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
   // private ActivityMapsBinding binding;

    private double latitud, longitud;
    private String getlat, getlong, name;
    private RequestQueue requestQueue;
    private boolean salio;
    private float zoom;
    //private ArrayList<Marker> miubic= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // binding = ActivityMapsBinding.inflate(getLayoutInflater());
       // setContentView(binding.getRoot());

        requestQueue = Volley.newRequestQueue(this);
        getlat = getIntent().getExtras().getString("latitud");
        getlong = getIntent().getExtras().getString("longitud");
        name = getIntent().getExtras().getString("nameA");
        latitud = Double.parseDouble(getlat);
        longitud = Double.parseDouble(getlong);
        salio = false;
        zoom = 18;
        //getUbicacion(name);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getUbicacion(String nameA) {
        // Toast.makeText(getApplicationContext(), "se hizo la consulta", Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://bdconandroidstudio.000webhostapp.com/verSoloNomAdulJson.php" + "?nombre_a=" + nameA,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getApplicationContext(), "se hizo la consulta", Toast.LENGTH_SHORT).show();
                        String latitudS;
                        String longitudS;
                        try {

                            latitudS = response.getString("latitud").toString();
                            longitudS = response.getString("longitud").toString();
                            latitud = Double.parseDouble(latitudS);
                            longitud = Double.parseDouble(longitudS);
                            if (!salio) {

                                countDownTimer2();
                            }


                        } catch (JSONException e) {
                            Toast.makeText(MapsActivity.this, "Error al acceder a la Base de datos", Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //cambio para bug 16 y 17
                        Toast.makeText(getApplicationContext(), "Error al acceder a la Base de datos", Toast.LENGTH_SHORT).show();

                    }
                }
        );


        requestQueue.add(jsonObjectRequest);

    }

    private void countDownTimer() {
        new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {
                // Toast.makeText(MapsActivity.this, "Faltan "+millisUntilFinished/1000+" segundos", Toast.LENGTH_SHORT).show();

            }

            public void onFinish() {
                getUbicacion(name);
                //Toast.makeText(MapsActivity.this, "Ubicación actualizada", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void countDownTimer2() {
        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
                //Toast.makeText(MapsActivity.this, "Faltan "+millisUntilFinished/1000+" segundos", Toast.LENGTH_SHORT).show();

            }

            public void onFinish() {
                onMapReady(mMap);
                Toast.makeText(MapsActivity.this, "Ubicación actualizada", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(sydney).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
        mMap.getUiSettings().setZoomControlsEnabled(true);//para los botones de zoom
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        countDownTimer();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        salio=true;
    }
}