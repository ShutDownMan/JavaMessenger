package receiver.echo.jedson;

public class Main {

	public static void main(String[] args) {
		// call the UDPEchoSender class
		try {
			UDPEchoSender sender = new UDPEchoSender();
			String echo = sender.sendEcho("Hello World");
			sender.close();
			System.out.println("Echoed: " + echo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
