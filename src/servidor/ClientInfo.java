package servidor;

import java.net.Socket;

public class ClientInfo {
	int myCpu, myMemory, myBlock;
	private Socket socket;
	
	
	public ClientInfo(Socket accept) {
		socket = accept;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getAddress() {
		return socket.getInetAddress().getHostAddress();
	}
	
}
