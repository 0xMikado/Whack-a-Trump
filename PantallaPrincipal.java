package com.example.jan.groupproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PantallaPrincipal extends AppCompatActivity {

    //:::::::::::::::: PUBLIC Pre-define Buttons ::::::::::::::::::::
    ImageButton ibExit, ibComoJugar, ibJugar, ibHighscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantallaprincipal);

        //======================= Define Buttons ==========================
        ImageButton ibExit = (ImageButton) findViewById(R.id.ibExit);
        ImageButton ibJugar = (ImageButton) findViewById(R.id.ibJugar);
        ImageButton ibComoJugar = (ImageButton) findViewById(R.id.ibComoJugar);
        ImageButton ibHighscores = (ImageButton) findViewById(R.id.ibHighscores);

        //======================= CLICK Functions =========================
        // EXIT Button
        ibExit.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // PLAY Button
        ibJugar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent mi_intent = new Intent(view.getContext(), Jugar.class);
                startActivity(mi_intent);
            }
        });

        // HOW TO PLAY Button
        ibComoJugar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent mi_intent = new Intent(view.getContext(), ComoJugar.class);

                //++++++++ TEST TO SEE FinDeJugar +++++++++++++
                //Intent mi_intent = new Intent(view.getContext(), FinDeJugar.class);
                //+++++++++++++++++++++++++++++++++++++++++++++

                startActivity(mi_intent);
            }
        });

        // SEE HIGHSCORES Button
        ibHighscores.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent mi_intent = new Intent(view.getContext(), Highscores.class);
                startActivity(mi_intent);
            }
        });

    }
}
