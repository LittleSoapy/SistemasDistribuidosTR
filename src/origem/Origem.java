package origem;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import utility.ConnectionHandler;
import utility.Message;
import utility.MessageReceiver;

public class Origem implements MessageReceiver {
	private int porta;
	private List<Socket> connections = new ArrayList<Socket>(2);

	public Origem(int porta) {
		this.porta = porta;
	}	

	public int getPorta() {
		return porta;
	}
	
	public void connect(String ip) {
		connect(ip, this.porta);
	}
	
	public void connect(String ip, int port) {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(ip, porta), 10);
			
			connections.add(socket);
			
			sendConnectMessage(socket);

			ConnectionHandler handler = new ConnectionHandler(socket, this);
			new Thread(handler).start();
			
		} catch (UnknownHostException e) {
			System.err.println("Nao foi possivel conectar ao outro servidor");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Nao foi possivel conectar ao outro servidor");
			e.printStackTrace();
		} 	
	}

	private void sendMessageToClient(Message msg, Socket dest) {
		try {
			new PrintStream(dest.getOutputStream()).println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendConnectMessage(Socket dest) {
		sendMessageToClient(Message.connect(), dest);
	}

	public void sendMessage(String msg, String index) {
		Socket dest = connections.get(Integer.parseInt(index));
		sendMessageToClient(Message.fromString(msg), dest);
	}
	
	public void receiveMessage(Message msg) {
		switch (msg.getType()) {
		case CONNECT_OK:
			break;
		case LOGOFF:
			disconnectClient(msg.content_a);
			break;
		case RESULT:
			handleResult(msg.content_a);
			break;
		case UNIDENTIFIED:
			break;
		default:
			System.err.println("Mensagem inesperada:" + msg);
			break;
		}		
	}

	private void handleResult(String result) {
		System.out.println("Operation result:" + result);
	}
	
	public void disconnect() {
		for (Socket s : connections) {
			sendMessageToClient(Message.logoff("~"), s);
		}
	}
	
	public void disconnectClient(String ip) {
		for (Socket s : connections) {
			if (s.getInetAddress().toString() == ip) {
				sendMessageToClient(Message.logoff("-"), s);
				connections.remove(s);
			}
		}
		System.out.println(ip + " - se desconectou");
	}
	
	public String[] getConnected() {
		String[] s = new String[connections.size()];
		for (int i = 0; i < s.length; i++) {
			s[i] = connections.get(i).getInetAddress().toString();
		}
		return s;
	}
}
