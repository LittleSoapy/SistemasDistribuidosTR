package cliente;

import java.util.Scanner;

import servidor.Servidor;

public class Menu {
	Servidor servidor;

	public Menu(Servidor servidor) {
		this.servidor = servidor;
	}

	public void repl() {
		System.out.println("escreva algo ou use o comando '/help para mais informacoes'");
		while (true) {
			Scanner teclado = new Scanner(System.in);
			String msg = teclado.nextLine();
			String[] msg_parts = msg.split(" ");
			if (msg.charAt(0) == '/') {
				switch (msg_parts[0]) {
				case "/conectar":
					handle_connect(msg_parts, teclado);
					break;
				case "/listar":
					listarClientes();
					break;
				case "/somar":
					soma(msg_parts, teclado);
					break;
				case "/mudar":
					mudar(msg_parts, teclado);
					break;
				case "/desconectar":
					desconectar(msg_parts, teclado);
					break;
				case "/sair":
					System.exit(0);
					break;
				case "/help":
					System.out.println("comando '/conectar' 'ip' 'porta' para conectar a outro servidor");
					System.out.println("comando '/listar' para mostrar outros clientes conectados e o total de recursos disponiveis");
					System.out.println("comando '/somar' 'ip' 'num1|num2|+' para somar os numeros no cliente indicado pelo ip");
					System.out.println("comando '/mudar' 'cpu' 'memoria' 'block' para mudar as variaveis localmente");
					System.out.println("comando '/desconectar' para desconectar com 1 ou varios clientes");
					System.out.println("comando '/sair' para fechar o programa");
					break;
				default:
					System.out.println("comando desconhecido, use '/help'");
					break;
				}
			} else {
				mandarMensagemGlobal(msg + "");
			}
		}
	}

	private void handle_connect(String[] args, Scanner teclado) {
		String ip = null;
		int port = 7077;
		if (args.length == 1) {
			System.out.println("Escrever IP de outro Servidor:");
			ip = teclado.nextLine();
		} else if (args.length == 2) {
			ip = args[1];
		} else if (args.length == 3) {
			ip = args[1];
			port = Integer.parseInt(args[2]);
		} else {
			System.err.println("Numero incompativel de argumentos");
			return;
		}
		conectar(ip, port);
	}

	public void conectar(String ip, int porta) {
		servidor.conectar(ip, porta);
	}

	public void listarClientes() {
		servidor.listarClientes();
	}

	private void soma(String[] args, Scanner teclado) {
		String ip = null;
		String msg = null;
		int num1, num2;
		if (args.length == 1) {
			System.out.println("Escrever IP de outro Servidor:");
			ip = teclado.nextLine();

			System.out.println("Escrever primeiro numero:");
			num1 = teclado.nextInt();
			System.out.println("Escrever segundo numero:");
			num2 = teclado.nextInt();

			msg = num1 + "|" + num2 + "|+";
		} else if (args.length == 2) {
			ip = args[1];

			System.out.println("Escrever primeiro numero:");
			num1 = teclado.nextInt();
			System.out.println("Escrever segundo numero:");
			num2 = teclado.nextInt();

			msg = num1 + "|" + num2 + "|+";
		} else if (args.length == 3) {
			ip = args[1];
			msg = args[2];
		} else {
			System.err.println("Numero incompativel de argumentos");
			return;
		}

		servidor.mandarMensagemDirecionada(msg, ip);
	}

	private void mudar(String[] args, Scanner teclado) {
		int cpu, memory, block;
		if (args.length == 1) {
			System.out.println("Escrever novo valor para CPU:");
			cpu = teclado.nextInt();
			System.out.println("Escrever novo valor para Memoria:");
			memory = teclado.nextInt();
			System.out.println("Escrever novo valor para Block:");
			block = teclado.nextInt();
		} else if (args.length == 2) {
			cpu = Integer.parseInt(args[1]);
			System.out.println("Escrever novo valor para Memoria:");
			memory = teclado.nextInt();
			System.out.println("Escrever novo valor para Block:");
			block = teclado.nextInt();
		} else if (args.length == 3) {
			cpu = Integer.parseInt(args[1]);
			memory = Integer.parseInt(args[2]);
			System.out.println("Escrever novo valor para Block:");
			block = teclado.nextInt();
		} else if (args.length == 4) {
			cpu = Integer.parseInt(args[1]);
			memory = Integer.parseInt(args[2]);
			block = Integer.parseInt(args[3]);
		} else {
			System.err.println("Numero incompativel de argumentos");
			return;
		}
		servidor.mudar(cpu, memory, block);
		mandarMensagemGlobal(cpu + "|" + memory + "|" + block + ">");
	}
	
	public void desconectar(String[] args, Scanner teclado) {
		String ip = null;
		if (args.length == 1) {
			System.out.println("Escrever IP de outro Servidor:");
			ip = teclado.nextLine();
			mandarMensagemDirecionada("disconnect!", ip);
		} else if (args.length == 2) {
			if(args[1].equals("todos")) {
				mandarMensagemGlobal("disconnect!");
			} else {
				ip = args[1];
				mandarMensagemDirecionada("disconnect!", ip);
			}
			
		} else {
			System.err.println("Numero incompativel de argumentos");
			return;
		}
		
	}

	public void mandarMensagemGlobal(String msg) {
		servidor.mandarChatMessage(msg);
	}
	public void mandarMensagemDirecionada(String msg, String ip) {
		servidor.mandarMensagemDirecionada(msg, ip);
	}
}
