package com.jesuisjedi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer {

    private int port;
    private boolean running = false;
    private DatagramSocket socket;
    private byte[] buf = new byte[256];
    
    public UDPServer(int port) throws SocketException {
        this.port = port;
        this.socket = new DatagramSocket(this.port);
    }

    public void start() throws IOException {
        running = true;

        System.out.println("Server started on port " + this.port);

        while (running) {

            // Wait for a client connection
            DatagramPacket packet 
            = new DatagramPacket(buf, buf.length);
            this.socket.receive(packet);

            System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort());

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received 
              = new String(packet.getData(), 0, packet.getLength());

              System.out.println("Received: " + received);
            
            if (received.equals("end")) {
                running = false;
                continue;
            }
            this.socket.send(packet);
        }
        this.socket.close();
    }
}
