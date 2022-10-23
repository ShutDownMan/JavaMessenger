package com.jesuisjedi.SerializeTest.Reader;

/*
 * Reader
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import com.jesuisjedi.SerializeTest.TestMessage;

public class Reader {
    public static void main(String[] args)
            throws IOException, ClassNotFoundException {
        System.out.println("Reader Start");

        SocketChannel sChannel = SocketChannel.open();
        sChannel.configureBlocking(true);
        if (sChannel.connect(new InetSocketAddress("localhost", 12345))) {

            ObjectInputStream ois = new ObjectInputStream(sChannel.socket().getInputStream());

            TestMessage s = (TestMessage) ois.readObject();
            System.out.println("String is: '" + s + "'");
        }

        System.out.println("End Reader");
    }
}