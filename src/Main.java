import java.util.Scanner;

import destino.DestinoServer;
import origem.Menu;
import origem.Origem;

public class Main {
	public static void main(String[] args) {
		boolean origem = true;
		if(args.length > 0) {
			origem = Boolean.parseBoolean(args[1]);
		} else {
			Scanner s = new Scanner(System.in);
			origem = s.nextBoolean();	
			s.close();
		}
		if (origem) {
			Origem servidor = new Origem(7077);
			
			Menu menu = new Menu(servidor);
			menu.repl();
		} else {
			DestinoServer servidor = new DestinoServer(7077);
			new Thread(servidor).start();
		}		
	}
}
