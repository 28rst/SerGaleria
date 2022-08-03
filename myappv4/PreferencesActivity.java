package com.example.rafae.myappv4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class PreferencesActivity extends AppCompatActivity {

    ListView listViewAnimais;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    List<Animal> animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        animalList = new ArrayList<>();
        listViewAnimais = findViewById(R.id.listViewAnimais);

        /*LinearLayout checkBoxesContainer = findViewById(R.id.checkBoxesContainer);
        CheckBox preferedAnimals = new CheckBox(this);

        checkBoxesContainer.addView(preferedAnimals);
        */
        readAnimais();

        Button guardar = findViewById(R.id.chooseAll);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get all

                Toast.makeText(PreferencesActivity.this, "Sucesso", Toast.LENGTH_SHORT);
                Intent intent = new Intent(PreferencesActivity.this, registos.class);
                startActivity(intent);
            }
        });
        ImageButton home = findViewById(R.id.main);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, perfilPublicador.class);
                startActivity(intent);
            }
        });
        ImageButton registos = findViewById(R.id.registos);
        registos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, registos.class);
                startActivity(intent);
            }
        });
        ImageButton mapa = findViewById(R.id.mapa);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void readAnimais() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_ANIMAIS, null, CODE_GET_REQUEST);
        request.execute();
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


    class AnimalAdapter extends ArrayAdapter<Animal> {

        List<Animal> animalList;

        public AnimalAdapter(List<Animal> animalList) {
            super(PreferencesActivity.this, R.layout.layout_animal_preferences_list, animalList);
            this.animalList = animalList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            //verificação do utilizador- se é normal é apresentado uma coisa, se é admin ou mod, é apresentado outra.
            View listViewItem = inflater.inflate(R.layout.layout_animal_preferences_list, null, true);

            final CheckBox checkBoxChoose = listViewItem.findViewById(R.id.checkBoxChoose);

            final Animal animal = animalList.get(position);
            checkBoxChoose.setText(animal.getEspecie());

            checkBoxChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    criarNovo(animal.getEspecie(),animal.getNomeComum(),animal.getGenero(), animal.getFamilia(),
                    animal.getOrdem(), animal.getClasse(), "", animal.getSexo(), animal.getDescri());
                    checkBoxChoose.setVisibility(View.GONE);

                }
            });


            return listViewItem;
        }


    }
    public void criarNovo(String nomeEspecie, String nomeComum, String genero, String familia, String ordem, String classe,
                          String filo, String sexo, String descri){


        if(nomeEspecie.isEmpty()){
            nomeEspecie = "asas";
        }if(nomeComum.isEmpty()){
            nomeComum= "asas";
        }if(genero.isEmpty()){
            genero= "asas";
        }if(familia.isEmpty()){
            familia= "asas";
        }if(ordem.isEmpty()){
            ordem = "asas";
        }if(classe.isEmpty()){
            classe= "Aves - Aves";
        }if(filo.isEmpty()){
            filo= "asas";
        }if(sexo.isEmpty()){
            sexo = "Macho";
        }if(descri.isEmpty()){
            descri= "Asinhas";
        }


        ContentValues values = new ContentValues();
        values.put("nomeComum", nomeComum);
        values.put("especie", nomeEspecie);
        values.put("sexo", sexo);
        values.put("ordem", ordem);
        values.put("familia", familia);
        values.put("classe", classe);
        values.put("genero", genero);
        values.put("filo", filo);
        values.put("descricao", descri);

        LigaBD liga = new LigaBD(PreferencesActivity.this);

        SQLiteDatabase bdWrite;
        SQLiteDatabase bdRead;

        bdRead = liga.getReadableDatabase();
        bdWrite= liga.getWritableDatabase();


        int idLast = 0;
        String [] nomeEspecieLast = new String[1000];
        String [] nomeComumLast = new String[1000];


        int dura = Toast.LENGTH_SHORT;
        boolean ask_repetido = false;
        int i = 0;
        Cursor listaLast = bdRead.rawQuery("SELECT especie, nomeComum FROM animal;", null);


        while(listaLast.moveToNext()){
            nomeEspecieLast[i] = listaLast.getString(0);
            nomeComumLast[i]= listaLast.getString(1);

            if((nomeEspecieLast[i].equals(nomeEspecie)) || (nomeComumLast[i].equals(nomeComum))){
                ask_repetido = true;
                System.out.println(nomeComumLast[i] + " = " + nomeComum + " __ " +nomeEspecieLast[i] +" = "+nomeEspecie );
            }
            i = i + 1;
        }
        listaLast.close();
        Logger log = Logger.getLogger("Loggerfromasas");
        if(ask_repetido){String msg = "Inserção sem sucesso! Animal já registado";
            log.warning(msg);
            Toast toast = Toast.makeText(getApplication(), msg, dura);
            toast.show();
        }else{
            bdWrite.insert("animal", null, values);
            String msg = "Sucesso!";
            log.warning(msg);
            Toast toast = Toast.makeText(getApplication(), msg, dura);
            toast.show();

        }
    }
}
