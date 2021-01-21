package org.dlands.nodemcuclient;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;

public class ConnectionTCPHandler {
    private static ConnectionTCPHandler Connections;
    private String ip = "localhost";
    private static Socket s = null;

    public static ConnectionTCPHandler connection(){
        if(Connections == null){
            Connections = new ConnectionTCPHandler();
        }
        return Connections;
    }

    public void connect(){
        try {
            s = new Socket(ip,8888);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setIp(String inputIp){
        ip = inputIp;
    }

    public boolean isConnected(){
        return s == null ? false : s.isConnected();
    }

    public boolean isAvalable(){
        return s == null ? false : true;
    }

    public void diconnect(){
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
