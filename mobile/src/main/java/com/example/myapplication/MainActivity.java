package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button b;
    String pokemonchoice = null;
    String pokemonname = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = findViewById(R.id.button2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessagetowearos(v, "ChoosePokemon");
            }
        });


//Register to receive local broadcasts, which we'll be creating in the next step//
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

    }
    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String m = intent.getStringExtra("message");
            Log.d("Received Message : ", m);

            String ar[] = m.split("@");
            Log.d ("length ",""+ ar.length);
            pokemonchoice = ar[0];
            pokemonname = ar[1];
            Log.d("Choice : " , pokemonchoice);
            Log.d("Name : " , pokemonname);
//            SharedPreferences sp = getPreferences(0);
//            //mode 0 means : MODE_PRIVATE: the default mode, where the created file can only be accessed by the calling application
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("pokemonchoice",pokemonchoice);
//            editor.putString("pokemonname",pokemonname);
//            editor.commit();
            Intent i = new Intent(MainActivity.this, Second.class);
            i.putExtra("pokemonchoice",pokemonchoice );
            i.putExtra("pokemonname",pokemonname );
            startActivity(i);
        }
    }

    public void sendmessagetowearos(View v, String message) {

        new NewThread("/my_path", message).start();
    }

    class NewThread extends Thread {
        String path;
        String message;

//Constructor for sending information to the Data Layer//

        NewThread(String p, String m) {
            path = p;
            message = m;
        }

        public void run() {
//Retrieve the connected devices, known as nodes//
            Task<List<Node>> wearableList =
                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                List<Node> nodes = Tasks.await(wearableList);
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =

                            Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(), path,
                                    message.getBytes());
                    Integer result = Tasks.await(sendMessageTask);
                    Log.d("message sent ", message);
                }
            } catch (Exception ex) {
                //TO DO: Handle the exception//
                Log.d("Exception : ", ex.toString());
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "This app is paused", Toast.LENGTH_LONG).show();
        SharedPreferences sp = getPreferences(0);
        //mode 0 means : MODE_PRIVATE: the default mode, where the created file can only be accessed by the calling application
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("pokemonchoice",pokemonchoice);
        editor.putString("pokemonname",pokemonname);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "This app is resumed", Toast.LENGTH_LONG).show();
        SharedPreferences sp = getPreferences(0);
        pokemonchoice= sp.getString("pokemonchoice", null);
        pokemonname = sp.getString("pokemonname", null);

        Log.d("MainActivity chioce ", ""+pokemonchoice);
        Log.d("MainAciti Name : " , ""+pokemonname);
        if(pokemonchoice != null || pokemonname != null)
        {
            Intent i = new Intent(MainActivity.this, Second.class);
            startActivity(i);
        }
    }
}
