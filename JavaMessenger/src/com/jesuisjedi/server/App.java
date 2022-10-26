package com.jesuisjedi.server;

/**
 * This application is a messenger server using TCP sockets.
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        /// initialize the server
        TCPServer server = new TCPServer(6666);

        /// start the server
        server.start();
    }
}
