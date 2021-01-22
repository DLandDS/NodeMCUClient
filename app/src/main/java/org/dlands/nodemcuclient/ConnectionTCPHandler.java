package org.dlands.nodemcuclient;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionTCPHandler {
    private static ConnectionTCPHandler Connections;
    private String ip = "localhost";
    private static Socket socket = null;
    public static final int BUFFER_SIZE = 1;
    public static ConnectionTCPHandler connection(){
        if(Connections == null){
            Connections = new ConnectionTCPHandler();
        }
        return Connections;
    }

    public void reset(){
        try {
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connections = null;
        socket = null;
    }

    public void connect(){
        try {
            socket = new Socket(ip,8888);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setIp(String inputIp){
        ip = inputIp;
    }

    public boolean isConnected(){
        return socket == null ? false : socket.isConnected();
    }

    public boolean isAvalable(){
        return socket == null ? false : true;
    }

    public void disconnect(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message){
        DataOutputStream dout = null;
        try {
            dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF(message);
            //System.out.println("message sent : " + message);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() throws IOException {
        final String message = null;
        DataInputStream din = new DataInputStream(socket.getInputStream());
        return din.readLine();
    }
}
