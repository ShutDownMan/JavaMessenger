package com.jesuisjedi;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    /**
     * Test the server
     */
    @Test
    public void testServer()
    {
        /// initialize the server
        TCPServer server = new TCPServer(6666);

        /// start the server
        server.start();
    }

    /**
     * Test the client
     */
    @Test
    public void testClient()
    {
        /// initialize the client
        TCPClient client = new TCPClient("localhost", 6666);

        /// start the client
        client.start();
        Message message = null;

        /// send a connect message
        message = new Message(MessageType.CONTROL, "CONNECT", "jesuisjedi", new ArrayList<String>());
        client.sendMessage(message);

        /// list of users message
        message = new Message(MessageType.CONTROL, "LIST", "jesuisjedi", null);
        client.sendMessage(message);

        /// send a hello message to all users
        ArrayList<String> users = new ArrayList<>();
        users.add("jesuisjedi");
        message = new Message(MessageType.TEXT, "Hello World!", "jesuisjedi", users);
        client.sendMessage(message);

        /// send a disconnect message
        // message = new Message(MessageType.CONTROL, "DISCONNECT", "jesuisjedi", users);
        // client.sendMessage(message);

        /// wait a sec
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /// stop the client
        client.stop();
    }

    /**
     * Test the client on UDP connection
     * @throws Exception
     */
    @Test
    public void testUdpClient() throws Exception {
        // Initialize client
        UDPClient client = new UDPClient("localhost", 6667);

        // Start the client
        client.start();
    }
}
