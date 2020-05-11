package assignment7;

import java.io.IOException;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatBox extends Application{
	
	private int number;
	Stage chatStage;
	private AnchorPane root;
	BoxUIController boxLoader;
	private String userName;

	public ChatBox(int ID, PrintWriter pw, String name) {
		number = ID;
		chatStage = new Stage();
				
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatBoxUI.fxml"));
		AnchorPane anc = null;
		try {
			anc = (AnchorPane) loader.load();
		} catch (Exception e) { e.printStackTrace(); }

		userName = name;
		chatStage = new Stage();
		root = new AnchorPane();
		boxLoader = loader.getController();
		boxLoader.writer = pw;
		boxLoader.number = number;
		boxLoader.name = userName;
		
		Scene scene = new Scene(anc,650,680);
		chatStage.setScene(scene);
		chatStage.setTitle("Group Chat" + "   user: " + userName);
		chatStage.show();
	}


	public void updateChat(String[] message) {
		boxLoader.Chat.setText(boxLoader.Chat.getText() + "\n" + message[1] + ":   " + message[2]);	
	}

	@Override
	public void start(Stage primaryStage) {}


	public String getTitle() {
		return chatStage.getTitle();
	}


	public void setTitle(String title) {
		chatStage.setTitle(title);
	}
}
