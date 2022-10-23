package com.jesuisjedi.SerializeTest.Sender;

/*
 * Writer
 */
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import com.jesuisjedi.SerializeTest.EnumTest;
import com.jesuisjedi.SerializeTest.TestMessage;

public class Sender {
    public static void main(String[] args) throws IOException {
        System.out.println("Sender Start");

        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.configureBlocking(true);
        int port = 12345;
        ssChannel.socket().bind(new InetSocketAddress(port));

        ArrayList<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        list.add("!");

        TestMessage msg = new TestMessage(EnumTest.JEAN, "Hello World", list);
        while (true) {
            SocketChannel sChannel = ssChannel.accept();

            ObjectOutputStream oos = new ObjectOutputStream(sChannel.socket().getOutputStream());
            oos.writeObject(msg);
            oos.close();

            System.out.println("Connection ended");
        }
    }
}