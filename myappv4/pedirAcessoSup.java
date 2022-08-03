package com.example.rafae.myappv4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class pedirAcessoSup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_acesso_sup);

        final EditText input_razao = findViewById(R.id.input_razao);
        final Button pedir = findViewById(R.id.pedirNivelUp);
        String razao = input_razao.getText().toString();
        pedir.setEnabled(true);
        pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Funcionalidade em desenvolvimento", Toast.LENGTH_LONG).show();
            }
        });



    }


}
