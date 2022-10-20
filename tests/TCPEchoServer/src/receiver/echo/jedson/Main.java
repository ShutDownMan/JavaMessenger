package receiver.echo.jedson;

public class Main {

	public static void main(String[] args) {
		try {
			TCPEchoListener listener = new TCPEchoListener(6666);
			listener.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
