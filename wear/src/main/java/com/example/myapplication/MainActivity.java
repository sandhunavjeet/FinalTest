import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myapplication.WearSecond;
import com.example.weartest.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    int choice = 0;
    ImageButton i1, i2;
    EditText e1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();

        i1 = findViewById(R.id.imageButton);
        i2 = findViewById(R.id.imageButton2);

        e1 = findViewById(R.id.editText);
        b1 = findViewById(R.id.button);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choice = 1;
                e1.setVisibility(View.VISIBLE);
                b1.setVisibility(View.VISIBLE);

            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = 2;
                e1.setVisibility(View.VISIBLE);
                b1.setVisibility(View.VISIBLE);
            }
        });

        Button b1 = findViewById(R.id.button);
        final EditText et = findViewById(R.id.editText);
//Create an OnClickListener//

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String datapath = "/my_path";
                String data = et.getText().toString();
                String msg = "" + choice + "@" + data;
                new SendMessage(datapath, msg).start();

            }
        });
        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

//Display the following when a new message is received//
            String m = intent.getStringExtra("message");
            Log.d("Received Message : ", m);
            if (m.equals("ChoosePokemon")) {
                ScrollView sv = findViewById(R.id.s1);
                sv.setVisibility(View.VISIBLE);

            }
        }
    }

    class SendMessage extends Thread {
        String path;
        String message;
//Constructor for sending information to the Data Layer//

        SendMessage(String p, String m) {
            path = p;
            message = m;
        }

        public void run() {
//Retrieve the connected devices//

            Task<List<Node>> nodeListTask =
                    Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {

//Block on a task and get the result synchronously//

                List<Node> nodes = Tasks.await(nodeListTask);
                for (Node node : nodes) {

//Send the message///

                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(), path, message.getBytes());


                    Integer result = Tasks.await(sendMessageTask);
//Handle the errors//


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MainActivity.this, WearSecond.class);
                            String ar[] = message.split("@");
                            i.putExtra("pokemonchoice", ar[0]);
                            i.putExtra("pokemonname", ar[1]);
                            startActivity(i);

                        }
                    });


                }

            } catch (Exception ex) {

                Log.d("Exception : ", ex.toString());

            }
        }
    }
}

