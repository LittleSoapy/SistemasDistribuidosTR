package servidor;

import java.io.IOException;
import java.util.Scanner;

public class TrataCliente implements Runnable{

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
        	servidor.receberMesagem(clientinfo.getSocket().getInetAddress().getHostAddress() + " - " + s.nextLine());
        }
        servidor.disconnectSocket(clientinfo.getSocket());
        s.close();
	}

}
