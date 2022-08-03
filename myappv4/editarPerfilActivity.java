package com.example.rafae.myappv4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class editarPerfilActivity extends AppCompatActivity {

    /**
     * @InjectView(R.id.username) EditText _nomeUtilizadorText;
     * @InjectView(R.id.input_email_old) EditText _emailTextAntigo;
     * @InjectView(R.id.input_email_novo) EditText _emailTextNovo;
     * @InjectView(R.id.input_password_antiga) EditText _passwordTextAntiga;
     * @InjectView(R.id.input_password_nova) EditText _passwordTextNova;
     * @InjectView(R.id.input_password_verifica) EditText _passwordTextVerifica;
     * @InjectView(R.id.guardarAlt) Button _guardarButton;
     * @InjectView(R.id.mudarAvatar) ImageButton _mudarAvatarButton;
     * @InjectView(R.id.background) LinearLayout _backgroundLayoutChange;
    */

    EditText _nomeUtilizadorText;
    TextView _emailTextAntigo;
    EditText _emailTextNovo;
    EditText _passwordTextAntiga;
    EditText _passwordTextNova;
    EditText _passwordTextVerifica;
    Button _guardarButton;

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1234:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap image = BitmapFactory.decodeFile(filePath);
                    Utilizador user2 = new Utilizador(
                            user.getId(),
                            user.getNomeUtilizador(),
                            filePath,
                            user.getNivelAcesso(),
                            user.getNivelPremium(),
                            user.getEmail(),
                            user.getImagemBackground(),
                            user.getPassword()
                    );

                    loginSession.getInstance(getApplicationContext()).userLogin(user2);

                }
        }

    };

    Utilizador user = loginSession.getInstance(this).getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        //final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext(),
                //R.style.Theme_AppCompat_DayNight_Dialog);
        _nomeUtilizadorText = findViewById(R.id.username) ;
        _emailTextAntigo = findViewById(R.id.input_email_old);
        _emailTextNovo = findViewById(R.id.input_email_novo) ;
        _passwordTextAntiga = findViewById(R.id.input_password_antiga) ;
        _passwordTextNova = findViewById(R.id.input_password_nova) ;
        _passwordTextVerifica = findViewById(R.id.input_password_verifica) ;
        _guardarButton = findViewById(R.id.guardarAlt);

        _nomeUtilizadorText.setText(user.getNomeUtilizador());
        _emailTextAntigo.setText(user.getEmail());

        Button pedirAcesso = (Button) findViewById(R.id.pedirNivelUp);
        pedirAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editarPerfilActivity.this, pedirAcessoSup.class);
                startActivity(intent);
            }
        });

        final ImageButton _mudarAvatarButton = (ImageButton) findViewById(R.id.mudarAvatar);
        _mudarAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "ImageButtonAvatar Clicado!",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });

        LinearLayout _backgroundLayoutChange = (LinearLayout) findViewById(R.id.background);
        _backgroundLayoutChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Linear Layout Background Clicado!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(getBaseContext(), "Deixe os campos em branco caso não queira que sofram alterações!", Toast.LENGTH_LONG+1).show();
        _guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(validate()){

                    /*progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Aguarde...");
                    progressDialog.show();*/
                    final String id = user.getId()+"";
                    final String nomeUtilizador = _nomeUtilizadorText.getText().toString();
                    final String email = _emailTextNovo.getText().toString();
                    final String password = _passwordTextNova.getText().toString();
                    //passar imagens da app para o servidor e vice-versa
                    final String avatar = "null";
                    final String imagemBackground = "null";
                    //delinear os niveis de acesso, até agora ficam assim
                    final String nivelAcesso = "1";
                    final String nivelPremium = "0";

                    //---------------------------------------------------------------------//

                    class UpdateUser extends AsyncTask<Void, Void, String> {

                        @Override
                        protected String doInBackground(Void... voids) {
                            RequestsCon requestHandler = new RequestsCon();
                            HashMap<String, String> params = new HashMap<>();
                            params.put("id", id);
                            params.put("nomeUtilizador", nomeUtilizador);
                            params.put("email", email);
                            params.put("password", password);
                            params.put("avatar", avatar);
                            params.put("nivelAcesso", nivelAcesso);
                            params.put("nivelPremium", nivelPremium);
                            params.put("imagemBackground", imagemBackground);

                            return requestHandler.sendPostRequest(Api.URL_UPDATE_USER, params);
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);

                            try {
                                JSONObject obj = new JSONObject(s);

                                if (!obj.getBoolean("error")) {
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                    JSONObject userJson = obj.getJSONObject("user");

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
                                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    UpdateUser uu = new UpdateUser();
                    uu.execute();


                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onSignupSuccess or onSignupFailed
                                    // depending on success
                                    //onSignupSuccess();
                                    // onSignupFailed();
                                    //progressDialog.dismiss();
                                }
                            }, 3000);

                    //---------------------------------------------------------------------//
           //     }else{
           //         Toast.makeText(getBaseContext(), "Alterações guardadas sem sucesso!", Toast.LENGTH_LONG).show();
           //         return;
           //     }
            }
        });




    }

    public boolean validate() {
        boolean valid = true;
        /*
        EditText _nomeUtilizadorText = findViewById(R.id.username) ;
        EditText _emailTextAntigo = findViewById(R.id.input_email_old);
        EditText _emailTextNovo = findViewById(R.id.input_email_novo) ;
        EditText _passwordTextAntiga = findViewById(R.id.input_password_antiga) ;
        EditText _passwordTextNova = findViewById(R.id.input_password_nova) ;
        EditText _passwordTextVerifica = findViewById(R.id.input_password_verifica) ;
        Button _guardarButton = findViewById(R.id.guardarAlt);
         */
        String nomeUtilizador   = _nomeUtilizadorText.getText().toString();
        //String emailOld         = _emailTextAntigo.getText().toString();
        String emailNew         = _emailTextNovo.getText().toString();
        String passwordOld      = _passwordTextAntiga.getText().toString();
        String passwordNew      = _passwordTextNova.getText().toString();
        String passwordCheck    = _passwordTextVerifica.getText().toString();

        //----------------------------NOME USER VERIFICA---------------------------------------------------//

        if(nomeUtilizador.isEmpty()){
            _nomeUtilizadorText.setText(user.getNomeUtilizador());
            _nomeUtilizadorText.setError(null);
        }else{
            if ( nomeUtilizador.length() < 4 || nomeUtilizador.length() > 12 || nomeUtilizador.contains(" ")) {
                _nomeUtilizadorText.setError("Indroduza um nome entre 4 a 12 caracteres e sem espaços");
                valid = false;
            } else {
                _nomeUtilizadorText.setError(null);
            }
        }
        //-------------------------------------------------------------------------------------------------//

        //--------------------------------EMAIL VERIFICA---------------------------------------------------//
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailNew).matches()){
            _emailTextAntigo.setError("Email inválido!");
            valid = false;
        } else {
            _emailTextAntigo.setError(null);
        }
        if(emailNew==user.getEmail()){
            _emailTextAntigo.setError("Email igual ao antigo!");
            valid = false;
        } else {
            _emailTextAntigo.setError(null);
        }
        if(emailNew.isEmpty()){
            _emailTextNovo.setText(user.getEmail());
            _emailTextNovo.setError(null);
        }
        //--------------------------------EMAIL END-----------------------------------------------------------//

        //----------------------PWD VERIFICA-----------------------------------------------------------------//
        if(passwordOld.isEmpty() && passwordNew.isEmpty() && passwordCheck.isEmpty()){
            _passwordTextAntiga.setText(user.getPassword());
            _passwordTextNova.setText(user.getPassword());
            _passwordTextVerifica.setText(user.getPassword());
            _passwordTextAntiga.setError(null);
        }else{
            if(passwordOld.isEmpty()){
                _passwordTextAntiga.setError("Introduza a sua antiga palavra passe");
                valid = false;
            }else{
                _passwordTextAntiga.setError(null);
            }
            if(passwordOld != user.getPassword()){
                //System.out.println(user.getPassword());
                _passwordTextAntiga.setError("Password errada");
                valid = false;
            }else{
                _passwordTextAntiga.setError(null);
            }
            if(passwordNew == user.getPassword()){
                _passwordTextNova.setError("Introduza uma Palavra passe diferente da antiga");
                valid = false;
            }else{
                _passwordTextNova.setError(null);
            }
            if(passwordNew.isEmpty()){
                _passwordTextNova.setError("Introduza a sua nova palavra passe");
                valid = false;
            }else{
                _passwordTextNova.setError(null);
            }
            if(passwordNew.length() < 4  || passwordNew.length() > 12){
                _passwordTextNova.setError("Entre 4 a 12 caracteres");
                valid = false;
            }else{
                _passwordTextNova.setError(null);
            }
            if(passwordCheck.isEmpty()){
                _passwordTextVerifica.setError("Verifique a sua palavra passe nova");
                valid = false;
            } else {
                _passwordTextVerifica.setError(null);
            }
            if(passwordCheck != passwordNew){
                _passwordTextVerifica.setError("Palavras passes não correspondem");
                valid = false;
            }else{
                _passwordTextVerifica.setError(null);
            }
        }
        //-------------------PWD END-------------------------------------------------------------------------//

        return valid;
    }
}
