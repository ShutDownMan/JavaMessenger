package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import message.Message;
import message.MessageType;

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

        System.out.println("Running UDP server on port " + this.port);

        while (running) {

            // Wait for a packet
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            this.socket.receive(packet);

            System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort());

            // Parse packet to message
            Message message = parsePacket();

            handleMessage(packet, message);
        }

        System.out.println("Closing UDP server");
        
        this.socket.close();
    }

    /**
     * Parse the message received on UDP packet
     */
    private void handleMessage(DatagramPacket packet, Message message) throws UnknownHostException {
        switch (message.type) {
            case CONTROL:
                handleControlMessage(packet, message);
                break;
            case TEXT:
            handleTextMessage(packet, message);
                break;
        }

    }

    private void handleControlMessage(DatagramPacket packet, Message message) throws UnknownHostException {
        // Connection packet
        switch ((String) message.payload) {
            case "CONNECT":
                System.out.println("Conexão recebida!");
                handleConnection(packet, message);
                printClients();
                sendListMessage();
                break;
            case "DISCONNECT":
                handleDisconnection(packet);
                System.out.println("Desconexão recebida!");
                printClients();
                break;
            case "LIST":
                System.out.println("Listagem recebida");
                handleListMessage(packet, message);
                break;
        }
    }

    private void handleTextMessage(DatagramPacket packet, Message message) {
        switch (message.type) {
            case TEXT:
                handleChatMessage(packet, message);
                break;
            default:
                break;
        }
    }

    /*
     * Handle connection packet
     * Add new ServerUDPClient at clients list
     * @param DatagramPacket packet
     */
    private void handleConnection(DatagramPacket packet, Message message) throws UnknownHostException {
       ServerUDPClient newClient = new ServerUDPClient(packet.getAddress(), packet.getPort(), message.sender);
       clients.add(newClient);

        Message connectedMessage = new Message(MessageType.CONTROL, "CONNECTED", "SERVER", null);
        System.out.println("Sending connected message" + connectedMessage.toString());

        sendMessage(connectedMessage, packet.getAddress(), packet.getPort());
    }

    private void handleDisconnection(DatagramPacket packet) {
        clients.removeIf(client -> client.getAddress().equals(packet.getAddress().getHostAddress()));

        sendMessage(new Message(MessageType.CONTROL, "DISCONNECTED", "SERVER", null), packet.getAddress(), packet.getPort());
    }

    private void handleListMessage(DatagramPacket packet, Message message) {
        ArrayList<String> connectedClients = new ArrayList<String>();
        for ( ServerUDPClient client : clients) {
            connectedClients.add(client.getUsername());
        }
        
        System.out.println("Sending list: " + connectedClients.toString());
        sendMessage(new Message(MessageType.CONTROL, connectedClients, "SERVER", message.recipients), packet.getAddress(), packet.getPort());
    }

    /**
     * Send list message to every user
     */
    private void sendListMessage() {
        ArrayList<String> connectedClients = new ArrayList<String>();
        for ( ServerUDPClient client : clients) {
            connectedClients.add(client.getUsername());
        }

        for ( ServerUDPClient client : clients) {
            sendMessage(new Message(MessageType.CONTROL, "LIST", "SERVER", connectedClients), client.getIpAddress(), client.getPort());
        }
    }

    /**
     * Send message to recipients list
     * @param packet
     * @param message
     */
    private void handleChatMessage(DatagramPacket packet, Message message) {
        boolean broadcast = false;
        if ( message.recipients.size() == 1 && message.recipients.get(0).equals("Chat Geral") ) {
            broadcast = true;
        }

        for (String username : message.recipients) {
            for (ServerUDPClient serverClient : clients) {
                if (broadcast || serverClient.getUsername().equals(username)) {
                    System.out.println("Enviando mensagem para " + username);
                    sendMessage(message, serverClient.getIpAddress(), serverClient.getPort());
                }
            }
        }
    }

    private void sendMessage(Message message, InetAddress address, int port) {
        try {
            DatagramPacket packet = createPacket(address, port, message);
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse a packet to a message
     * @param packet
     * @return receveid message on packet
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Message parsePacket() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(buf);
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
		
        Message message = (Message) is.readObject();
        is.close();

        return message;
    }

    private DatagramPacket createPacket(InetAddress address, int port, Message message) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
		oos.flush();
		oos.writeObject(message);
		oos.flush();
		byte[] data = baos.toByteArray();
		oos.close();

		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

		return packet;
	}

    private void printClients() {
        System.out.println("Clients: ");
        for (ServerUDPClient client : clients) {
            System.out.println(client.getAddress() + ":" + client.getPort());
        }
    }
}
