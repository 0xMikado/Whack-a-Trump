package com.example.jan.groupproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class FinDeJugar extends AppCompatActivity {

    //:::::::::::::::: PUBLIC Pre-define Buttons ::::::::::::::::::::
    ImageButton ibPantallaPrincipal, ibJugar, ibHighscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_de_juego);
        // :::::::::::::::::::::::: Define TextView :::::::::::::::::
        TextView tvScore = (TextView) findViewById(R.id.tvScore);

        //======================= Define Buttons ==========================
        ImageButton ibPantallaPrincipal = (ImageButton) findViewById(R.id.ibPantallaPrincipal);
        ImageButton ibJugar = (ImageButton) findViewById(R.id.ibJugar);
        ImageButton ibHighscores = (ImageButton) findViewById(R.id.ibHighscores);

        // HOME Button
        ibPantallaPrincipal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent mi_intent = new Intent(view.getContext(), PantallaPrincipal.class);
                startActivity(mi_intent);
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

        // SEE HIGHSCORES Button
        ibHighscores.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent mi_intent = new Intent(view.getContext(), Highscores.class);
                startActivity(mi_intent);
            }
        });
        // Get Bundle
        Bundle b1 = this.getIntent().getExtras();
        int score = b1.getInt("Score");

        String ScoreString = Integer.toString(score);

        tvScore.setText(ScoreString);

    }
}
