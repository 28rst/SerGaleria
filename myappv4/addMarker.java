package com.example.rafae.myappv4;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class addMarker extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String valores = "asas";
    String especie = "";
    String nomeComum = "";
    public String values = "";


    public static final String EXTRA_MESSAGE = "com.example.myappv4.MESSAGE";
    private SQLiteDatabase bd;
    Spinner spinner;
    Spinner spinner2;
    String message2 = "asas";
    Intent intent_ex = new Intent();
    LigaBD liga = new LigaBD(this);
    static boolean clicked = false;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);



        //System.out.println(message2);

        ImageButton perfil;
        ImageButton registos;
        ImageButton mapa;



        ImageButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addMarker.this, MapsActivity.class);
                try{
                    Bundle bundle = getIntent().getExtras();
                    String mensagem = insertObs(bundle);
                    System.out.println(mensagem);

                    intent.putExtra("asas", mensagem);
                    startActivity(intent);
                }catch(NullPointerException e){

                }
            }
        });

        ImageButton getMapa = findViewById(R.id.getMapa);
        getMapa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println(message2);
                intent_ex = new Intent(addMarker.this, addMapaMarker.class);
                startActivity(intent_ex);
                System.out.println(message2);
                clicked = true;
            }
        });

        LinearLayout linearButtonMap = findViewById(R.id.linearButtonMap);
        LinearLayout linearBombs = findViewById(R.id.linearBombs);
        LinearLayout linearSave = findViewById(R.id.linearSave);
        if(clicked){
            linearButtonMap.setVisibility(View.GONE);
            linearBombs.setVisibility(View.VISIBLE);
            linearSave.setVisibility(View.VISIBLE);
            clicked = false;
        }


        perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addMarker.this, perfilPublicador.class);
                startActivity(intent);
            }
        });

        registos = findViewById(R.id.registos);
        registos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addMarker.this, registos.class);
                startActivity(intent);
            }
        });

        mapa = findViewById(R.id.mapa);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addMarker.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        String valuesToAnim = getValoresAnimais();

        String splitVal[] = valuesToAnim.split(";");

        String nomeEspecie = splitVal[0];
        String nomeComum = splitVal[1];

        String [] nomeEspecies = nomeEspecie.split(",");
        String [] nomesComuns = nomeComum.split(",");

        //System.out.println(message2);


        spinner = (Spinner) findViewById(R.id.nomeEspecieDrop);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nomeEspecies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner2 = (Spinner) findViewById(R.id.nomeComumDrop);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nomesComuns);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);


    }
    public String simFaz(JSONArray animais) throws JSONException {
        boolean first = true;
        for (int i = 0; i < animais.length(); i++) {
            JSONObject obj = animais.getJSONObject(i);
            Animal a = new Animal(
                    obj.getInt("id"),
                    obj.getString("especie"),
                    obj.getString("nomeComum"),
                    obj.getString("sexo"),
                    obj.getString("ordem"),
                    obj.getString("familia"),
                    obj.getString("classe"),
                    obj.getString("genero"),
                    obj.getString("descri")
            );


            if(first){
                first = false;
                especie = a.getEspecie();
                nomeComum = a.getNomeComum();
                System.out.println(especie+"--"+nomeComum);
            } else {
                especie = especie + "," + a.getEspecie();
                nomeComum = nomeComum + "," + a.getNomeComum();
                System.out.println(especie+"--"+nomeComum);
            }
        }
        values = especie+";"+nomeComum;

        return values;
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
            System.out.println(s + "asas");

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    simFaz(object.getJSONArray("animais"));

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

    public String insertObs(Bundle bundle){
        String nomeEspecieFromSpinner = spinner.getSelectedItem().toString();
        String nomeComumFromSpinner = spinner2.getSelectedItem().toString();
        EditText descri = (EditText) findViewById(R.id.campoDescri);
        String descriFromEdit = descri.getText().toString();
        EditText nomeObs = (EditText) findViewById(R.id.campoNomeObs);
        String nomeObsFromEdit = nomeObs.getText().toString();
        System.out.println(message2);
        Utilizador user = loginSession.getInstance(this).getUser();
        int utilizadorId = user.getId();
        Double latitude = bundle.getDouble("latitude");
        Double longitude = bundle.getDouble("longitude");
        String mensagem = latitude +";"+ longitude + ";" + nomeEspecieFromSpinner + ";" + nomeComumFromSpinner + ";" + descriFromEdit+";"+utilizadorId;
        System.out.println("Após mensagem dos bumbles:" + mensagem);
        /*
        $nomeObs = $_POST['nome'];
				$descri = $_POST['descricao'];
				$ficheiro = $_POST['ficheiro'];
				$longitude = $_POST['longitude'];
				$latitude = $_POST['latitude'];
				$utilizador_id = $_POST['utilizadorId'];
         */
        int idAnimal;
        String ficheiro = "asas";
        HashMap<String, String> params = new HashMap<>();
        params.put("especie", nomeEspecieFromSpinner);
        params.put("nome", nomeObsFromEdit);
        params.put("descricao", descriFromEdit);
        params.put("ficheiro", ficheiro);
        params.put("longitude", longitude.toString());
        params.put("latitude", latitude.toString());
        params.put("utilizadorId", utilizadorId+"");
        //'especie', 'nome','descricao','ficheiro','longitude','latitude', 'utilizadorId
        if(isNetworkConnected()){
            System.out.println(nomeEspecieFromSpinner+"---"+nomeComumFromSpinner+".---."+descriFromEdit+"--_!"+
            ficheiro+"-.-."+longitude+"---"+latitude+"a-s-"+utilizadorId);
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_OBS, params, CODE_POST_REQUEST);
            request.execute();
        }
        else{
            String listarIdAnimal = "SELECT id FROM animal " +
                    "WHERE especie = '"+nomeEspecieFromSpinner+"';";
            bd = liga.getReadableDatabase();
            Cursor listIdAnimal = bd.rawQuery(listarIdAnimal, null);
            listIdAnimal.moveToFirst();
            idAnimal = listIdAnimal.getInt(0);
            System.out.println(idAnimal);
            String insertObs = "INSERT INTO 'observacao' ('nome', 'idAnimal', 'descricao', 'longitude', 'latitude', " +
                    "'utilizador_id') VALUES('"+ nomeObsFromEdit +"', +"+ idAnimal +", '"+
                    descriFromEdit +"', '"+ longitude +"', '"+ latitude +"', '"+ utilizadorId + "');";
            bd = liga.getWritableDatabase();
            bd.execSQL(insertObs);
        }


        return mensagem;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        parent.getItemAtPosition(pos);

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }


    public String getValoresAnimais(){

            String listarAni = "SELECT especie, nomeComum FROM animal;";

            LigaBD liga = new LigaBD(this);
            bd = liga.getReadableDatabase();
            Cursor listaAnimais = bd.rawQuery(listarAni, null);

            /**
             *
             Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
             v.vibrate(1000);
             *
             */
            Boolean first = true;
            while(listaAnimais.moveToNext()){
                if(first){
                    first = false;
                    especie = (listaAnimais.getString(0));
                    nomeComum = (listaAnimais.getString(1));
                } else {
                    especie = especie + "," + (listaAnimais.getString(0));
                    nomeComum = nomeComum + "," + (listaAnimais.getString(1));
                }

            }
            valores = especie + ";" + nomeComum;




        System.out.println("Valores: "+valores);

        return valores;
     }


}
