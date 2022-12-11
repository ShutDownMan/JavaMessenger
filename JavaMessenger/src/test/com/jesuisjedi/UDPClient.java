package test.com.jesuisjedi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

import javax.xml.crypto.Data;

import com.jesuisjedi.message.Message;
import com.jesuisjedi.message.MessageType;

class UDPClient {

    private int port;
	private byte[] buf = new byte[5000];
	private DatagramSocket clientSocket = new DatagramSocket();
	private InetAddress address;

    public UDPClient(String host, int port) throws SocketException, UnknownHostException {
        this.port = port;
		this.address = InetAddress.getByName(host);
    }

	public void connect() throws Exception {
		Message message = new Message(MessageType.CONTROL, "CONNECT", "casseb", new ArrayList<String>());

		DatagramPacket sendPacket = createPacket(message);

		System.out.println("Enviando mensagem: " + message.toString());
		clientSocket.send(sendPacket);

		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

		clientSocket.receive(receivePacket);

		Message receivedMessage = parsePacket();

		System.out.println("Mensagem recebida do servidor:" + receivedMessage.toString());
	}

	public void disconnet() throws Exception {
		Message message = new Message(MessageType.CONTROL, "DISCONNECT", "casseb", new ArrayList<String>());

		DatagramPacket sendPacket = createPacket(message);

		System.out.println("Enviando mensagem: " + message.toString());
		clientSocket.send(sendPacket);

		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

		clientSocket.receive(receivePacket);

		Message receivedMessage = parsePacket();

		System.out.println("Mensagem recebida do servidor:" + receivedMessage.toString());
	}

	public void listUsers() throws Exception {
		Message message = new Message(MessageType.CONTROL, "LIST", "casseb", new ArrayList<String>());

		DatagramPacket sendPacket = createPacket(message);

		System.out.println("Enviando mensagem: " + message.toString());
		clientSocket.send(sendPacket);

		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

		clientSocket.receive(receivePacket);

		Message receivedMessage = parsePacket();

		System.out.println("Mensagem recebida do servidor:" + receivedMessage.toString());
	}
	
	private DatagramPacket createPacket(Message message) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
		oos.flush();
		oos.writeObject(message);
		oos.flush();
		byte[] data = baos.toByteArray();
		oos.close();

		DatagramPacket packet = new DatagramPacket(data, data.length, this.address, this.port);

		return packet;
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
}
