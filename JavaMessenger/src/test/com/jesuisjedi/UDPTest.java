package test.com.jesuisjedi;

import org.junit.Test;

public class UDPTest {

	private UDPClient client;
	
	public UDPTest() throws Exception {
		client = new UDPClient("localhost", 6667);
	}
	
	@Test
	public void testUdpConnection() throws Exception {
        // Start the client
        client.connect();
		client.disconnet();
	}

	@Test
	public void testListUsers() throws Exception {
		client.listUsers();
	}
}
