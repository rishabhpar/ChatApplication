package assignment7;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ChatRoom extends Observable{

	public int number;
	public List<String> users = new ArrayList<String>();
	private Server server;
	
	public ChatRoom (Server server) {
		this.server = server;
	}

	public void sendMessage(String msg) {
		setChanged();
		notifyObservers(msg);
	}

	public void setNumber(int i) {
		number = i;
		
	}

	public void addUsers(String[] splitMsg) {
		// add initiator
		if(server.getUserObservers().containsKey(splitMsg[1])) {
			addObserver(server.getUserObservers().get(splitMsg[1]));
			users.add(splitMsg[1]);
		}
		// add invitees
		if(splitMsg.length > 2){
			String[] otherUsers = splitMsg[2].split(",");
			for(int i = 0; i < otherUsers.length; i++) {
				addObserver(server.getUserObservers().get(otherUsers[i]));
				users.add(otherUsers[i]);
			}
		}
	}
}
