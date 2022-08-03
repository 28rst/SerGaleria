package com.example.rafae.myappv4;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class observation extends AppCompatActivity {

    private SQLiteDatabase bd;
    Logger log = Logger.getLogger("myAppV4.registoAnimal.Logger");
    ListView listViewAnimais;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    String nomeEspecie = "";
    String nomeComum = "";
    Double latit;
    Double longit;
    Bundle bundle;

    private int idObs = 0;


    List<Animal> animalList;

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Aguarde", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        bundle = getIntent().getExtras();
        Double lati = bundle.getDouble("lati");
        Double longi = bundle.getDouble("longi");


        ImageView imgObs = findViewById(R.id.imgObs);
        if(isNetworkConnected()){
            //String url2 = "http://e0f2b94d.ngrok.io/prolserhum/img/";
            //new DownloadImageFromInternet(imgObs).execute(url2+nomeEspecie.trim().toLowerCase()+"/"+ nomeEspecie.trim().toLowerCase() +".png");
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_OBS, null, CODE_GET_REQUEST);
            request.execute();

        }else{

            String getObs = "SELECT idAnimal, nome, descricao, utilizador_id, latitude, longitude, id FROM observacao WHERE latitude='"+lati+
                    "' AND longitude = '"+longi+"';";
            LigaBD liga = new LigaBD(this);
            bd = liga.getReadableDatabase();
            final Cursor listaObserva= bd.rawQuery(getObs, null);
            int id_animal = 0;
            int id_utilizador = 0;
            String nomeObs = "";
            String descri = "";


            while (listaObserva.moveToNext()) {
                id_animal = listaObserva.getInt(0);
                nomeObs = listaObserva.getString(1);
                descri = listaObserva.getString(2);
                id_utilizador = listaObserva.getInt(3);
                latit = listaObserva.getDouble(4);
                longit = listaObserva.getDouble(5);
                idObs = listaObserva.getInt(6);
                String listarAni = "SELECT 'especie', 'nomeComum' FROM 'animal' " +
                        "WHERE 'id' = " + id_animal + ";";
                Cursor listaAnimal = bd.rawQuery(listarAni, null);
                while(listaAnimal.moveToNext()){
                    nomeEspecie = listaAnimal.getString(0);
                    nomeComum = listaAnimal.getString(1);
                    System.out.println(nomeEspecie+"-.-.!"+nomeComum);

                    try{
                        File file = getTempFile(this.getApplicationContext(),"nome");
                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(id_animal+ "," + nomeEspecie + "," + nomeComum);
                        bw.close();
                    } catch(Exception e){
                        log.warning("Erro: "+e);
                    }
                }
            }

            TextView observacaoCampo = findViewById(R.id.observacaoCampo);
            observacaoCampo.setText(nomeObs);
            TextView descriCampo = findViewById(R.id.descriCampo);
            descriCampo.setText(descri);
            TextView especieCampo = findViewById(R.id.especieCampo);
            TextView nomeComumCampo = findViewById(R.id.nomeComumCampo);
            nomeComumCampo.setText("Nome comum: "+nomeComum);
            especieCampo.setText("Nome técnico: "+nomeEspecie);
            System.out.println(nomeComum+"-.-."+nomeEspecie);
            Toast.makeText(observation.this,
                    "Não tem acesso à internet para visualizar as imagens", Toast.LENGTH_SHORT);
        }

        final ImageButton deleteObs = findViewById(R.id.deleteObs);
        deleteObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    deleteObs(idObs);
                }else{
                    String deleteObs = "DELETE from observacao WHERE id ="+idObs+";";
                    LigaBD liga = new LigaBD(getApplicationContext());
                    SQLiteDatabase bd = liga.getWritableDatabase();
                    bd.execSQL(deleteObs);
                    bd.close();

                    String verifica = "SELECT nome FROM observacao WHERE id="+idObs+";";
                    SQLiteDatabase bdq = liga.getReadableDatabase();
                    Cursor listaVerifica = bdq.rawQuery(verifica, null);
                    listaVerifica.moveToFirst();
                    try{
                        listaVerifica.getInt(0);
                    }catch (Exception e){
                        System.out.println("sucesso");
                        Toast.makeText(observation.this, "Observação apagada com sucesso", Toast.LENGTH_LONG).show();
                        Intent intent  = new Intent(observation.this, MapsActivity.class);
                    }
                }


            }
        });

        ImageButton goToMap = findViewById(R.id.goToMap);
        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(observation.this, MapsActivity.class);
                intent.putExtra("especie", nomeEspecie);
                intent.putExtra("nomeComum", nomeComum);
                intent.putExtra("latitude", latit);
                intent.putExtra("longitude", longit);

                startActivity(intent);
            }
        });

        ImageButton home = (ImageButton) findViewById(R.id.main);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(observation.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton perfil = (ImageButton) findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(observation.this, perfilPublicador.class);
                startActivity(intent);
            }
        });

        ImageButton registos = (ImageButton) findViewById(R.id.registos);
        registos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(observation.this, registos.class);
                startActivity(intent);
            }
        });

        ImageButton MapsActivity = (ImageButton) findViewById(R.id.mapa);
        MapsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(observation.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        //Button

    }


    private File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = url;
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
            log.warning("Erro no ficheiro: "+e);
        }
        return file;
    }


    private void deleteObs(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_OBS + id, null, CODE_GET_REQUEST);
        request.execute();
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
                        idObs = obj.getInt("idObs");
                        System.out.println(idObs+","+longitude+","+latitude+","+nomeEspecie+","+nomeComum+","+descri+","+nomeUilizador);

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


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
