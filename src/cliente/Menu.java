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
			if (msg.charAt(0) == '/') {
				switch (msg) {
				case "/conectar":
					System.out.println("Escrever IP de outro Servidor:");
					String ip = teclado.nextLine();
					conectar(ip, 7077);
					teclado.nextLine();
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
