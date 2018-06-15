package destino;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.IntBinaryOperator;

import utility.ConnectionHandler;
import utility.Message;
import utility.MessageReceiver;

public class DestinoServer implements Runnable, MessageReceiver {
	private int porta;
	private ServerSocket servidor;
	private Socket origem;
	public boolean running = true;
	public DestinoServer(int porta) {
		this.porta = porta;
	}

	public int getPorta() {
		return porta;
	}

	public void run() {
		try {
			servidor = new ServerSocket(this.porta);
			System.out.println("Porta " + this.porta + " aberta!");

			while (this.running) {
				if(origem == null) {
					System.out.println("Esperando conexão...");
					Socket socket = servidor.accept();
					System.out.println("Nova conexão com " + socket.getInetAddress());
					ConnectionHandler handler = new ConnectionHandler(socket, this);
					new Thread(handler).start();
					origem = socket;					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void sendMessage(Message msg) {
		System.out.println("Retornando: " + msg);
		try {
			new PrintStream(origem.getOutputStream()).println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receiveMessage(Message msg) {
		System.out.println("Mensagem recebida: " + msg);
		switch(msg.getType()) {
		case CONNECT:
			sendMessage(Message.connectOK(servidor.getInetAddress().toString()));
			break;
		case LOGOFF:
			origem = null;
			break;
		case SOMA:
			calcAndSend(msg.content_a, msg.content_b, (x,y)-> x + y);
			break;
		case SUBTRACAO:
			calcAndSend(msg.content_a, msg.content_b, (x,y)-> x - y);
			break;
		case MULTIPLICACAO:
			calcAndSend(msg.content_a, msg.content_b, (x,y)-> x * y);
			break;
		case DIVISAO:
			calcAndSend(msg.content_a, msg.content_b, (x,y)-> x / y);
			break;
		default:
			System.err.println("Mensagem inesperada:" + msg);
			break;
		}
		
	}
	private void calcAndSend(String argA, String argB, IntBinaryOperator op) {
		int result = op.applyAsInt(Integer.getInteger(argA), Integer.getInteger(argB));
		sendMessage(Message.result(result));
	}
	
	public int soma(int num1, int num2) {
		return num1 + num2;
	}
}
