package com.jesuisjedi.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerUDPClient {
    private int port;
    private InetAddress ipAddress;
    private String username;

    public ServerUDPClient(InetAddress ipAddress, int port, String username) throws UnknownHostException {
        this.port = port;
        this.ipAddress = ipAddress;
        this.username = username;
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

    public String getUsername() {
        return username;
    }
}
