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

import com.jesuisjedi.message.Message;
import com.jesuisjedi.message.MessageType;

class UDPClient {

    private String host;
    private int port;
	private byte[] buf = new byte[5000];

    public UDPClient(String host, int port) throws SocketException, UnknownHostException {
        this.host = host;
        this.port = port;
    }

	public void start() throws Exception {

        System.out.println("Client started on port " + this.port);

		DatagramSocket clientSocket = new DatagramSocket();

		InetAddress IPAddress = InetAddress.getByName(host);
		System.out.println("IP address: " + IPAddress.getHostAddress());

		byte[] receiveData = new byte[1024];

		System.out.println("Digite o texto a ser enviado ao servidor: ");

		Message message = new Message(MessageType.CONTROL, "CONNECT", "jesuisjedi", new ArrayList<String>());

		// DatagramPacket sendPacket = createPacket(IPAddress, message);
		DatagramPacket sendPacket = createPacket(IPAddress, message);

		System.out.println("Enviando pacote UDP para " + host + ":" + this.port);
		clientSocket.send(sendPacket);

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

		clientSocket.receive(receivePacket);
		System.out.println("Pacote UDP recebido...");

		message = parsePacket(receivePacket);

		System.out.println("Texto recebido do servidor:" + message.toString());

		clientSocket.close();

		// String modifiedSentence = new String(receivePacket.getData());

		// System.out.println("Texto recebido do servidor:" + modifiedSentence);

		// Message disconnectionMessage = new Message(MessageType.CONTROL, "DISCONNECT", "jesuisjedi", new ArrayList<String>());
		// sendPacket = createPacket(IPAddress, disconnectionMessage);

		// clientSocket.send(sendPacket);
		// clientSocket.close();
		// System.out.println("Socket cliente fechado!");
	}
	
	private DatagramPacket createPacket(InetAddress address, Message message) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
		oos.flush();
		oos.writeObject(message);
		oos.flush();
		byte[] data = baos.toByteArray();
		oos.close();

		DatagramPacket packet = new DatagramPacket(data, data.length, address, this.port);

		return packet;
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
		System.out.println("Parsing packet");
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
		
        Message message = (Message) is.readObject();
        is.close();

        return message;
    }
}
