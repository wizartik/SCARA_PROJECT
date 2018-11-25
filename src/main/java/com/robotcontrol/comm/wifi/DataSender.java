package com.robotcontrol.comm.wifi;

import java.io.*;

public class DataSender {

    private SocketServer server;

    private BufferedOutputStream outputStream;
    private BufferedWriter bufferedWriter;

    public DataSender(SocketServer server) throws IOException {
        this.server = server;

        server.createServer();
        server.createSocket();
        outputStream = new BufferedOutputStream(server.getSocket().getOutputStream());
//        outputStream = new ObjectOutputStream(server.getSocket().getOutputStream());
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    public void sendString(String string) throws IOException {

        System.out.println("sending: " + string);

        bufferedWriter.write(string);
        bufferedWriter.flush();
//        outputStream.flush();
    }

    @Override
    protected void finalize() throws Throwable {
        server.closeSocket();
        server.closeServer();
    }
}
