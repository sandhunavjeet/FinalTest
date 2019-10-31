package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class Second extends AppCompatActivity {

    String pokemonchoice;
    String pokemonname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        SharedPreferences sp = getPreferences(0);
        pokemonchoice= sp.getString("pokemonchoice", null);
        pokemonname = sp.getString("pokemonname", null);

        if(pokemonchoice ==null)
        {
            Bundle extras = getIntent().getExtras();

            pokemonchoice = extras.getString("pokemonchoice");
            pokemonname = extras.getString("pokemonname");

        }

        TextView tv = findViewById(R.id.textView);
        tv.setText("Name : " + pokemonname);


//
        Log.d("Second Activity Name : ", pokemonname);
        Log.d("Second Actiy Choice : ", pokemonchoice);

        ImageView iv = findViewById(R.id.imageView);
        tv.setText("Name : " + pokemonname);
        if (pokemonchoice.equals("1")) {
            iv.setImageResource(R.drawable.caterpie);
        } else {
            iv.setImageResource(R.drawable.pikachu);
        }
    }
}
