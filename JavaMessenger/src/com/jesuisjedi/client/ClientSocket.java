package com.jesuisjedi.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import com.jesuisjedi.message.Message;

public class ClientSocket {
    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;

    public ClientSocket(String serverName, int serverPort) throws IOException {
        socket = new Socket(serverName, serverPort);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public void sendMessage(Message message) {
        try {
            outputStream.writeObject(message);
            System.out.println("Sent message: " + message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message receiveMessage() {
        try {
            return (Message) inputStream.readObject();
        } catch (EOFException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
