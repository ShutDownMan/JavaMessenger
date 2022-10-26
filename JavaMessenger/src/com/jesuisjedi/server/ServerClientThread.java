package com.jesuisjedi.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.ArrayList;
import com.jesuisjedi.message.Message;
import com.jesuisjedi.message.MessageType;

class ServerClientThread extends Thread {
    /**
     * Thread that handles the client connection.
     */
    private final TCPServer tcpServer;
    private String clientName;
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerClientThread(TCPServer tcpServer, Socket clientSocket) {
        this.tcpServer = tcpServer;
        this.clientSocket = clientSocket;
    }

    String getClientName() {
        return clientName;
    }

    public void sendMessage(Message message) {
        try {
            System.out.println("Sending message: " + message.toString());
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            in = new ObjectInputStream(clientSocket.getInputStream());
            //out = new PrintWriter(clientSocket.getOutputStream(), true);
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            /// while, read Message object from socket
            while (true) {
                Message message = (Message) in.readObject();
                System.out.println("Received message: " + message.toString());

                handleMessage(message);
            }
        } catch(EOFException e) {
            System.out.println("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                /// check if clientSocket is already closed
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(Message message) {
        switch (message.type) {
            case CONTROL:
                handleControlMessage(message);
                break;
            case TEXT:
                handleTextMessage(message);
                break;
        }
    }

    private void handleControlMessage(Message message) {
        switch ((String) message.payload) {
            case "CONNECT":
                /// set client name
                clientName = message.sender;
                break;
            case "DISCONNECT":
                /// remove client from list
                handleDisconnectMessage(message);
                break;
            case "LIST":
                /// send list of clients
                handleListMessage(message);
                break;
        }
    }

    private void handleDisconnectMessage(Message message) {
        /// remove client from list
        tcpServer.removeClient(this);

        /// send disconnect message to client
        sendMessage(new Message(MessageType.CONTROL, "DISCONNECTED", "SERVER", null));

        /// close socket
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleListMessage(Message message) {
        /// send list of connected clients
        ArrayList<String> recipients = new ArrayList<String>();
        recipients.add(message.sender);
        
        sendMessage(new Message(MessageType.CONTROL, tcpServer.getClientNames(), "SERVER", recipients));
    }

    private void handleTextMessage(Message message) {
        /// get client names
        HashMap<String, ServerClientThread> clients = tcpServer.getClients();

        /// filter recipients
        ArrayList<String> recipients = (ArrayList<String>) message.recipients;
        ArrayList<String> validRecipients = new ArrayList<String>();

        for (String recipient : recipients) {
            if (clients.containsKey(recipient)) {
                validRecipients.add(recipient);
            }
        }

        /// send message to valid recipients
        for (String recipient : validRecipients) {
            ServerClientThread client = clients.get(recipient);
            client.sendMessage(message);
        }
    }
}