package com.example.rafae.myappv4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class novoRegisto extends AppCompatActivity {


    private Logger log = Logger.getLogger("novoRegisto");

    //--------------------//


    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    //para a base de dados local
    private SQLiteDatabase bd;
    private ResultSet listaNomesAnimais;
    Connection con = null;
    //base de dados local end

    //definir views
    EditText editTextAnimalId, editTextEspecie, editTextNomeComum;
    Spinner spinnerClasse, spinnerSexo;
    ProgressBar progressBar;
    ListView listViewAnimais;
    Button buttonAddUpdate;

    ImageButton novo;
    ImageButton perfil;
    ImageButton registos;
    ImageButton mapa;

    Button btpic, btnup;
    private Uri fileUri;
    String picturePath;
    Uri selectedImage;
    Bitmap photo;
    String ba1;


    //lista para apresentar os animais
    List<Animal> animalList;

    //ver se é para update ou para create
    boolean isUpdating = false;

    private String encoded_string, image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            setContentView(R.layout.activity_novo_registo);

            Context context = getApplicationContext();
            int dura = Toast.LENGTH_SHORT;
            animalList = new ArrayList<>();
            //whenuser está lginado
            try {
                if(isNetworkConnected()){
                    Toast toast = Toast.makeText(context, "Tem ligação à internet", dura);
                    toast.show();

                    editTextAnimalId = (EditText) findViewById(R.id.editTextAnimalId);
                    editTextEspecie = (EditText) findViewById(R.id.editTextEspecie);
                    editTextNomeComum = (EditText) findViewById(R.id.editTextNomeComum);
                    spinnerClasse= (Spinner) findViewById(R.id.spinnerClasses);
                    spinnerSexo = (Spinner) findViewById(R.id.spinnerSexo);

                    progressBar = (ProgressBar) findViewById(R.id.progressBar);

                    listViewAnimais = (ListView) findViewById(R.id.listViewAnimais);
                    readAnimais();
                    //----

                    btpic = (Button) findViewById(R.id.chooseImg);
                    btpic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //clickpic();

                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getFileUri();
                            i.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                            startActivityForResult(i, 10);

                            //Toast.makeText(getApplicationContext(), "Funcionalidade em desenvolvimento", Toast.LENGTH_LONG).show();
                        }
                    });

                    buttonAddUpdate = (Button) findViewById(R.id.buttonAddUpdate);
                    buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isUpdating) {
                                updateAnimal();
                            } else {
                                createAnimal();
                            }
                        }
                    });
                    ImageButton home;
                    home = findViewById(R.id.main);
                    home.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(novoRegisto.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });

                    perfil = findViewById(R.id.perfil);
                    perfil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(novoRegisto.this, perfilPublicador.class);
                            startActivity(intent);
                        }
                    });

                    registos = findViewById(R.id.registos);
                    registos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(novoRegisto.this, registos.class);
                            startActivity(intent);
                        }
                    });

                    mapa = findViewById(R.id.mapa);
                    mapa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(novoRegisto.this, MapsActivity.class);
                            startActivity(intent);
                        }
                    });
                    //connectViaExternalDB();
                    //connectViaLocalDB();

                }
                else{
                    Toast toast = Toast.makeText(context, "Não tem ligação à internet", dura);
                    toast.show();

                    novo = findViewById(R.id.novo);
                    novo.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            vibra(10);
                        }
                    });

                    perfil = findViewById(R.id.perfil);
                    perfil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(novoRegisto.this, perfilPublicador.class);
                            startActivity(intent);
                        }
                    });

                    registos = findViewById(R.id.registos);
                    registos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(novoRegisto.this, registos.class);
                            startActivity(intent);
                        }
                    });

                    mapa = findViewById(R.id.mapa);
                    mapa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(novoRegisto.this, MapsActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            }catch(Exception e){
                System.out.println("Erro: "+e);
                log.warning(e+"");
            }

    }

    private void getFileUri() {
        image_name = "testing123.jpg";
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + image_name
        );

        file_uri = Uri.fromFile(file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10 && resultCode == RESULT_OK) {
            new Encode_image().execute();
        }
    }

    private class Encode_image extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bitmap.recycle();

            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest();
        }
    }

    private void makeRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Api.URL_SAVE_IMG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("encoded_string",encoded_string);
                map.put("image_name",image_name);

                return map;
            }
        };
        requestQueue.add(request);
    }
/*
    private void clickpic() {
        // Check Camera
        PopupMenu popup = new PopupMenu(novoRegisto.this, btpic);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.choose, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.cam) {
                    if (getApplicationContext().getPackageManager().hasSystemFeature(
                            PackageManager.FEATURE_CAMERA)) {
                        // Open default camera
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the image capture Intent
                        startActivityForResult(intent, 100);

                    } else {
                        Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                        btnup.setVisibility(View.INVISIBLE);

                    }
                }else{

                }
                return true;
            }
        });
        btnup.setVisibility(View.VISIBLE);
        popup.show();//showing popup menu
    }
    */
    private void createAnimal() {
        String especie = editTextEspecie.getText().toString().trim();
        String nomeComum = editTextNomeComum.getText().toString().trim();
        String ordem = "asas";
        String familia = "asas";
        String classe = spinnerClasse.getSelectedItem().toString();
        String genero = "asas";
        String descri = "asas";
        String filo = "asas";
        String sexo = spinnerSexo.getSelectedItem().toString();

        //validações
        if (TextUtils.isEmpty(especie)) {
            editTextEspecie.setError("Insira o nome da especie");
            editTextEspecie.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(nomeComum)) {
            editTextNomeComum.setError("Insira o nome comum do maldito animal");
            editTextNomeComum.requestFocus();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("especie", especie);
        params.put("nomeComum", nomeComum);
        params.put("sexo", sexo);
        params.put("classe", classe);
        params.put("ordem", ordem);
        params.put("familia", familia);
        params.put("genero", genero);
        params.put("descri", descri);

        if (isNetworkConnected()) {
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_ANIMAL, params, CODE_POST_REQUEST);
            request.execute();
        } else {
            animalList.clear();
            /*
            private static final String criarBDAnimal = "CREATE TABLE 'animal' ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "`especie` TEXT, `nomeComum` TEXT NOT NULL, " +
            "`sexo` TEXT, `reino` TEXT NOT NULL DEFAULT 'Animalia', " +
            "`ordem` TEXT, `familia` TEXT, `classe` TEXT, `genero` TEXT, " +
            "`filo` TEXT, `descricao` TEXT);";


            String especie = editTextEspecie.getText().toString().trim();
        String nomeComum = editTextNomeComum.getText().toString().trim();
        String ordem = "asas";
        String familia = "asas";
        String classe = spinnerClasse.getSelectedItem().toString();
        String genero = "asas";
        String descri = "asas";
        String sexo = spinnerSexo.getSelectedItem().toString();
             */
            String listarAni = "INSERT INTO animal (especie, nomeComum, sexo, ordem, familia, classe, genero, filo, descricao) " +
                    "VALUES ('"+especie+"', '"+ nomeComum +"', '"+ sexo +"', '"+ ordem +"', '"+ familia +"', '"+ classe + "'" +
                    ", '"+ genero +"', '"+filo+"', '"+descri+"');";
            LigaBD liga = new LigaBD(this);
            bd = liga.getWritableDatabase();
            bd.execSQL(listarAni, null);

            //request.get();
        }
    }

    private void readAnimais() {
        if(isNetworkConnected()) {
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_ANIMAIS, null, CODE_GET_REQUEST);
            request.execute();
        }
    }


    private void updateAnimal() {
        String id = editTextAnimalId.getText().toString();
        String especie  = editTextEspecie.getText().toString().trim();
        String nomeComum = editTextNomeComum.getText().toString().trim();
        String ordem = "asas";
        String familia = "asas";
        String classe = spinnerClasse.getSelectedItem().toString();
        String genero = "asas";
        String descri = "asas";
        String sexo = spinnerSexo.getSelectedItem().toString();

        if (TextUtils.isEmpty(especie)) {
            editTextEspecie.setError("O campo não pode estar vazio");
            editTextEspecie.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nomeComum)) {
            editTextNomeComum.setError("O campo não pode estar vazio");
            editTextNomeComum.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("especie", especie);
        params.put("nomeComum", nomeComum);
        params.put("sexo", sexo);
        params.put("classe", classe);
        params.put("ordem",ordem);
        params.put("familia", familia);
        params.put("genero", genero);
        params.put("descri", descri);


        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_ANIMAL, params, CODE_POST_REQUEST);
        request.execute();

        buttonAddUpdate.setText("Add");
        editTextEspecie.setText("");
        editTextNomeComum.setText("");
        spinnerClasse.setSelection(0);
        spinnerSexo.setSelection(0);

        isUpdating = false;
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

    private void deleteAnimal(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_ANIMAL + id, null, CODE_GET_REQUEST);
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
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
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
            super(novoRegisto.this, R.layout.layout_animal_list, animalList);
            this.animalList = animalList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_animal_list, null, true);

            TextView textViewEspecie = listViewItem.findViewById(R.id.textViewEspecie);

            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final Animal animal = animalList.get(position);

            textViewEspecie.setText(animal.getEspecie());

            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isUpdating = true;
                    editTextAnimalId.setText(String.valueOf(animal.getId()));
                    editTextEspecie.setText(animal.getEspecie());
                    editTextNomeComum.setText(animal.getNomeComum());
                    spinnerClasse.setSelection(((ArrayAdapter<String>) spinnerClasse.getAdapter()).getPosition(animal.getClasse()));
                    spinnerSexo.setSelection(((ArrayAdapter<String>) spinnerSexo.getAdapter()).getPosition(animal.getSexo()));

                    buttonAddUpdate.setText("Update");
                }
            });

            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(novoRegisto.this);
                    builder.setTitle("Delete " + animal.getEspecie())
                            .setMessage("Tem a certeza?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAnimal(animal.getId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            return listViewItem;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void vibra(int tempo){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(tempo);
        return;
    }

}
