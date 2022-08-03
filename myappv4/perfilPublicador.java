package com.example.rafae.myappv4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class perfilPublicador extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_publicador);


        if (!loginSession.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        Utilizador user = loginSession.getInstance(this).getUser();

        TextView username = findViewById(R.id.username);
        username.setText(user.getNomeUtilizador());

        ImageView avatar = findViewById(R.id.avatar);
        Bitmap image = BitmapFactory.decodeFile(user.getAvatar());
        avatar.setImageBitmap(image);


        findViewById(R.id.editar).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(perfilPublicador.this, editarPerfilActivity.class);

                startActivity(intent);
            }
        }));

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(getApplicationContext(), "Sessão terminada", Toast.LENGTH_SHORT).show();
                loginSession.getInstance(getApplicationContext()).logout();
            }
        });


        ImageButton home = findViewById(R.id.main);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(perfilPublicador.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /*

        ImageButton perfil = findViewById(R.id.perfil);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(perfilPublicador.this, perfilPublicador.class);
                startActivity(intent);
            }
        });

         */

        ImageButton registos = findViewById(R.id.registos);
        registos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(perfilPublicador.this, registos.class);
                startActivity(intent);
            }
        });
        ImageButton mapa = findViewById(R.id.mapa);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(perfilPublicador.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
