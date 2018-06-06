package origem;

import java.util.Scanner;

public class Menu {
	Origem origem;

	public Menu(Origem servidor) {
		this.origem = servidor;
	}

	public void repl() {
		System.out.println("escreva algo ou use o comando '/help para mais informacoes'");
		Scanner teclado = new Scanner(System.in);
		while (true) {
			String msg = teclado.nextLine();
			String[] msg_parts = msg.split(" ");
			switch(msg.charAt(0)) {
			case '[':
				handleSlash(msg_parts, teclado);
				break;
			case '+':
				handleOp(msg_parts, teclado, "+");
				break;
			case '-':
				handleOp(msg_parts, teclado, "-");
				break;
			case '*':
				handleOp(msg_parts, teclado, "*");
				break;
			case '/':
				handleOp(msg_parts, teclado, "/");
			}
		}
	}

	private void handleSlash(String[] msg_parts, Scanner teclado) {
		switch (msg_parts[0]) {
		case "[conectar":
			handle_connect(msg_parts, teclado);
			break;
		case "[listar":
			listarClientes();
			break;
		case "[op":
			sendOp(msg_parts, teclado);
			break;
		case "[somar":
			handleOp(msg_parts, teclado, "+");
			break;
		case "[subtrair":
			handleOp(msg_parts, teclado, "-");
			break;
		case "[multiplicar":
			handleOp(msg_parts, teclado, "*");
			break;
		case "[dividir":
			handleOp(msg_parts, teclado, "/");
			break;
		case "[desconectar":
			desconectar(msg_parts, teclado);
			break;
		case "[sair":
			System.exit(0);
			break;
		case "[help":
			System.out.println("comando '[conectar' 'ip' 'porta' para conectar a outro servidor");
			System.out.println("comando '[listar' para mostrar outros clientes conectados e o total de recursos disponiveis");
			System.out.println("comando '+' 'num1' 'num2' para somar os numeros no cliente indicado pelo ip");
			System.out.println("comando '-' 'num1' 'num2' para subtrair os numeros no cliente indicado pelo ip");
			System.out.println("comando '*' 'num1' 'num2' para multiplicar os numeros no cliente indicado pelo ip");
			System.out.println("comando '/' 'num1' 'num2' para dividir os numeros no cliente indicado pelo ip");
			System.out.println("comando '[desconectar' para desconectar com 1 ou varios clientes");
			System.out.println("comando '[sair' para fechar o programa");
			break;
		default:
			System.out.println("comando desconhecido, use '/help'");
			break;
		}
	}
	
	private void handleOp(String[] args, Scanner teclado, String op_code) {
		String num1, num2;
		if (args.length == 1) {
			System.out.println("Escrever primeiro numero:");
			num1 = teclado.nextLine();
			System.out.println("Escrever segundo numero:");
			num2 = teclado.nextLine();

		} else if (args.length == 3) {
			num1 = args[1];
			num2 = args[2];
		} else {
			System.err.println("Numero incompativel de argumentos");
			return;
		}
		System.out.println("Escolha um índice para mandar a operação:");
		listarClientes();
		var index = teclado.nextLine();
		System.out.println("Enviando");
		var msg = op_code + "|" + num1 + "|" + num2;
		origem.sendMessage(msg, index);;
	}

	private void sendOp(String[] msg_parts, Scanner teclado) {
		String msg = "";
		String dest_index = "";
		if(msg_parts.length == 4) {
			dest_index = msg_parts[4];
		}
		if(msg_parts.length >= 3) {
			msg += msg_parts[1] + "|" + msg_parts[2] + "|" + msg_parts[3];
		}
		
		origem.sendMessage(msg, origem.getConnected()[Integer.parseInt(dest_index)]);
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
		origem.connect(ip, porta);
	}

	public void listarClientes() {
		var connected = origem.getConnected();
		for (int i = 0; i < connected.length; i++) {
			System.out.println(i + " - " + connected[i]);			
		}
	}

	public void desconectar(String[] args, Scanner teclado) {
		String ip = null;
		if (args.length == 1) {
			System.out.println("Escrever IP de outro Servidor:");
			ip = teclado.nextLine();
			origem.disconnectClient(ip);
		} else if (args.length == 2) {
			if(args[1].equals("todos")) {
				origem.disconnect();
			} else {
				ip = args[1];
				origem.disconnectClient(ip);
			}
		} else {
			System.err.println("Numero incompativel de argumentos");
			return;
		}
	}
	
}
