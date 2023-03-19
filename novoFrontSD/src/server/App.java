package server;

import java.io.IOException;

/**
 * This application is a messenger server using TCP sockets.
 *
 */
public class App
{
    public static void main( String[] args ) throws IOException
    {
        /// initialize the server
        TCPServer server = new TCPServer(6666);
        // UDPServer udpServer = new UDPServer(6667);

        /// start the server
        server.start();

        // start UDP server
        // udpServer.start();
    }
}
