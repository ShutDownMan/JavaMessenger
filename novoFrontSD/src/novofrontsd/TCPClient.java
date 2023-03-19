package novofrontsd;

import java.io.IOException;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import message.Message;
import message.MessageType;

public class TCPClient {
    private String serverName;
    private int serverPort;
    private boolean running = false;
    private ClientSocket clientSocket = null;
    private Thread clientThread = null;

    private HashMap<MessageType, ArrayList<Function>> callbacks = new HashMap<>();

    public TCPClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void start() {
        if (running) {
            return;
        }

        try {
            System.out.println("Connecting to " + serverName + " on port " + serverPort);
            clientSocket = new ClientSocket(serverName, serverPort);
            System.out.println("Starting client thread...");
            clientThread = new Thread(new ClientThread());
            clientThread.start();
            running = true;
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

    public boolean isConnected() {
        return running;
    }

    // function that receives a message type and a lambda function
    // and adds the lambda function to the list of callbacks for that message type
    public void addOn(MessageType type, Function callback) {
        if (!callbacks.containsKey(type)) {
            callbacks.put(type, new ArrayList<>());
        }
        callbacks.get(type).add(callback);
    }

    private class ClientThread implements Runnable {
        @Override
        public void run() {
            while (running) {
                Message message = clientSocket.receiveMessage();
                if (message != null) {
                    System.out.println("Calling callback for message type " + message.getType());
                    if (callbacks.containsKey(message.getType())) {
                        System.out.println("Calling " + callbacks.get(message.getType()).size() + " callbacks");
                        for (Function callback : callbacks.get(message.getType())) {
                            callback.apply(message);
                        }
                    }
                }
            }
        }
    }
}
