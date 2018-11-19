package com.robotcontrol.comm.wifi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.robotcontrol.parameters.constant.Communication.PORT;

public class SocketServer {

    private ServerSocket server;

    private Socket socket;

    protected ServerSocket createServer() throws IOException {
        if (server == null) {
            server = new ServerSocket(PORT);
        }
        return server;
    }

    protected Socket createSocket() throws IOException {
        if (server == null){
            createServer();
        }

        if (socket == null) {
            socket = server.accept();
        }

        return socket;
    }

    protected void closeServer() throws IOException {
        server.close();
        server = null;
    }

    protected void closeSocket() throws IOException {
        socket.close();
        socket = null;
    }

    public ServerSocket getServer() {
        return server;
    }

    public Socket getSocket() {
        return socket;
    }
}
