package com.example.myapplication;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weartest.R;

public class WearSecond extends WearableActivity {

    private TextView mTextView;

    int health=100;
    int hunger = 0;
    boolean alive = true;
    String status = "Awake";
    String pokemonname;
    TextView hungertv;
    TextView healthtv
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_second);

        mTextView = (TextView) findViewById(R.id.textView4);

        // Enables Always-on
        setAmbientEnabled();
        hungertv = findViewById(R.id.textView2);
        healthtv = findViewById(R.id.textView3);
        Bundle extras = getIntent().getExtras();

        String pokemonchoice = extras.getString("pokemonchoice");
        pokemonname = extras.getString("pokemonname");

        mTextView.setText(pokemonname);
        ImageView image = findViewById(R.id.imageView2);
        if(pokemonchoice.equals("1")) {
            image.setImageResource(R.drawable.caterpie);
        }
        else
        {
            image.setImageResource(R.drawable.pikachu);
        }

        Button b = findViewById(R.id.button6);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alive)
                {
                    hunger = hunger - 12;
                    healthtv.setText("Health : " + health);
                    hungertv.setText("Hunger : " + hunger);
                }
            }
        });
        new TimerClass().start();
    }
    class TimerClass extends Thread{
        int counter = 0;
        public void run()
        {
            while(alive)
            {
                TextView hungertv = findViewById(R.id.textView2);
                TextView healthtv = findViewById(R.id.textView3);

                hunger = hunger + 10;
                if(hunger > 80)
                {
                    health = health - 5;
                }

                if(health <= 0)
                {
                    alive = false;
                }
                healthtv.setText("Health : " + health);
                hungertv.setText("Hunger : " + hunger);
                mTextView.setText(pokemonname +" is " + counter + " seconds old");
                try
                {
                    Thread.sleep(5000);
                }
                catch(Exception e)
                {}
                counter = counter + 5;
            }
        }
    }
}