package com.jesuisjedi;

/**
 * This application is a messanger server using TCP sockets.
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
