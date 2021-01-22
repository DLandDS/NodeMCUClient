package org.dlands.nodemcuclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editAddress = (EditText) findViewById(R.id.inputAddress);
        Button connect = (Button) findViewById(R.id.connect_btn);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        Toast.makeText(getApplicationContext(),"Connecting", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        ConnectionTCPHandler.connection().setIp(editAddress.getText().toString());
                        ConnectionTCPHandler.connection().connect();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if(ConnectionTCPHandler.connection().isConnected()){
                            Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                            finish();
                        } else {
                            ConnectionTCPHandler.connection().reset();
                            Toast.makeText(getApplicationContext(),"Cannot be connected.\nPlease Check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        });
    }
}