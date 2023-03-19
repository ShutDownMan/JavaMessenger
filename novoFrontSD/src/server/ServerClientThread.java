package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.ArrayList;
import message.Message;
import message.MessageType;

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

    public void sendMessage(Message message) throws EOFException {
        try {
            System.out.println("Sending message: " + message.toString());
            // check if socket is open
            if (!clientSocket.isClosed()) {
                out.writeObject(message);
            } else {
                System.out.println("Client socket terminated");
                throw new EOFException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            // in = new BufferedReader(new
            // InputStreamReader(clientSocket.getInputStream()));
            in = new ObjectInputStream(clientSocket.getInputStream());
            // out = new PrintWriter(clientSocket.getOutputStream(), true);
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            /// while, read Message object from socket
            while (true) {
                Message message = (Message) in.readObject();
                System.out.println("Received message: " + message.toString());

                handleMessage(message);
            }
        } catch (EOFException e) {
            System.out.println("Client \'" + clientName + "\' is disconnected");
            // handle disconnect
            handleClientDisconnect(clientName);
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
            case CONNECT:
                handleConnectMessage(message);
                break;
            case LIST_USERS:
                handleListMessage(message);
                break;
            default:
                System.out.println("Unknown message type");
                break;
        }
    }

    private void handleControlMessage(Message message) {
        switch ((String) message.payload) {
            case "DISCONNECT":
                /// remove client from list
                handleDisconnectMessage(message);
                break;
        }
    }

    private void handleDisconnectMessage(Message message) {
        /// remove client from list
        tcpServer.removeClient(this);

        /// send disconnect message to client
        try {
            this.sendMessage(new Message(MessageType.CONTROL, "DISCONNECTED", "SERVER", null));
        } catch (EOFException e) {
            e.printStackTrace();
        }

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

        try {
            tcpServer.sendMessage(new Message(MessageType.LIST_OF_USERS, tcpServer.getClientNames(), "SERVER", recipients));
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        try {
            tcpServer.sendMessage(message);
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnectMessage(Message message) {
        /// check if client name is already taken
        if (tcpServer.getClientNames().contains((String)message.payload)) {
            /// disconnect the other client
            ServerClientThread sct = tcpServer.getClients().get((String)message.payload);
            sct.handleDisconnectMessage(new Message(MessageType.CONTROL, "DISCONNECT", "SERVER", null));
        }

        /// set client name
        clientName = message.sender;

        /// broadcast connect message
        try {
            tcpServer.sendMessage(new Message(MessageType.CONNECTED, clientName, "SERVER", tcpServer.getClientNames()));
        } catch (EOFException e) {
            handleClientDisconnect(clientName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientDisconnect(String clientName) {
        System.out.println("handleClientDisconnect");
        /// remove client from list
        tcpServer.removeClient(this);

        /// send disconnect message to other clients
        try {
            tcpServer.sendMessage(new Message(MessageType.CONTROL, "DISCONNECT", clientName, tcpServer.getClientNames()));
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}