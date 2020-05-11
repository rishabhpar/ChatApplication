package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClientReadThread implements Runnable {
	
	private BufferedReader reader;
	private HashMap<Integer, ChatBox> chatBoxes;
	private ClientGUIController clientLoader;
	private PrintWriter writer;
	private String username;
	private Socket socket;
	private Stage primaryStage;
	
	
	public ClientReadThread(BufferedReader reader, HashMap<Integer, ChatBox> chatBoxes,
			ClientGUIController clientLoader, PrintWriter writer, String username, Socket socket, Stage primaryStage) {
		this.reader = reader;
		this.chatBoxes = chatBoxes;
		this.clientLoader = clientLoader;
		this.writer = writer;
		this.username = username;
		this.socket = socket;
		this.primaryStage = primaryStage;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		String user;
		try {
			while ((user = reader.readLine()) != null) {
				String[] action = user.split("	");
				
				try {
					int id = Integer.parseInt(action[0]);
					
					Platform.runLater(() -> {
						if (chatBoxes.containsKey(id)) {
							if(action[1].equals("SERVER") && action[2].equals("New chat created."))
								startChat(action);
							else
								chatBoxes.get(id).updateChat(action);
						}
						else {
							startChat(action);
						}	
					});
					
				} catch (Exception e) {
					if (action[0].equals("global")){
						clientLoader.Chat.appendText("\n" + action[1] + ":	" + action[2]);
					} else if (action[0].equals("start") || action[0].equals("enter")) {
						Platform.runLater(() -> {
							homeScreen(action[2].split(","));
						});
					} else if (action[0].equals("quit")) {
						Platform.runLater(() -> {
							clientLoader.updateOptions(action[1]);
						});
					}
				}

			}
		} catch (IOException ignored) {}
	}
	
	public void homeScreen(String[] activeUsers) {
		String globalChat = null;
		if (clientLoader != null) {
			globalChat = clientLoader.Chat.getText();
		}
		
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientGUI.fxml"));
		AnchorPane anc = null;
		try {
			anc = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientLoader = (ClientGUIController) loader.getController();
		clientLoader.Name = username;
		clientLoader.writer = writer;
		clientLoader.chatBoxes = chatBoxes;
		clientLoader.socket = socket;
		clientLoader.stage = primaryStage;
		clientLoader.setOptions(activeUsers);
		if (globalChat != null) {
			clientLoader.Chat.setText(globalChat);
		}
		
		Scene scene2 = new Scene(anc,675,675);
		primaryStage.setScene(scene2);
		primaryStage.setTitle("Chat: " + username);
		primaryStage.show();
	}
	
	public void startChat(String[] message) {
		int ID = Integer.parseInt(message[0]);
		ChatBox newChat = new ChatBox(ID, writer, username);
		if(chatBoxes.containsKey(ID)) {
			newChat.setTitle(chatBoxes.get(ID).getTitle());
			chatBoxes.get(ID).boxLoader.writer.println("close	" + ID);
			chatBoxes.get(ID).boxLoader.writer.flush();
		}
		newChat.updateChat(message);
		chatBoxes.put(ID, newChat);
	}
	

}