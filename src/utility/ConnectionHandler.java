package utility;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionHandler implements Runnable {

	private Socket socket;
	private MessageReceiver receiver;
	private Scanner s;

	public ConnectionHandler(Socket socket, MessageReceiver receiver) {
		this.receiver = receiver;
		this.socket = socket;
		try {
			this.s = new Scanner(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		boolean cleanDisconnect = false;
		while (s.hasNextLine()) {
			Message msg = Message.fromString(s.nextLine());
			
			if (msg.getType() == Message.MessageType.LOGOFF) {
				cleanDisconnect = true;
			}
			receiver.receiveMessage(msg);
		}
		if(!cleanDisconnect) {
			Message dummy = Message.logoff(socket.getInetAddress().toString());
			receiver.receiveMessage(dummy);
			s.close();
		}
	}

}
