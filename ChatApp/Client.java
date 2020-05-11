package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Client extends Application{
	
	private String username;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private HashMap<Integer, ChatBox> chatBoxes = new HashMap<Integer, ChatBox>();
	private Stage primaryStage;
	private ClientGUIController clientLoader;
	private Thread userThread;
	private final static int port = 8989;


	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		
		AnchorPane root = new AnchorPane();
		root.setPrefHeight(376);
		root.setPrefWidth(448);
		
		Text ChatRoom = new Text();
		ChatRoom.setFill(Color.DARKORANGE);
		ChatRoom.setLayoutX(213);
		ChatRoom.setLayoutY(51);
		ChatRoom.setText("Chat Room");
		Font font = new Font("Corbel", 49);
		ChatRoom.setFont(font);
		
		Text user = new Text();
		user.setLayoutX(293);
		user.setLayoutY(116);
		Font font2 = new Font("Corbel", 15);
		user.setFont(font2);
		user.setText("<- Username");
		
		Text addy = new Text();
		addy.setLayoutX(51);
		addy.setLayoutY(229);
		Font font3 = new Font("Corbel", 15);
		addy.setFont(font3);
		addy.setText("Enter IP Address (ex: 127.0.0.1) if you are not using localhost");
		
		TextField UserNameText = new TextField();
		UserNameText.setLayoutX(31);
		UserNameText.setLayoutY(97);
		UserNameText.setPrefHeight(32);
		UserNameText.setPrefWidth(228);
		
		TextField AddressBox = new TextField();
		AddressBox.setLayoutX(108);
		AddressBox.setLayoutY(270);
		AddressBox.setPrefHeight(26);
		AddressBox.setPrefWidth(209);
		
		Button Enter = new Button();
		Enter.setLayoutX(180);
		Enter.setLayoutY(320);
		Font font4 = new Font("Corbel", 15);
		Enter.setFont(font4);
		Enter.setText("Enter");
		
		root.getChildren().add(ChatRoom);
		root.getChildren().add(user);
		root.getChildren().add(addy);
		root.getChildren().add(UserNameText);
		root.getChildren().add(AddressBox);
		root.getChildren().add(Enter);

		Stage stage1 = new Stage();
		stage1.setScene(new Scene(root));
		stage1.show();
		
		Enter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(AddressBox.getText() == null || AddressBox.getText().equals("")) {
		    		connectToServer("localhost", UserNameText.getText());
		    	} else if (!AddressBox.getText().equals("")) {
		    		connectToServer(AddressBox.getText(), UserNameText.getText());
		    	}
		    	stage1.close();
			}
		});
	}
	
	public void connectToServer(String ip, String name) {
		try {
			socket = new Socket(ip, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		userThread = new Thread(new ClientReadThread(reader, chatBoxes, clientLoader, writer, name, socket, primaryStage)); 
		userThread.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!name.equals("")) {
			username = name;
			writer.println("enter	" + username);
			writer.flush();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			writer.println("start	" + username);
			writer.flush();
		}
	} 
	
}
