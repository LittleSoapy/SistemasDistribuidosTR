import cliente.Menu;
import servidor.Servidor;

public class Main {
	public static void main(String[] args) {
		Servidor servidor = new Servidor(7077);
		new Thread(servidor).start();
		
		Menu menu = new Menu(servidor);
		menu.repl();
	}
}
