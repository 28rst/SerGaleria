package com.example.rafae.myappv4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_name) EditText _nomeUtilizadorText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        /*final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext(),
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Aguarde...");
        progressDialog.show();*/

        final String nomeUtilizador = _nomeUtilizadorText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String avatar = "null";
        final String nivelAcesso = "1";
        final String nivelPremium = "0";
        final String imagemBackground = "null";

        class RegisterUser extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                RequestsCon requestHandler = new RequestsCon();
                HashMap<String, String> params = new HashMap<>();
                params.put("nomeUtilizador", nomeUtilizador);
                params.put("email", email);
                params.put("password", password);
                params.put("avatar", avatar);
                params.put("nivelAcesso", nivelAcesso);
                params.put("nivelPremium", nivelPremium);
                params.put("imagemBackground", imagemBackground);

                return requestHandler.sendPostRequest(Api.URL_REGISTER, params);
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

        RegisterUser ru = new RegisterUser();
        ru.execute();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        //progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(SignupActivity.this, perfilPublicador.class);
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Falha ao iniciar sessão!", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String nomeUtilizador = _nomeUtilizadorText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (nomeUtilizador.isEmpty() || nomeUtilizador.length() < 4 || nomeUtilizador.contains(" ")) {
            _nomeUtilizadorText.setError("Indroduza um nome com pelo menos 4 caracteres e sem espaços");
            valid = false;
        } else {
            _nomeUtilizadorText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Introduza um email válido!");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 12) {
            _passwordText.setError("Palavra passe tem de estar entre 4 a 12 caracteres");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
