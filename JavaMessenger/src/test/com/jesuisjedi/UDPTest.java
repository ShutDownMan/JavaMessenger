package test.com.jesuisjedi;

import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.Test;

public class UDPTest {
	
	@Test
	public void testUdpClient() throws Exception {
		// Initialize client
        UDPClient client = new UDPClient("localhost", 6667);

        // Start the client
        client.start();
	}
}
