package com.example.rafae.myappv4;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class registos extends AppCompatActivity {

    private SQLiteDatabase bd;
    Logger log = Logger.getLogger("myAppV4.registos.Logger");
    //ProgressBar progressBar;
    String id;

    EditText editTextAnimalId, editTextEspecie, editTextNomeComum;
    Spinner spinnerClasse, spinnerSexo;
    ListView listViewAnimais;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    List<Animal> animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registos);

        if(isNetworkConnected()){
            Button syncNow = (Button) findViewById(R.id.syncNow);
            syncNow.setVisibility(View.VISIBLE);
            syncNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    putLocalAnimaisOnExternal();
                }
            });
        }else{
            Button preferencias = findViewById(R.id.preferences);
            preferencias.setVisibility(View.INVISIBLE);
        }

        android.support.v7.widget.SearchView searchView = findViewById(R.id.searchRegisto);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Funcionalidade em desenvolvimento", Toast.LENGTH_LONG).show();
                //asas
            }
        });

        Button preferences = (Button) findViewById(R.id.preferences);
        preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registos.this, PreferencesActivity.class);
                startActivity(intent);
            }
        });

        ImageButton novoRegisto = (ImageButton) findViewById(R.id.novoRegisto);
        novoRegisto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(registos.this, novoRegisto.class);
                startActivity(intent);
            }
        });

        ImageButton home = (ImageButton) findViewById(R.id.main);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registos.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton perfil = (ImageButton) findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registos.this, perfilPublicador.class);
                startActivity(intent);
            }
        });

        ImageButton registos = (ImageButton) findViewById(R.id.registos);
        registos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registos.this, registos.class);
                startActivity(intent);
            }
        });

        ImageButton MapsActivity = (ImageButton) findViewById(R.id.mapa);
        MapsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registos.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        animalList = new ArrayList<>();
        listViewAnimais = findViewById(R.id.listViewAnimais);
        readAnimais();

    }

    private void deleteAnimal(String id){

        //String deleteQuery = "DELETE FROM animal WHERE id = "+id+";";
        LigaBD liga = new LigaBD(this);
        bd = liga.getWritableDatabase();
        bd.delete("animal", "id =" + id, null);
    }

    private void readAnimais() {
        if(isNetworkConnected()){
            Toast.makeText(this, "Tem acesso ao servidor!", Toast.LENGTH_SHORT);
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_ANIMAIS, null, CODE_GET_REQUEST);
            request.execute();
        }else{
            Toast.makeText(this, "Conexão ao servidor não estabelecida, verifique a a ligação à internet!", Toast.LENGTH_SHORT);
            String especie = "";
            String nomeComum = "";
            animalList.clear();
            String listarAni = "SELECT * FROM animal;";
            LigaBD liga = new LigaBD(this);
            bd = liga.getReadableDatabase();
            Cursor listaAnimais = bd.rawQuery(listarAni, null);
            while(listaAnimais.moveToNext()) {
                int ida = Integer.parseInt(listaAnimais.getString(0));
                System.out.println(id);
                especie = listaAnimais.getString(1);
                System.out.println(especie);
                nomeComum = listaAnimais.getString(2);
                String sexo = listaAnimais.getString(3);
                //reino = listaAnimais.getString(4);
                String ordem = listaAnimais.getString(5);
                String familia = listaAnimais.getString(6);
                String classe = listaAnimais.getString(7);
                String genero = listaAnimais.getString(8);
                //filo = listaAnimais.getString(9);
                String descri = listaAnimais.getString(10);
                animalList.add(new Animal(
                        ida,
                        especie,
                        nomeComum,
                        sexo,
                        ordem,
                        familia,
                        classe,
                        genero,
                        descri
                ));
            }
            try{
                File file = getTempFile(this.getApplicationContext(),"nome");
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(id+ "," + especie + "," + nomeComum);
                bw.close();
            } catch(Exception e){
                log.warning("Erro: "+e);
            }

            //creating the adapter and setting it to the listview
            AnimalAdapter adapter = new AnimalAdapter(animalList);
            listViewAnimais.setAdapter(adapter);
        }

    }

    private void putLocalAnimaisOnExternal(){

        String id = "";
        String especie = "";
        String nomeComum = "";
        String sexo = "";
        String classe = "";
        String ordem = "";
        String familia = "";
        String genero = "";
        //String filo = "";
        //String reino = "";
        String descri = "";

        /*
        private static final String criarBDAnimal = "CREATE TABLE 'animal' ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "`especie` TEXT, `nomeComum` TEXT NOT NULL, " +
            "`sexo` TEXT, `reino` TEXT NOT NULL DEFAULT 'Animalia', " +
            "`ordem` TEXT, `familia` TEXT, `classe` TEXT, `genero` TEXT, " +
            "`filo` TEXT, `descricao` TEXT);";

         */

        //$sexo, $ordem, $familia, $classe, $genero, $descri
        String listarAni = "SELECT * FROM animal;";
        LigaBD liga = new LigaBD(this);
        bd = liga.getReadableDatabase();
        Cursor listaAnimais = bd.rawQuery(listarAni, null);
        while(listaAnimais.moveToNext()){
            id = listaAnimais.getString(0);
            System.out.println(id);
            especie = listaAnimais.getString(1);
            System.out.println(especie);
            nomeComum = listaAnimais.getString(2);
            sexo = listaAnimais.getString(3);
            //reino = listaAnimais.getString(4);
            ordem = listaAnimais.getString(5);
            familia = listaAnimais.getString(6);
            classe = listaAnimais.getString(7);
            genero = listaAnimais.getString(8);
            //filo = listaAnimais.getString(9);
            descri = listaAnimais.getString(10);

            HashMap<String, String> params = new HashMap<>();
            params.put("especie", especie);
            params.put("nomeComum", nomeComum);
            params.put("sexo", sexo);
            params.put("classe", classe);
            params.put("ordem", ordem);
            params.put("familia", familia);
            params.put("genero", genero);
            params.put("descri", descri);

            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_ANIMAL, params, CODE_POST_REQUEST);
            request.execute();
        }

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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    class AnimalAdapter extends ArrayAdapter<Animal> {

        List<Animal> animalList;

        public AnimalAdapter(List<Animal> animalList) {
            super(registos.this, R.layout.layout_animal_list_normal, animalList);
            this.animalList = animalList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            //verificação do utilizador- se é normal é apresentado uma coisa, se é admin ou mod, é apresentado outra.
            View listViewItem = inflater.inflate(R.layout.layout_animal_list_normal, null, true);

            TextView textViewEspecie = listViewItem.findViewById(R.id.textViewEspecie);

            TextView textViewVer = listViewItem.findViewById(R.id.textViewVerAnimal);
            //Caso admin ou mod -- TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final Animal animal = animalList.get(position);

            textViewEspecie.setText(animal.getEspecie());

            textViewVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(registos.this, registoAnimal.class);
                    intent.putExtra("idAnimal", animal.getId());
                    intent.putExtra("especie", animal.getEspecie());
                    intent.putExtra("nomeComum", animal.getNomeComum());
                    intent.putExtra("classe", animal.getClasse());
                    intent.putExtra("descri", animal.getDescri());
                    startActivity(intent);
                    //buttonAddUpdate.setText("Update");
                }
            });

            return listViewItem;
        }
    }


}
