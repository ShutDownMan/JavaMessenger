package com.jesuisjedi;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerUDPClient {
    private String address;
    private int port;
    private InetAddress ipAddress;
    private String name;

    public ServerUDPClient(String address, int port, InetAddress ipAddress) throws UnknownHostException {
        this.address = address;
        this.port = port;
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public String getName() {
        return name;
    }
}
