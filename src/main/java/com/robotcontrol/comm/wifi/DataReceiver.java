package com.robotcontrol.comm.wifi;

import java.io.*;

public class DataReceiver {

    private DataReceiverListener listener;
    private BufferedInputStream inputStream;
    private BufferedReader bufferedReader;
    private SocketServer server;
    private boolean listening;

    public DataReceiver(SocketServer socketServer) throws IOException {
        this.server = socketServer;
        listener = new DataReceiverResolver();
        inputStream =  new BufferedInputStream(server.getSocket().getInputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        listening = true;
    }

    public void resolveData(){
        new Thread(() -> {
            while (listening) {
                try {
                    String data = bufferedReader.readLine();
                    listener.onDataReceiveEvent(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }
}
