package com.jesuisjedi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class UDPServer {

    private int port;
    private boolean running = false;
    private DatagramSocket socket;
    private byte[] buf = new byte[256];

    private ArrayList<ServerUDPClient> clients = new ArrayList<ServerUDPClient>();
    
    public UDPServer(int port) throws SocketException {
        this.port = port;
        this.socket = new DatagramSocket(this.port);
    }

    public void start() throws IOException {
        running = true;

        System.out.println("Server started on port " + this.port);

        while (running) {

            // Wait for a received packet
            DatagramPacket packet 
            = new DatagramPacket(buf, buf.length);
            this.socket.receive(packet);

            System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort());
            
            int connectionType = 1;
            // Check if is connection packet
            if (connectionType == 2) {
                ServerUDPClient newClient = new ServerUDPClient(packet.getAddress().getHostAddress(), packet.getPort(), packet.getAddress());
                clients.add(newClient);

            } else if (connectionType == 3) { // Check if is a disconnection packet
                ServerUDPClient connectedClient = new ServerUDPClient(packet.getAddress().getHostAddress(), packet.getPort(), packet.getAddress());
                clients.remove(connectedClient);
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received 
              = new String(packet.getData(), 0, packet.getLength());

              System.out.println("Received address:" + packet.getAddress());
            
            if (received.equals("end")) {
                running = false;
                continue;
            }
            this.socket.send(packet);
        }
        this.socket.close();
    }

    /**
     * Parse the message received on UDP packet
     */
    private void handleMessage() {
        // TODO
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
