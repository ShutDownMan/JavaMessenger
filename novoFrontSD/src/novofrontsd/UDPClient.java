package novofrontsd;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import message.Message;
import message.MessageType;

class UDPClient implements Runnable {

    private int port;
	private byte[] buf = new byte[5000];
	private DatagramSocket clientSocket = new DatagramSocket();
	private InetAddress address;
	private boolean running = false;
	public String username;
	public ArrayList<panelChat> tabs;
    public JList<ItemUser> listaUsers;
	public javax.swing.JTabbedPane tabbedPaneChat;


    public UDPClient(String host, int port, String username) throws SocketException, UnknownHostException {
        this.port = port;
		this.address = InetAddress.getByName(host);
		this.username = username;
    }

	public void run() {
		try {
			receiveMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connect() throws Exception {
		Message message = new Message(MessageType.CONTROL, "CONNECT", username, new ArrayList<String>());

		DatagramPacket sendPacket = createPacket(message);

		System.out.println("Enviando mensagem: " + message.toString());
		clientSocket.send(sendPacket);

		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

		clientSocket.setSoTimeout(5000);
		clientSocket.receive(receivePacket);
		clientSocket.setSoTimeout(0);

		Message receivedMessage = parsePacket();
		receivedMessage.decryptMessage();

		if (receivedMessage.payload.equals("CONNECTED")) {
			System.out.println("Conectado com sucesso");
		} else {
			throw new Exception("Erro ao conectar");
		}
	}

	public void disconnet() throws Exception {
		Message message = new Message(MessageType.CONTROL, "DISCONNECT", username, new ArrayList<String>());

		DatagramPacket sendPacket = createPacket(message);

		System.out.println("Enviando mensagem: " + message.toString());
		clientSocket.send(sendPacket);

		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

		clientSocket.receive(receivePacket);

		Message receivedMessage = parsePacket();

		System.out.println("Mensagem recebida do servidor:" + receivedMessage.toString());
	}

	public ArrayList<String> listUsers() throws Exception {
        System.out.println("calling listusers");
		Message message = new Message(MessageType.CONTROL, "LIST", username, new ArrayList<String>());

		DatagramPacket sendPacket = createPacket(message);

		System.out.println("Enviando mensagem: " + message.toString());
		clientSocket.send(sendPacket);

		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

		clientSocket.receive(receivePacket);

		Message receivedMessage = parsePacket();
		System.out.println("Listagem recebida!! " + receivedMessage.toString());
		ArrayList<String> users = (ArrayList<String>) receivedMessage.payload;

		return users;
	}

	public void receiveMessages() throws Exception {
		running = true;
		while (running) {
			DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
			clientSocket.receive(receivePacket);

			Message receivedMessage = parsePacket();
			receivedMessage.decryptMessage();

			System.out.println("Mensagem recebida do servidor:" + receivedMessage.toString());
			
			// Check if message is control type
			if (receivedMessage.type == MessageType.CONTROL) {
				if (receivedMessage.payload.equals("LIST")) {
					ArrayList<String> users = (ArrayList<String>) receivedMessage.recipients;
					this.updateList(this.mountUsersList(users));
				}
			}

			if (!receivedMessage.sender.equals("SERVER")) {
				String panelUser;
				if (receivedMessage.recipients.get(0).equals("Chat Geral")) {
					panelUser = "Chat Geral";
				} else {
					panelUser = receivedMessage.sender;
				}

				panelChat panel = encontrarPanelUser(panelUser);
				if (panel == null) {
					panel = new panelChat(panelUser);
					tabs.add(panel);
					this.tabbedPaneChat.add(panel.getNome(), panel);
				}
				panel.setChat("(" + receivedMessage.time + ") " + receivedMessage.sender + ": " + receivedMessage.payload.toString());
			}
		}
	}

	public void sendMessage(String payload, String destination) {
		try {
			ArrayList<String> users = new ArrayList<String>();
			users.add(destination);

			Message message = new Message(MessageType.TEXT, payload, username, users);
	
			DatagramPacket sendPacket = createPacket(message);
	
			System.out.println("Enviando mensagem: " + message.toString());
			clientSocket.send(sendPacket);

		} catch ( Exception e ) {
			e.printStackTrace();
		}
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

	private panelChat encontrarPanelUser(String nome){
		for (panelChat tab : tabs) {
			System.out.println(tab.getNome());
			if (tab.getNome().equals(nome)) {
				return tab;
			}
		}

		return null;
    }

	/**
	 * Mount the users list with ItemUser class.
	 * @param users
	 * @return
	 */
	private ArrayList<ItemUser> mountUsersList(ArrayList<String> users) {
		ArrayList<ItemUser> usersList = new ArrayList<>();

		ItemUser broadcast = new ItemUser("Chat Geral");
        usersList.add(broadcast);
		
		for(String user : users) {
			if (!user.equals(this.username)) {
				usersList.add(new ItemUser(user));
			}
		}

		return usersList;
	}

	// Update the news list.
	private void updateList(ArrayList<ItemUser> users) {
		// Cleaning the list.
		DefaultListModel model = new DefaultListModel();
		model.clear();
		this.listaUsers.setModel(model);
		// Creating the new model.
		model = new DefaultListModel();
		int size = users.size();
		for(int i = 0; i < size; i++) {
			model.addElement(users.get(i));
		}
		this.listaUsers.setModel(model);
	} 
}
