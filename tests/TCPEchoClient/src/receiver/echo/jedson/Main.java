package receiver.echo.jedson;

public class Main {

	public static void main(String[] args) {
		// call the TCPEchoSender class
		try {
			TCPEchoSender sender = new TCPEchoSender("127.0.0.1", 6666);
			String echo = null;
			do {
				echo = sender.sendEcho("Hello World!");
				System.out.println("Echo: " + echo);
				// sleep for 1 second
				Thread.sleep(1000);
			} while(echo != null);
			
			// sender.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
