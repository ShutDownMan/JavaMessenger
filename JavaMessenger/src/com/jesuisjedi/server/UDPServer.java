package com.jesuisjedi.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.jesuisjedi.message.Message;

public class UDPServer {

    private int port;
    private boolean running = false;
    private DatagramSocket socket;
    private byte[] buf = new byte[5000];

    private ArrayList<ServerUDPClient> clients = new ArrayList<ServerUDPClient>();
    
    public UDPServer(int port) throws SocketException {
        this.port = port;
        this.socket = new DatagramSocket(this.port);
    }

    public void start() throws IOException, ClassNotFoundException {
        running = true;

        while (running) {

            // Wait for a packet
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            this.socket.receive(packet);

            System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort());

            // Parse packet to message
            Message message = parsePacket(packet);

            System.out.println("Received message: " + message.toString());
            handleMessage(packet, message);

            String received = new String(packet.getData(), 0, packet.getLength());

            if (received.equals("end")) {
                running = false;
                continue;
            }
            this.socket.send(packet);
        }
        
        this.socket.close();
    }

    /**
     * Parse a packet to a message
     * @param packet
     * @return receveid message on packet
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Message parsePacket(DatagramPacket packet) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(buf);
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));

        Message message = (Message) is.readObject();
        is.close();

        return message;
    }

    /**
     * Parse the message received on UDP packet
     */
    private void handleMessage(DatagramPacket packet, Message message) throws UnknownHostException {
        // Connection packet
        switch ((String) message.payload) {
            case "CONNECT":
                /// set client name
                System.out.println("Conexão recebida!");
                handleConnection(packet);
                break;
            case "DISCONNECT":
                /// remove client from list
                handleDisconnection(packet);
                System.out.println("Desconexão recebida!");
                break;
        }
    }

    /*
     * Handle connection packet
     * Add new ServerUDPClient at clients list
     * @param DatagramPacket packet
     */
    private void handleConnection(DatagramPacket packet) throws UnknownHostException {
       ServerUDPClient newClient = new ServerUDPClient(packet.getAddress(), packet.getPort());
       clients.add(newClient);
    }

    private void handleDisconnection(DatagramPacket packet) {
        clients.removeIf(client -> client.getAddress().equals(packet.getAddress().getHostAddress()));
    }

    // TO DO: Get clients list from UDP packet message on handleMessage
    public void broadcast() throws IOException {
        for (ServerUDPClient client : clients) {
            String message = "Hello from server";
            
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, client.getIpAddress(), client.getPort());
            this.socket.send(packet);
        }
    }
}
