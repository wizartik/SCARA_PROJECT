package com.robotcontrol.comm.wifi;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class DataSender {

    private SocketServer server;

    private ObjectOutputStream outputStream;

    public DataSender(SocketServer server) throws IOException {
        this.server = server;

        server.createServer();
        server.createSocket();
        outputStream = new ObjectOutputStream(server.getSocket().getOutputStream());
    }

    public void sendString(String string) throws IOException {
        outputStream.writeChars(string);
        outputStream.flush();
    }

    @Override
    protected void finalize() throws Throwable {
        server.closeSocket();
        server.closeServer();
    }
}
