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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class registoAnimal extends AppCompatActivity {

    private SQLiteDatabase bd;
    Logger log = Logger.getLogger("myAppV4.registoAnimal.Logger");
    ListView listViewAnimais;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

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
        setContentView(R.layout.activity_registo_animal);

        Bundle bundle = getIntent().getExtras();
        int id_ = bundle.getInt("idAnimal");
        final String especie = bundle.getString("especie");
        final String nomeComum= bundle.getString("nomeComum");
        String classe = bundle.getString("classe");
        String descri = bundle.getString("descri");
        System.out.println(id_+"");
        LinearLayout descriAni = (LinearLayout) findViewById(R.id.descriAnimal);
        LinearLayout imgAni= (LinearLayout) findViewById(R.id.imgAnimal);
        TextView especieCampo = findViewById(R.id.especieCampo);
        TextView nomeComumCampo = findViewById(R.id.nomeComumCampo);
        TextView classeCampo = findViewById(R.id.classeCampo);
        TextView descriCampo = findViewById(R.id.descriCampo);

        ImageView animalAvatar = findViewById(R.id.imgAvatar);
        if(isNetworkConnected()){

            nomeComumCampo.setText("Nome comum: "+nomeComum);
            especieCampo.setText("Nome técnico: "+especie);
            classeCampo.setText("Classe: "+classe);
            descriCampo.setText("Descrição: "+descri);

            /*
            String url2 = "http://e0f2b94d.ngrok.io/prolserhum/img/";
            int cont1 = 1;
            ImageView imgImg = new ImageView(registoAnimal.this);
            new DownloadImageFromInternet(animalAvatar).execute(url2+"/"+especie.trim().toLowerCase()+"/"+ especie.trim().toLowerCase() +".png");
            */
        }else{
            String listarAni = "SELECT id, especie, nomeComum FROM animal WHERE ID ="+id_+";";
            LigaBD liga = new LigaBD(this);
            bd = liga.getReadableDatabase();
            Cursor listaAnimais = bd.rawQuery(listarAni, null);

            while(listaAnimais.moveToNext()){
                TextView tvEspecie = new TextView(this);
                TextView tvNomeComum= new TextView(this);
                ImageView imgImg = new ImageView(this);

                String id = listaAnimais.getString(0);
                String nomeEspecie = listaAnimais.getString(1);
                String nonomeComum = listaAnimais.getString(2);
                try{
                    File file = getTempFile(this.getApplicationContext(),"nome");
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(id+ "," + nomeEspecie + "," + nonomeComum);
                    bw.close();
                } catch(Exception e){
                    log.warning("Erro: "+e);
                }

                tvEspecie.setText("Espécie: "+nomeEspecie);
                tvNomeComum.setText("Nome Comum: "+nomeComum);
                descriAni.addView(tvEspecie);
                descriAni.addView(tvNomeComum);


                System.out.println(id+": "+nomeEspecie+" - "+nomeComum);
            }

        }

        ImageButton goToMap = findViewById(R.id.goToMap);
        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registoAnimal.this, MapsActivity.class);
                intent.putExtra("especie", especie);
                intent.putExtra("nomeComum", nomeComum);
                startActivity(intent);
            }
        });

        ImageButton home = (ImageButton) findViewById(R.id.main);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registoAnimal.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton perfil = (ImageButton) findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registoAnimal.this, perfilPublicador.class);
                startActivity(intent);
            }
        });

        ImageButton registos = (ImageButton) findViewById(R.id.registos);
        registos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registoAnimal.this, registos.class);
                startActivity(intent);
            }
        });

        ImageButton MapsActivity = (ImageButton) findViewById(R.id.mapa);
        MapsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registoAnimal.this, MapsActivity.class);
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

    private void refreshAnimalList(JSONArray animais) throws JSONException {

        animalList.clear();
        for (int i = 0; i < animais.length(); i++) {
            JSONObject obj = animais.getJSONObject(i);
            animalList.add(new Animal(
                    obj.getInt("id"),
                    obj.getString("especie"),
                    obj.getString("nomeComum"),
                    obj.getString("sexo"),
                    obj.getString("ordem"),
                    obj.getString("familia"),
                    obj.getString("classe"),
                    obj.getString("genero"),
                    obj.getString("descri")
            ));
        }

        //creating the adapter and setting it to the listview
        AnimalAdapter adapter = new AnimalAdapter(animalList);
        listViewAnimais.setAdapter(adapter);
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
                    refreshAnimalList(object.getJSONArray("animais"));
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


    class AnimalAdapter extends ArrayAdapter<Animal> {

        List<Animal> animalList;

        public AnimalAdapter(List<Animal> animalList) {
            super(registoAnimal.this, R.layout.layout_registo_animal_list, animalList);
            this.animalList = animalList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            //verificação do utilizador- se é normal é apresentado uma coisa, se é admin ou mod, é apresentado outra.
            View listViewItem = inflater.inflate(R.layout.layout_registo_animal_list, null, true);

            TextView textViewEspecie = listViewItem.findViewById(R.id.textViewEspecie);
            TextView textViewGoToMap = listViewItem.findViewById(R.id.textViewGoToMap);
            //Caso admin ou mod -- TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final Animal animal = animalList.get(position);

            textViewEspecie.setText(animal.getEspecie());
            textViewGoToMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(registoAnimal.this, MapsActivity.class);
                    startActivity(intent);
                    //buttonAddUpdate.setText("Update");
                }
            });

            return listViewItem;
        }
    }
}
