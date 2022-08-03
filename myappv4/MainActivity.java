package com.example.rafae.myappv4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private Logger log = Logger.getLogger("MainActivity");
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_nome_utilizador) EditText _nomeUtilizadorText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    ProgressDialog pd;
    TextView textoSobreRandomAnimal;
    //When user está loginado
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    ImageButton novo;
    ImageButton perfil;
    ImageButton registos;
    ImageButton mapa;

    static boolean first = true;

    final String url2 = "http://6db6f3e3.ngrok.io/prolserhum/img/";
    //ver se é para update ou para create
    boolean isUpdating = false;


    private class BackTask extends AsyncTask<String,Integer,Void> {
        String text = "";
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Atenciosamente,");
            pd.setMessage("Aguarde.");
            pd.setProgress(0);
            pd.setMax(100);
            pd.setCancelable(true);
            pd.setIndeterminate(false);
            pd.show();
        }

        protected Void doInBackground(String... params) {
            URL url;
            try {
                //create url object to point to the file location on internet
                url = new URL(params[0]);
                //make a request to server
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //get InputStream instance
                InputStream is = con.getInputStream();
                //create BufferedReader object
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

                String line;
                //read content of the file line by line
                while ((line = br.readLine()) != null) {
                    text += "\n\r"+line;
                }

                br.close();

            } catch (Exception e) {
                e.printStackTrace();
                //close dialog if error occurs
            }
            return null;
        }


        protected void onPostExecute(Void result) {
            //close dialog
            if (pd != null)
                pd.dismiss();
            textoSobreRandomAnimal = (TextView) findViewById(R.id.textoSobreRandomAnimal);
            textoSobreRandomAnimal.setText(text);
            textoSobreRandomAnimal.setMovementMethod(new ScrollingMovementMethod());

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(loginSession.getInstance(this).isLoggedIn()){
            //se estiver loginado, faz o activity_main
            setContentView(R.layout.activity_main);
            Context context = getApplicationContext();
            int dura = Toast.LENGTH_SHORT;

            //when user está lginado
            try {
                if(isNetworkConnected()){
                    if(first){
                        Toast toast = Toast.makeText(context, "Tem ligação à internet", dura);
                        toast.show();
                        first = false;
                    }
                    //new File("http://e0f2b94d.ngrok.io/prolserhum/img/TigreSabre").listFiles().length;

                    ImageView img =(ImageView) findViewById(R.id.fotograf);
                    try {
                        Picasso.with(this).load(url2+"toShow.png").into(img);
                    }catch (IllegalArgumentException we){
                        log.warning("end of files to import: "+we);
                    }
                    textoSobreRandomAnimal = findViewById(R.id.textoSobreRandomAnimal);
                    BackTask bt = new BackTask();
                    bt.execute(url2+"texto.txt");

                    /*File file=new File(url2);
                    File[] list = file.listFiles();
                    int count = 0;
                    for (File f: list){
                        String name = f.getName();
                        if (name.endsWith(".jpg") || name.endsWith(".mp3") || name.endsWith(".png"))
                            count++;
                        System.out.println("ASAS " + count);
                    }*/

                    //URL url3 = new URL("http","e0f2b94d.ngrok.io/prolserhum/img/Tigre Sabre/", 80, "1.png");

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
                            Intent intent = new Intent(MainActivity.this, perfilPublicador.class);
                            startActivity(intent);
                        }
                    });

                    registos = findViewById(R.id.registos);
                    registos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, registos.class);
                            startActivity(intent);
                        }
                    });

                    mapa = findViewById(R.id.mapa);
                    mapa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
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
                            Intent intent = new Intent(MainActivity.this, perfilPublicador.class);
                            startActivity(intent);
                        }
                    });

                    registos = findViewById(R.id.registos);
                    registos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, registos.class);
                            startActivity(intent);
                        }
                    });

                    mapa = findViewById(R.id.mapa);
                    mapa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            }catch(Exception e){
                System.out.println("Erro: "+e);
                log.warning(e+"");
            }
        }
        else{
            setContentView(R.layout.activity_main_login);
            ButterKnife.inject(this);
            _loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });

            _signupLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });
        }

    }

    private static String readText(String link){

        ArrayList<String> al=new ArrayList<>();

        try{
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;

            try {
                while ((line = br.readLine()) != null) {
                    al.add(line);
                }
            } finally {
                br.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return al.get(0);
    }

    @Override
    public void onClick(View v) {

    }

    //outside OnCreate()
    //funções e classe do NÂO LOGINADO

    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Aguarde...");
        try{
            progressDialog.show();
        }catch (Exception e){
            log.warning(e.toString());
        }


        final String nomeUtilizador = _nomeUtilizadorText.getText().toString();
        final String password = _passwordText.getText().toString();

        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /*try{
                    HttpURLConnection connection = (HttpURLConnection) new URL("http://33b429ea.ngrok.io").openConnection();
                    connection.setRequestMethod("HEAD");
                    int responseCode = connection.getResponseCode();
                    if (responseCode != 200) {
                        Toast.makeText(getApplicationContext(), "Servidor não está a responder", Toast.LENGTH_SHORT).show();
                    }

                } catch (MalformedURLException e){
                    log.warning(e+"");
                } catch (ProtocolException ex){
                    log.warning(ex+"");
                } catch (IOException exa){
                    log.warning(exa+"");
                } catch (Exception exat){
                    log.warning(exat+"");
                }
                */
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    System.out.println("DAMN IT!!!!"+s);
                    JSONObject obj = new JSONObject(s);
                    System.out.println("DAMN IT!! WORK IT FOR ONCE!!!!"+s);
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        System.out.println(s);
                        JSONObject userJson = obj.getJSONObject("user");
                        System.out.println(userJson);
                        Utilizador user = new Utilizador(
                                userJson.getInt("id"),
                                userJson.getString("nomeUtilizador"),
                                userJson.getString("avatar"),
                                userJson.getInt("nivelAcesso"),
                                userJson.getInt("nivelPremium"),
                                userJson.getString("email"),
                                userJson.getString("imagemBackground"),
                                userJson.getString("password")
                        );

                        loginSession.getInstance(getApplicationContext()).userLogin(user);
                        finish();
                        startActivity(new Intent(getApplicationContext(), perfilPublicador.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Nome de utilizador ou password incorretos", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e+"-- Erro");
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestsCon requestHandler = new RequestsCon();

                HashMap<String, String> params = new HashMap<>();

                params.put("nomeUtilizador", nomeUtilizador);
                params.put("password", password);

                System.out.println(params+nomeUtilizador+"----"+password+"---asas, estou aqui");
                return requestHandler.sendPostRequest(Api.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess();
                        // onLoginFailed();
                        //progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(MainActivity.this, perfilPublicador.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Falha ao iniciar sessão", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String nomeUs = _nomeUtilizadorText.getText().toString();
        String password = _passwordText.getText().toString();

        if (nomeUs.isEmpty()) {
            _nomeUtilizadorText.setError("Onde está o nome de utilizador?");
            valid = false;
        } else {
            _nomeUtilizadorText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Onde está a palavra passe?");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
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



    //outside OnCreate()
    //funções e classe do loginado

    /*
    private void createAnimal() {
        String especie  = editTextEspecie.getText().toString().trim();
        String nomeComum = editTextNomeComum.getText().toString().trim();
        String ordem = "asas";
        String familia = "asas";
        String classe = spinnerClasse.getSelectedItem().toString();
        String genero = "asas";
        String descri = "asas";
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

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_ANIMAL, params, CODE_POST_REQUEST);
        request.execute();
        //request.get();
    }

    private void readAnimais() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_ANIMAIS, null, CODE_GET_REQUEST);
        request.execute();
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
            super(MainActivity.this, R.layout.layout_animal_list, animalList);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

*/

}
