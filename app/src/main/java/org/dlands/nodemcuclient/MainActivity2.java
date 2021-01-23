package org.dlands.nodemcuclient;

import android.graphics.drawable.Drawable;
import android.icu.util.TaiwanCalendar;
import android.icu.util.TimeUnit;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.DataInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class MainActivity2 extends AppCompatActivity {
    private static boolean isTimerStarted = false;
    private static Instant startTimer;
    private static Instant stopTimer;
    Thread threadDataStream = new Thread(new DataStream());
    Thread threadRefreshData = new Thread(new RequestQuery());

    void writePing(long valuePing){
        TextView textPing = (TextView) findViewById(R.id.ping);
        if(valuePing > 3000){
            textPing.setText("Packet Lost");
        } else {
            textPing.setText("Ping : " + valuePing + " ms");
        }
    }

    void writePing(String messagePing){
        TextView textPing = (TextView) findViewById(R.id.ping);
        textPing.setText(messagePing);
    }

    void setStatusLED(int StatusInput) {
        TextView statusLED = (TextView) findViewById(R.id.StatusLED);
        RelativeLayout boxColor = (RelativeLayout) findViewById(R.id.StatusBackgroud);
        switch (StatusInput) {
            case 0:
                boxColor.setBackground(ContextCompat.getDrawable(boxColor.getContext(), R.drawable.rounded_corner_off));
                statusLED.setText("OFF");
                break;
            case 1:
                boxColor.setBackground(ContextCompat.getDrawable(boxColor.getContext(), R.drawable.rounded_corner_on));
                statusLED.setText("ON");
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Switch switchLED = (Switch) findViewById(R.id.SwitchLED);
        threadDataStream.start();
        threadRefreshData.start();
        switchLED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (switchLED.isChecked()) {
                            ConnectionTCPHandler.connection().send("1");
                        } else {
                            ConnectionTCPHandler.connection().send("0");
                        }
                        return null;
                    }
                }.execute();
            }
        });
    }

    class DataStream implements Runnable {
        String message;
        Handler handler = new Handler();

        @Override
        public void run() {
            System.out.println("Thread Started");
            try {
                while (true) {
                    //System.out.println("Collecting the message");
                    message = ConnectionTCPHandler.connection().read();
                    stopTimer = Instant.now();
                    isTimerStarted = false;
                    long ping = Duration.between(startTimer, stopTimer).toMillis();
                    //System.out.println("message recieve = " + message);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (message.equalsIgnoreCase("0")) {
                                MainActivity2.this.setStatusLED(0);
                            } else if (message.equalsIgnoreCase("1")) {
                                MainActivity2.this.setStatusLED(1);
                            }
                            MainActivity2.this.writePing(ping);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class RequestQuery implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (isTimerStarted == false){
                    startTimer = Instant.now();
                    isTimerStarted = true;
                }
                ConnectionTCPHandler.connection().send("s");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}