package com.jesuisjedi.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerUDPClient {
    private int port;
    private InetAddress ipAddress;
    private String name;

    public ServerUDPClient(InetAddress ipAddress, int port) throws UnknownHostException {
        this.port = port;
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return ipAddress.getHostAddress();
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public String getName() {
        return name;
    }
}
