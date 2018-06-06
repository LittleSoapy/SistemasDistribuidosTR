package utility;

import java.util.List;

public class Message{
	public enum MessageType {
		CONNECT,
		CONNECT_OK,
		SOMA,
		SUBTRACAO,
		MULTIPLICACAO,
		DIVISAO,
		RESULT,
		LOGOFF,
		UNIDENTIFIED;
		public String toString() {
			String s;
			switch(this) {
			case CONNECT: s = "CONNECT"; break;
			case CONNECT_OK: s = "CONNECT_OK"; break;
			case SOMA: s = "+"; break;
			case SUBTRACAO: s = "-"; break;
			case MULTIPLICACAO: s = "*"; break;
			case DIVISAO: s = "/"; break;
			case RESULT: s = "r"; break;
			case LOGOFF: s = "LOGOFF|"; break;
			default: s = "UNIDENTIFIED";
			}
			return s + "|";
		}
	}
	
	private MessageType type;
	public String content_a;
	public String content_b;
	
	public MessageType getType() { return type; }
	
	private Message(MessageType type) {
		this.type = type;		
	}
	
	public static Message connect() {		
		return new Message(MessageType.CONNECT);
	}
	
	public static Message connectOK(String ip) {		
		var m = new Message(MessageType.CONNECT_OK);
		m.content_a = ip;
		return m;
	}
	
	public static Message logoff(String ip) {
		var m = new Message(MessageType.LOGOFF);
		m.content_a = ip;
		return m;
	}
	
	public static Message result(int value) {
		var m = new Message(MessageType.RESULT);
		m.content_a = ""+value;
		return m;
	}
	
	public String toString() {
		String s = this.type.toString();
		if (this.content_a != null) {
			s += this.content_a + "|";
		}
		if (this.content_b != null) {
			s += this.content_b + "|";			
		}
		return s; 		
	}
	
	public static Message fromString(String s) {
		String[] parts = s.split("|");
		MessageType t = null;
		switch(parts[0]) {
		case "CONNECT": 
			return Message.connect();
		case "CONNECT_OK": 
			return Message.connectOK(parts[1]);
		case "LOGOFF": 
			return Message.logoff(parts[1]);
			
		case "r":
			var r = new Message(MessageType.RESULT);
			r.content_a = parts[1];
			return r;
			
		case "+": 
			t = MessageType.SOMA;
			break;
		case "-": 
			t = MessageType.SUBTRACAO;
			break;
		case "*": 
			t = MessageType.MULTIPLICACAO;
			break;			
		case "/": 
			t = MessageType.DIVISAO;
			break;
			
		default: return new Message(MessageType.UNIDENTIFIED);
		}
		var m = new Message(t);
		m.content_a = parts[1];
		m.content_b = parts[2];
		return m;
	}
}
