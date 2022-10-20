package receiver.echo.jedson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPEchoSender {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    public TCPEchoSender(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendEcho(String msg) {
        /// send the message to the server and return the echoed message
        out.println(msg);
        String echo = null;
        try {
            echo = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return echo;
    }

    public void close() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
