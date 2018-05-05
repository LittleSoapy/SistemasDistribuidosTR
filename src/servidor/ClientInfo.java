package servidor;

import java.net.Socket;

public class ClientInfo {
	int myCpu, myMemory, MyBlock;
	private Socket socket;
	
	
	public ClientInfo(Socket accept) {
		socket = accept;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	
	
}
