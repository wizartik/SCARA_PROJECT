package com.robotcontrol.comm.wifi;

import java.io.IOException;
import java.io.ObjectInputStream;

public class DataReceiver {

    private DataReceiverListener listener;
    private ObjectInputStream inputStream;
    private SocketServer server;
    private boolean listening;

    public DataReceiver(SocketServer socketServer) throws IOException {
        this.server = socketServer;
        listener = new DataReceiverResolver();
        inputStream =  new ObjectInputStream(server.getSocket().getInputStream());
        listening = true;
    }

    public void resolveData(){
        new Thread(() -> {
            while (listening) {
                try {
                    String data = (String) inputStream.readObject();
                    listener.onDataReceiveEvent(data);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }
}
