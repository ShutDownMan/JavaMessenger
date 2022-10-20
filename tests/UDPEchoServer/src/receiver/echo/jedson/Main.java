package receiver.echo.jedson;

public class Main {

	public static void main(String[] args) {
		// call the UDPEchoListener class
		try {
			UDPEchoListener listener = new UDPEchoListener();
			listener.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
