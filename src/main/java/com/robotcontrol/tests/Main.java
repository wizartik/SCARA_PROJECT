package com.robotcontrol.tests;

import com.robotcontrol.calc.contouringControl.controllers.data.DataController;
import com.robotcontrol.calc.contouringControl.controllers.path.PathController;
import com.robotcontrol.calc.contouringControl.entities.path.ContourPath;
import com.robotcontrol.calc.stepperControl.controllers.PathConverter;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.comm.serialPort.PortController;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.exc.WrongInputData;
import jssc.SerialPortException;

import java.io.File;
import java.io.IOException;

public class Main {


//    private static SerialPort serialPort;
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        String[] portNames = SerialPortList.getPortNames();
//
//        if (portNames.length == 0) {
//            System.out.println("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
//            System.out.println("Press Enter to exit...");
//            try {
//                System.in.read();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        // выбор порта
//        System.out.println("Available com-ports:");
//        for (int i = 0; i < portNames.length; i++){
//            System.out.println(portNames[i]);
//        }
//        System.out.println("Type port name, which you want to use, and press Enter...");
//        Scanner in = new Scanner(System.in);
//        String portName = in.next();
//
//        // writing to port
//        serialPort = new SerialPort(portName);
//        try {
//            // opening port
//            serialPort.openPort();
//
//            serialPort.setParams(SerialPort.BAUDRATE_9600,
//                    SerialPort.DATABITS_8,
//                    SerialPort.STOPBITS_1,
//                    SerialPort.PARITY_NONE);
//
//            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
//                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
//
//            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
//            // writing string to port
//            serialPort.writeString("Hurrah!!!");
//
//            System.out.println("String wrote to port, waiting for response..");
//        }
//        catch (SerialPortException ex) {
//            System.out.println("Error in writing data to port: " + ex);
//        }
//    }
//
//    // receiving response from port
//    private static class PortReader implements SerialPortEventListener {
//
//        @Override
//        public void serialEvent(SerialPortEvent event) {
//            if(event.isRXCHAR() && event.getEventValue() > 0) {
//                try {
//                    // получение ответа от порта
//                    String receivedData = serialPort.readString(event.getEventValue());
//                    System.out.println("Received response from port: " + receivedData);
//                }
//                catch (SerialPortException ex) {
//                    System.out.println("Error in receiving response from port: " + ex);
//                }
//            }
//        }
//    }

    public static void main(String[] args) throws WrongExtension, BoundsViolation, WrongInputData, IOException, ImpossibleToImplement, SerialPortException {

        ContourPath contourPath = PathController.makePath(DataController.convertToGCode(new File("D:\\GCodes\\test.txt")));
//
        SteppersPath steppersPath = PathConverter.convertToSteppersPath(contourPath);//
//

        PortController.sendSteppersPath(steppersPath);
//        System.out.println(steppersPath.getSteppersPoints().size());

        // getting serial ports list into the array
//

    }
}
