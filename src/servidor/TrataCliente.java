package servidor;

import java.io.IOException;
import java.util.Scanner;

public class TrataCliente implements Runnable {

	private ClientInfo clientinfo;
	private Servidor servidor;
	private Scanner s;

	public TrataCliente(ClientInfo clientinfo, Servidor servidor) {
		this.clientinfo = clientinfo;
		this.servidor = servidor;

		try {
			this.s = new Scanner(this.clientinfo.getSocket().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (s.hasNextLine()) {
			String msg = s.nextLine();

			if (msg.charAt(msg.length() - 1) == '>') {
				try {
					String[] info = msg.split("[| >]");
					clientinfo.myCpu = Integer.parseInt(info[0]);
					clientinfo.myMemory = Integer.parseInt(info[1]);
					clientinfo.myBlock = Integer.parseInt(info[2]);
				} catch (Exception e) {
					System.err.println("Erro em gravar dados do cliente");
					System.err.println(e);
				}
			}

			if (msg.charAt(msg.length() - 1) == '+') {
				try {
					String[] info = msg.split("[| +]");
					int result = servidor.soma(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
					servidor.mandarMensagemDirecionada(Integer.toString(result), clientinfo.getSocket().getInetAddress().getHostAddress());
				} catch (Exception e) {
					System.err.println("Erro na requisicao do servico de soma");
				}
			}
			
			if (msg.equals("disconnect!")) {
				try {
					servidor.mandarMensagemDirecionada("disconnect!", clientinfo.getSocket().getInetAddress().getHostAddress());
					break;
				} catch (Exception e) {
					System.err.println("Erro na requisicao do servico de soma");
				}
			}

			servidor.receberMesagem(clientinfo.getSocket().getInetAddress().getHostAddress() + " - " + msg);
		}
		servidor.disconnectClient(clientinfo.getSocket().getInetAddress().getHostAddress());
		s.close();
	}

}
