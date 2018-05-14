package servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map.Entry;

public class Servidor implements Runnable {
	private int myCpu, myMemory, myBlock;
	private int porta;
	private ServerSocket servidor;
	private HashMap<String, ClientInfo> Clientes = new HashMap<String, ClientInfo>();

	public Servidor(int porta) {
		this.porta = porta;
		this.myCpu = 100;
		this.myMemory = 100;
		this.myBlock = 100;
	}

	public Servidor(int porta, int cpu, int memory, int block) {
		this.porta = porta;
		this.myCpu = cpu;
		this.myMemory = memory;
		this.myBlock = block;
	}
	
	public void mudar(int cpu, int memory, int block) {
		this.myCpu = cpu;
		this.myMemory = memory;
		this.myBlock = block;
	}
	

	public int getPorta() {
		return porta;
	}

	public void run() {
		try {
			servidor = new ServerSocket(this.porta);
			System.out.println("Porta " + this.porta + " aberta!");

			while (true) {
				ClientInfo clientinfo = new ClientInfo(servidor.accept());

				if (!Clientes.containsKey(clientinfo.getSocket().getInetAddress().getHostAddress())) {
					
					System.out.println(
							"Nova conexão com o cliente " + clientinfo.getSocket().getInetAddress().getHostAddress());

					Clientes.put(clientinfo.getSocket().getInetAddress().getHostAddress(), clientinfo);

					// possivel modificar?
					TrataCliente tc = new TrataCliente(clientinfo, this);
					new Thread(tc).start();
					//
					String msg = myCpu + "|" + myMemory + "|" + myBlock + ">";
					sendMessageToClient(msg, clientinfo.getSocket());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listarClientes() {
		int cpuTotal = 0, memoryTotal = 0, blockTotal = 0;
		for (Entry<String, ClientInfo> entry : Clientes.entrySet()) {
			String key = entry.getKey();
			Socket value = entry.getValue().getSocket();
			System.out.println(key + " " + value);
			cpuTotal += entry.getValue().myCpu;
			memoryTotal += entry.getValue().myMemory;
			blockTotal += entry.getValue().myBlock;
		}
		cpuTotal += myCpu;
		memoryTotal += myMemory;
		blockTotal += myBlock;
		System.out.println("Cpu: " + cpuTotal + " memory: " + memoryTotal + " block: " + blockTotal);
	}

	public void conectar(String ip, int porta) {
		try {
			ClientInfo clientinfo = new ClientInfo(new Socket());
			clientinfo.getSocket().connect(new InetSocketAddress(ip, porta), 10);

			Clientes.put(clientinfo.getSocket().getInetAddress().getHostAddress(), clientinfo);
			sendConnectMessage(clientinfo.getSocket());

			TrataCliente tc = new TrataCliente(clientinfo, this);
			new Thread(tc).start();

			String msg = myCpu + "|" + myMemory + "|" + myBlock + ">";
			sendMessageToClient(msg, clientinfo.getSocket());

			System.out
					.println("Nova conexão com o cliente " + clientinfo.getSocket().getInetAddress().getHostAddress());
		} catch (UnknownHostException e) {
			System.err.println("Nao foi possivel conectar ao outro servidor");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Nao foi possivel conectar ao outro servidor");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMessageToClient(String msg, Socket client) {
		try {
			new PrintStream(client.getOutputStream()).println(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendConnectMessage(Socket client) {
		try {
			new PrintStream(client.getOutputStream()).println(Message.ConnectMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void mandarMensagemDirecionada(String msg, String ip) {
		sendMessageToClient(msg, Clientes.get(ip).getSocket());
	}
	
	public void mandarChatMessage(String msg) {
		// Message message = Message.ChatMessage(msg);
		for (Entry<String, ClientInfo> entry : Clientes.entrySet()) {
			// String key = entry.getKey();
			Socket value = entry.getValue().getSocket();
			sendMessageToClient(msg, value);
		}

	}

	public void receberMesagem(String msg) {
		Message m = Message.fromString(msg);
		switch (m.getType()) {
		case CONNECT:
			break;
		case IP_LIST:
			break;
		case CHAT_MESSAGE:
			break;
		case LOGOFF:
			break;
		default:
			break;
		}
		System.out.println(msg);
	}

	public int soma(int num1, int num2) {
		return num1 + num2;
	}
	
	public void disconnectClient(String ip) {
		Clientes.remove(ip);
		System.out.println(ip + " - se desconectou");
	}
}
