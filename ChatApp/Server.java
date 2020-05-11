package assignment7;

import java.io.*;
import java.net.*;
import java.util.*;



public class Server {
	
	private static List<ChatRoom> allChats = new ArrayList<>();
	private static Map<String, ClientObserver> userObservers = new HashMap<String, ClientObserver>();
	boolean addedUsers = false;
	private ServerSocket serverSocket;
	private final static int port = 8989;

	public static void main(String[] args) {
		new Server().go();
	}
    public Map<String, ClientObserver> getUserObservers() {
        return userObservers;
    }

    private void go() {
		try {
			serverSocket = new ServerSocket(port);
			do {
				Socket clientSocket = serverSocket.accept();
				ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
				Thread readThread = new Thread(new ServerReadThread(clientSocket, writer, allChats, this, getUserObservers()));
				readThread.start(); 
			}while(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
