package servidor;

import java.util.List;

public class Message {
	public enum MessageType {
		CONNECT,
		IP_LIST,
		CHAT_MESSAGE,
		LOGOFF,
		UNIDENTIFIED;
		public String toString() {
			String s;
			switch(this) {
			case CONNECT: s = "CONNECT|"; break;
			case IP_LIST: s = "IPLIST|"; break;
			case CHAT_MESSAGE: s = "CHAT|"; break;
			case LOGOFF: s = "LOGOFF|"; break;
			default: s = "UNIDENTIFIED";
			}
			return s;
		}
	}
	
	private MessageType type;
	private String content;
	
	public MessageType getType() { return type; }
	
	private Message(MessageType type) {
		this.type = type;		
	}
	
	public static Message ConnectMessage() {		
		return new Message(MessageType.CONNECT);
	}
	
	public static Message IpList(List<String> ips) {
		Message message = new Message(MessageType.IP_LIST);
		message.content = String.join(";", ips);		
		return message;
	}
	
	public static Message ChatMessage(String msg) {
		Message message = new Message(MessageType.CHAT_MESSAGE);
		message.content = msg;
		return message;
	}
	
	public static Message LogoffMessage() {
		return new Message(MessageType.LOGOFF);
	}
	
	public String toString() {
		String s = this.type.toString();
		if (this.content != null) {
			s += content;
		}
		return s; 		
	}
	
	public static Message fromString(String s) {
		String[] parts = s.split("|");
		switch(parts[0]) {
		case "CONNECT": return new Message(MessageType.CONNECT);
		case "IP_LIST": 
			Message ip_m = new Message(MessageType.IP_LIST);
			ip_m.content = parts[1];
			return ip_m;
		case "CHAT_MESSAGE": 
			Message chat_m =  new Message(MessageType.CHAT_MESSAGE);
			chat_m.content = parts[1];
			return chat_m;
		case "LOGOFF": return new Message(MessageType.LOGOFF);
		default: return new Message(MessageType.UNIDENTIFIED);
		}
		
	}
}
