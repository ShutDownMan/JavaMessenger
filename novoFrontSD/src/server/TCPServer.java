package com.jesuisjedi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class TCPServer {
    private int port;
    private boolean running = false;
    private ServerSocket serverSocket = null;
    private Thread serverThread = null;
    private ArrayList<ServerClientThread> clientThreads = new ArrayList<ServerClientThread>();

    public TCPServer(int port) {
        this.port = port;
    }

    public void start() {
        if (running) {
            return;
        }

        running = true;
        try {
            serverSocket = new ServerSocket(port);
            serverThread = new Thread(new ServerThread());
            serverThread.start();
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
            serverSocket.close();
            serverThread.join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    

    private class ServerThread implements Runnable {
        @Override
        public void run() {
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ServerClientThread clientThread = new ServerClientThread(TCPServer.this, clientSocket);
                    clientThreads.add(clientThread);
                    clientThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ServerClientThread[] getClientThreads() {
        return clientThreads.toArray(new ServerClientThread[clientThreads.size()]);
    }

    public ArrayList<String> getClientNames() {
        ArrayList<String> clientNames = new ArrayList<String>();
        for (ServerClientThread clientThread : clientThreads) {
            clientNames.add(clientThread.getClientName());
        }
        return clientNames;
    }

    public void removeClient(ServerClientThread serverClientThread) {
        clientThreads.remove(serverClientThread);
    }

    public HashMap<String, ServerClientThread> getClients() {
        /// return a dictionary of client names and client threads
        HashMap<String, ServerClientThread> clients = new HashMap<>();
        for (ServerClientThread clientThread : clientThreads) {
            clients.put((String) clientThread.getClientName(), clientThread);
        }
        return clients;
    }
}
