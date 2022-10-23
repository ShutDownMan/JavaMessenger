package com.jesuisjedi;

import java.io.*;
import java.net.*;

class UDPClient {

    private String host;
    private int port;

    public UDPClient(String host, int port) throws SocketException, UnknownHostException {
        this.host = host;
        this.port = port;
    }

	public void start() throws Exception {

        System.out.println("Client started on port " + this.port);

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));

		DatagramSocket clientSocket = new DatagramSocket();

		InetAddress IPAddress = InetAddress.getByName(host);
		System.out.println("IP address: " + IPAddress.getHostAddress());

		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];

		System.out.println("Digite o texto a ser enviado ao servidor: ");
		String sentence = inFromUser.readLine();
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,
				sendData.length, IPAddress, this.port);

		System.out
				.println("Enviando pacote UDP para " + host + ":" + this.port);
		clientSocket.send(sendPacket);

		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);

		clientSocket.receive(receivePacket);
		System.out.println("Pacote UDP recebido...");

		String modifiedSentence = new String(receivePacket.getData());

		System.out.println("Texto recebido do servidor:" + modifiedSentence);
		clientSocket.close();
		System.out.println("Socket cliente fechado!");
	}
}
