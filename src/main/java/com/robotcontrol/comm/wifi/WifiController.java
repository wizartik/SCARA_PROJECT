package com.robotcontrol.comm.wifi;

import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.comm.DataFormer;

import java.io.IOException;

public class WifiController {

    private SocketServer server;
    private DataReceiver dataReceiver;
    private DataSender dataSender;

    public WifiController() throws IOException {
        init();
    }

    private void init() throws IOException {
        server = new SocketServer();
        server.createServer();
        server.createSocket();
        dataReceiver = new DataReceiver(server);
        dataReceiver.resolveData();
        dataSender = new DataSender(server);
    }

    public void sendData(SteppersPath steppersPath, int stepper) throws IOException {
        DataFormer dataFormer = new DataFormer();

        dataSender.sendString(dataFormer.makeDataForOneStepper(steppersPath, stepper));
    }

    public void sendString(String string) throws IOException {
        dataSender.sendString(string);
    }

    public void stopListening() {
        dataReceiver.setListening(false);
    }

    public boolean isConnected() {
        return server != null && server.getServer() != null && server.getServer().isBound() && server.getSocket() != null && server.getSocket().isConnected();
    }

    public void closeConnection() throws IOException {
        stopListening();
        server.closeSocket();
        server.closeServer();
    }
}
