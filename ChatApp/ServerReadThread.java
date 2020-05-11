package assignment7;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerReadThread implements Runnable {
	private Socket socket;
	private List<ChatRoom> allChats;
	private Server server;
	private BufferedReader reader;
	private ClientObserver writer;
	Map<String, ClientObserver> users;

	public ServerReadThread(Socket clientSocket, ClientObserver writer, List<ChatRoom> allChats, Server server, Map<String, ClientObserver> userObservers) {
		this.socket = clientSocket;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		this.server = server;
		this.users = userObservers;
		this.writer = writer;
		this.allChats = allChats;
	}

	@Override
	public void run() {
		String msg = new String();
		try {
			while((msg = reader.readLine()) != null) {
				String[] splitMsg = msg.split("	");
				try {
					allChats.get(Integer.parseInt(splitMsg[0])).sendMessage(msg);
				} catch (Exception e) {
					if (splitMsg[0].equalsIgnoreCase("global")) {
						Collection<ClientObserver> allusers = users.values();
						for (ClientObserver writer : allusers) {
							if (writer != this.writer) {
								writer.println(msg);
								writer.flush();
							}
						}
						
					} else if (splitMsg[0].equalsIgnoreCase("close")) {
						for(ChatRoom r : allChats) {
							if (r.number == Integer.parseInt(splitMsg[1])) {
								r.sendMessage(Integer.toString(r.number) + "	Server	Close window");
								r.users.clear();
								r.setNumber(-1);
							} 
						}
					} else if (splitMsg[0].equalsIgnoreCase("new")) {
						String[] clients = splitMsg[2].split(",");
						int lengthOfClients = clients.length+1;
						String[] usernames = new String[lengthOfClients];
						// add members of the chat room
						usernames[0] = splitMsg[1];
						
						for(int i = 0; i< lengthOfClients-1; i++) {
							usernames[i+1] = clients[i];
						}
						// check if such a chat room exists
						boolean chatExists = false;
						
						for (ChatRoom r : allChats) {
							if(r.users.containsAll(Arrays.asList(usernames)) && r.users.size() == usernames.length) {
								chatExists = true;
								break;
							}
						}
						
						if (!chatExists) {
							ChatRoom chat = new ChatRoom(server);
							allChats.add(chat);
							chat.setNumber(allChats.indexOf(chat));
							chat.addUsers(splitMsg);
							chat.sendMessage(chat.number + "	Server	Members: " + Arrays.toString(usernames));
						}

					} else if (splitMsg[0].equalsIgnoreCase("enter")) {
						String name = splitMsg[1];
						users.put(name, this.writer);
						server.addedUsers = true;
					} else if (splitMsg[0].equalsIgnoreCase("start")) {
						if (server.addedUsers) {
							Set<String> usernames = users.keySet();
							server.addedUsers = false;
							String names = "";
							for (String n : usernames) {
								names += n + ",";
							}
							ChatRoom chat = new ChatRoom(server);
							names = "enter	" + splitMsg[1] + "	" + names;
							String[] userLogin = names.split("	");
							chat.addUsers(userLogin);
							chat.sendMessage(names);
						}
					} else if(splitMsg[0].equals("quit")) {
						for(ChatRoom r: allChats) {
							if(r.users.contains(splitMsg[1])) {
								r.sendMessage(r.number + "	SERVER	CHAT ENDED.");
								r.setNumber(-1);
								r.users.clear();
								break;
							}
						}
						for (String n : users.keySet()) {
							if (!n.equals(splitMsg[1])) {
								users.get(n).println("quit	" + splitMsg[1]);
								users.get(n).flush();
							}
						}
						server.getUserObservers().remove(splitMsg[1]);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
