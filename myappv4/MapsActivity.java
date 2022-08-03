package com.example.rafae.myappv4;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class MapsActivity extends FragmentActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static final boolean TODO = true;
    private static final int REQUEST_CHECK_SETTINGS = 1;
//extends Activity implements ConnectionCallbacks, OnConnectionFailedListener

    private GoogleMap mMap;
    private boolean verificaAddMarker = false;
    private SQLiteDatabase bd;
    private UiSettings mUiSettings;
    private static final int REQUEST_LOCATION = 2;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    Logger log = Logger.getLogger("MapsActivity");

    HashMap<String, Marker> hashMapMarker = new HashMap<>();
    String unique = "0";

    FusedLocationProviderClient mFusedLocationClient;
    private SupportMapFragment mapFragment;
    private Marker mCurrLocationMarker;
    private LatLng latLng;
    //LocationRequest mLocationRequest;
    //Location mLastLocation;
    //_------------------------
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;


    private LocationRequest mLocationRequest;


    public static int countObs = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ImageButton home = findViewById(R.id.main);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, perfilPublicador.class);
                startActivity(intent);
            }
        });
        ImageButton registos = findViewById(R.id.registos);
        registos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, registos.class);
                startActivity(intent);
            }
        });
        ImageButton mapa = findViewById(R.id.mapa);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


        ImageButton addMarker = findViewById(R.id.addMarker);
        addMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificaAddMarker = true;
                Intent intent = new Intent(MapsActivity.this, addMarker.class);
                startActivity(intent);
            }
        });

        if (verificaAddMarker) {
            criarNovoMarcador(mapFragment, mMap);
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapsActivity.this, observation.class);
                intent.putExtra("lati", marker.getPosition().latitude);
                intent.putExtra("longi", marker.getPosition().longitude);
                startActivity(intent);

            }
        });
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);

            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nA localidade onde se encontra é: "
                    + cityName;
            Toast.makeText(MapsActivity.this, s, Toast.LENGTH_SHORT).show();

            LatLng latLng2 = new LatLng(loc.getLatitude(), loc.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng2, 14);
            mMap.animateCamera(cameraUpdate);

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(50000);
        locationRequest.setFastestInterval(50000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All good");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "not good, show dialog ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "Pending Intent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //System.out.println("Estou aqui: atiividade:" + this.getClass().getName() + "linha:" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //System.out.println("Estou aqui: atiividade:" + this.getClass().getName() + "linha:" + Thread.currentThread().getStackTrace()[2].getLineNumber());
        displayLocationSettingsRequest(this);
        //System.out.println("Estou aqui: atiividade:" + this.getClass().getName()
         //       + "linha:" + Thread.currentThread().getStackTrace()[2].getLineNumber());

        //System.out.println("Estou aqui: atiividade:" + this.getClass().getName()
        //        + "linha:" + Thread.currentThread().getStackTrace()[2].getLineNumber());

        //System.out.println("Estou aqui: atiividade:"+ this.getClass().getName()
        //        +"linha:"+Thread.currentThread().getStackTrace()[2].getLineNumber());

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(240000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //System.out.println("Estou aqui: atiividade:"+ this.getClass().getName()
        //        +"linha:"+Thread.currentThread().getStackTrace()[2].getLineNumber());
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //System.out.println("Estou aqui: atiividade:"+ this.getClass().getName()
            //        +"linha:"+Thread.currentThread().getStackTrace()[2].getLineNumber());
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //System.out.println("Estou aqui: atiividade:"+ this.getClass().getName()
                //        +"linha:"+Thread.currentThread().getStackTrace()[2].getLineNumber());
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                //System.out.println("Estou aqui: atiividade:"+ this.getClass().getName()
                //        +"linha:"+Thread.currentThread().getStackTrace()[2].getLineNumber());
                mMap.setMyLocationEnabled(true);
            } else {
                displayLocationSettingsRequest(this);
                //System.out.println("Estou aqui: atiividade:"+ this.getClass().getName()
                //        +"linha:"+Thread.currentThread().getStackTrace()[2].getLineNumber());
            }
        } else {
            //System.out.println("Estou aqui: atiividade:"+ this.getClass().getName()
            //        +"linha:"+Thread.currentThread().getStackTrace()[2].getLineNumber());
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
            //System.out.println("Estou aqui: atiividade:"+ this.getClass().getName()
            //        +"linha:"+Thread.currentThread().getStackTrace()[2].getLineNumber());
        }

        //System.out.println("Estou aqui: atiividade:"+ this.getClass().getName() +"linha:"+Thread.currentThread().getStackTrace()[2].getLineNumber());

        Double latitude = 0.0;
        Double longitude = 0.0;
        String lati = "";
        String longi = "";
        String nomeEspecie = "";
        String nomeComum = "";
        String descri = "";
        int idUtilizador = 0;
        int idObs = 0;
        String nomeObs = "";
        int idAnimal = 0;

        if(isNetworkConnected()){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_OBS, null, CODE_GET_REQUEST);
            request.execute();
            verifyBDs();
        }else{
            String getCountObs = "SELECT COUNT(id) FROM observacao";
            LigaBD liga = new LigaBD(this);
            bd = liga.getReadableDatabase();
            Cursor listaCountObs = bd.rawQuery(getCountObs, null);
            listaCountObs.moveToFirst();
            countObs = Integer.parseInt(listaCountObs.getString(0));
            System.out.println(countObs);

            String listarObs = "SELECT * FROM observacao;";
            Cursor listaObserva = bd.rawQuery(listarObs, null);
            while (listaObserva.moveToNext()) {

                idObs = Integer.parseInt(listaObserva.getString(0));
                nomeObs = listaObserva.getString(1);
                idAnimal = Integer.parseInt(listaObserva.getString(2));
                //idUtilizador = Integer.parseInt(listaObserva.getString(6));
                descri = listaObserva.getString(3);
                latitude = listaObserva.getDouble(6);
                longitude = listaObserva.getDouble(5);
                System.out.println(latitude + "---" + longitude);
                String listarNomeEspecie_comum_descri_idUtilizador_FromObs = "SELECT 'especie', 'nomeComum', 'descri' FROM 'animal' " +
                        "WHERE 'id' = " + idAnimal + ";";
                Cursor listaAnimal = bd.rawQuery(listarNomeEspecie_comum_descri_idUtilizador_FromObs, null);
                try {
                    if (listaAnimal.moveToFirst()) {
                        nomeEspecie = listaAnimal.getString(0);
                        nomeComum = listaAnimal.getString(1);
                        descri = listaAnimal.getString(2);
                    } else {
                        System.out.println("Não tem registos na base de dados!");
                    }
                } catch (SQLException e) {
                    log.warning("Erro: "+e);
                }
                //longitude = Double.parseDouble(longi);
                //latitude = Double.parseDouble(lati);
                System.out.println(longitude+"-.--.-.-.-.-."+latitude);
                String nomeUtilizador ="";
                criarMarcador(googleMap, longitude, latitude, nomeEspecie, nomeComum, descri, nomeUtilizador);
            }

        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapsActivity.this, "CLICKED", Toast.LENGTH_SHORT);
                Intent intent = new Intent(MapsActivity.this,observation.class);
                intent.putExtra("lati", marker.getPosition().latitude);
                intent.putExtra("longi", marker.getPosition().longitude);
                startActivity(intent);
            }
        });

        final SearchView searchView = findViewById(R.id.searchMap);
        searchView.setQueryHint("Search View Hint");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchValue = searchView.getQuery().toString();
                limitMap(searchValue, mMap);
                return false;
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            mMap.setMyLocationEnabled(true);

            return;
        }

        mMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);
        mMap.setOnMyLocationClickListener((GoogleMap.OnMyLocationClickListener) MapsActivity.this);
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
    }

    public void verifyBDs(){


        return;
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(View.GONE);
            System.out.println(s + "asasinhas");

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray ob = object.getJSONArray("obs");
                    for (int i = 0; i < ob.length(); i++) {
                        JSONObject obj = ob.getJSONObject(i);
                        Double longitude = obj.getDouble("longitude");
                        Double latitude = obj.getDouble("latitude");
                        int idAnimal = obj.getInt("idAnimal");
                        String descri = obj.getString("descricao");
                        String ficheiro = obj.getString("ficheiro");
                        String nomeComum = obj.getString("nomeComum");
                        String nomeEspecie = obj.getString("especie");
                        String nomeUilizador = obj.getString("nomeUtilizador");
                        System.out.println(longitude+","+latitude+","+nomeEspecie+","+nomeComum+","+descri+","+nomeUilizador);
                        criarMarcador(mMap, longitude, latitude, nomeEspecie, nomeComum, descri, nomeUilizador);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestsCon requestHandler = new RequestsCon();
            String toRetu = null;
            if (requestCode == CODE_POST_REQUEST){
                toRetu=requestHandler.sendPostRequest(url, params);
                System.out.println(this.getClass().getName() + toRetu);
            }
            if (requestCode == CODE_GET_REQUEST)
                toRetu = requestHandler.sendGetRequest(url);

            return toRetu;
        }
    }

    private void limitMap(String value, GoogleMap mMap){
        Toast.makeText(getApplicationContext(), "Funcionalidade em desenvolvimento", Toast.LENGTH_LONG).show();

        int sizeHash = hashMapMarker.size();
        Marker marker;
        boolean removedAny=false;
        while(sizeHash >= 0){
            String asas = hashMapMarker.get(sizeHash+"").getTitle();
            if(value == asas) {

            }else{
                removedAny=true;
                marker = hashMapMarker.get(sizeHash+"");
                marker.remove();
            }
            sizeHash--;
        }
        if(!removedAny){
            while(sizeHash <= hashMapMarker.size()){
                marker = hashMapMarker.get(sizeHash+"");
                //marker.setPosition(hashMapMarker.get(sizeHash+"").getPosition());
            }
        }
        return;

    }

    public void onMyLocationClick(@NonNull Location location) {
        displayLocationSettingsRequest(this);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
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
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 15000, 10, locationListener);

    }


    public boolean onMyLocationButtonClick() {
        displayLocationSettingsRequest(this);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 15000, 10, locationListener);
        return false;
    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    public void criarNovoMarcador(SupportMapFragment mapFragment, GoogleMap googleMap) {

        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        String message = intent.getStringExtra(bundle.getString("asas"));
        System.out.println(message);
        String lati = "";
        String longi = "";
        String nomeEspecie = "";
        String nomeComum = "";
        String descri = "";
        int idUtilizador = 0;

        String[] mansagem = message.split(";");
        lati = mansagem[0];
        longi = mansagem[1];
        //longi = "3.0";
        System.out.println(longi);
        nomeEspecie = mansagem[2];
        nomeComum = mansagem[3];
        descri = mansagem[4];
        idUtilizador = Integer.parseInt(mansagem[5]);
        Double latitude = Double.parseDouble(lati);
        Double longitude = Double.parseDouble(longi);

        //Double longitude = 10.0;
        System.out.println(longitude);
        String nomeUtilizador = "";
        criarMarcador(googleMap, longitude, latitude, nomeEspecie, nomeComum, descri, nomeUtilizador);
    }

    public void criarMarcador(GoogleMap googleMap, Double longitude, Double latitude, String nomeEspecie, String nomeComum, String descri, String nomeUtilizador) {

        mMap = googleMap;
        Bundle bund = getIntent().getExtras();
        LatLng novoMarker = new LatLng(latitude, longitude);
        int unique1 = Integer.parseInt(unique);
        unique1 ++;
        unique = unique1+"";
        Marker ant1 = mMap.addMarker(new MarkerOptions().position(novoMarker).title(nomeEspecie+","+nomeComum).snippet(descri));
        hashMapMarker.put(unique,ant1);
        //ant1.showInfoWindow();

        ant1.setVisible(true);
        //ant1.setDraggable(true);

        if(bund != null) {
            Double lat = bund.getDouble("latitude");
            Double lon = bund.getDouble("longitude");
            LatLng latLng = new LatLng(lat, lon);
            if(lat != 0.0 && lon != 0.0){
                ant1.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                getIntent().removeExtra("latitude");
                getIntent().removeExtra("longitude");
                bund.remove("latitude");
                bund.remove("longitude");
            }

            if (bund.getString("especie") != null || bund.getString("nomeComum") != null) {
                String especie = bund.getString("especie");
                String nomeComumS = bund.getString("nomeComum");
                if (nomeEspecie == especie || nomeComum == nomeComumS) {
                    mMap.clear();
                    LatLng pos = new LatLng(bund.getDouble("latitude"), bund.getDouble("longitude"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 20));
                    ant1 = mMap.addMarker(new MarkerOptions().position(novoMarker).title(nomeEspecie).snippet(descri));
                    ant1.showInfoWindow();
                    ant1.setVisible(true);
                    ant1.setDraggable(true);
                } else {
                    Toast.makeText(MapsActivity.this, "A procura retornou nenhum resultado", Toast.LENGTH_LONG);
                }
            }
        }


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(novoMarker));
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}