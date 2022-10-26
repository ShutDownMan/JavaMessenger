package com.jesuisjedi.client;

import java.io.IOException;
import java.io.EOFException;
import com.jesuisjedi.message.Message;

public class TCPClient {
    private String serverName;
    private int serverPort;
    private boolean running = false;
    private ClientSocket clientSocket = null;
    private Thread clientThread = null;

    public TCPClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void start() {
        if (running) {
            return;
        }

        running = true;
        try {
            clientSocket = new ClientSocket(serverName, serverPort);
            clientThread = new Thread(new ClientThread());
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!running) {
            return;
        }

        running = false;
        try {
            clientSocket.close();
            clientThread.join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) throws EOFException {
        clientSocket.sendMessage(message);
    }

    private class ClientThread implements Runnable {
        @Override
        public void run() {
            while (running) {
                Message message = clientSocket.receiveMessage();
                if (message != null) {
                    System.out.println("Received message: " + message.toString());
                }
            }
        }
    }
}
