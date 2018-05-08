package cliente;

import java.util.Scanner;

import servidor.Servidor;

public class Menu {
	Servidor servidor;
 
	public Menu(Servidor servidor) {
		this.servidor = servidor;
	}

	public void repl() {
		Scanner teclado = new Scanner(System.in);
		Boolean sair = false;
		System.out.println("escreva algo ou use o comando '/help para mais informacoes'");
		while (!sair) {
			String msg = teclado.nextLine();
			String[] msg_parts = msg.split(" ");
			if (msg.charAt(0) == '/') {
				switch (msg_parts[0]) {
				case "/conectar":
					handle_connect(msg_parts, teclado);
					break;
				case "/sair":
					sair = true;
					break;
				case "/re":
					re();
					break;
				case "/help":
					System.out.println("comando '/conectar' para conectar a outro servidor");
					System.out.println("comando '/sair' para fechar o programa");
					break;
				default:
					System.out.println("comando desconhecido, use '/help'");
					break;
				}
			} else {
				mandarMensagem("" + msg);
			}

		}
		teclado.close();
	}

	private void handle_connect(String[] args, Scanner teclado) {
		String ip = null;
		int port = 7077;
		if (args.length == 0) {
			System.out.println("Escrever IP de outro Servidor:");
			ip = teclado.nextLine();
		} else if (args.length == 1) {
			ip = args[1];
		} else if (args.length == 2) {
			ip = args[1];
			port = Integer.parseInt(args[2]);
		} else {
			System.out.println("--Número incompatível de argumentos");
			return;
		}
		conectar(ip, port);
		teclado.nextLine();
	}

	public void conectar(String ip, int porta) {
		servidor.conectar(ip, porta);
		
	}
	
	public void re() {
		servidor.re();
		
	}
	

	public void mandarMensagem(String msg) {
		servidor.mandarChatMessage(msg);
	}
}
