package receiver.echo.jedson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/// echo server listens for TCP packets and echoes them back
public class TCPEchoListener {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    /// create a new echo server
    public TCPEchoListener(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        System.out.println("Connection established");

        // listen for messages
        String inputLine;
        while(true) {
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                out.println(inputLine);
            }
        }

        // close the connection
        // out.close();
        // in.close();
        // clientSocket.close();
    }

    public void run() {

    }
}
