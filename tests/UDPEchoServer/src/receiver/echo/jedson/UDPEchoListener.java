package receiver.echo.jedson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/// echo server listens for UDP packets and echoes them back
public class UDPEchoListener {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public UDPEchoListener() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run() throws IOException {
        running = true;

        while (running) {
            DatagramPacket packet 
              = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received 
              = new String(packet.getData(), 0, packet.getLength());
            
            if (received.equals("end")) {
                running = false;
                continue;
            }
            socket.send(packet);
        }
        socket.close();
    }
}
