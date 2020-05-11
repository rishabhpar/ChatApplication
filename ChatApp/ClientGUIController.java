package assignment7;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientGUIController {
	 	String currentMessage = "";
	    CheckBox[] boxes;
	    String Name = "";
	    PrintWriter writer = null;
	    HashMap<Integer, ChatBox> chatBoxes = new HashMap<Integer, ChatBox>();
	    Socket socket = null;
	    Stage stage = null;
	    String[] users;

	    @FXML
	    public Button SendButton;
	    @FXML
	    public TextArea Chat;
	    @FXML
	    public Button DirectMessage;
	    @FXML
	    public TextField MessageBox;
	    @FXML
	    public Label ChatLabel;
	    @FXML 
	    public VBox Menu;
	    @FXML 
	    public Button Quit;
	    
	    
	    public void setOptions(String[] users) {
	    	this.users = users;
	    	Menu.getChildren().clear();
	    	boxes = new CheckBox[users.length-1];
	    	if (boxes != null && boxes.length > 0) {
		    	int count = 0;
		    	for (int i = 0; i < users.length; i++) {
		    		if(!(users[i].equals(Name))) {
						boxes[count] = new CheckBox(users[i]);
						Menu.getChildren().add(boxes[count]);
						count++;
		    		}
				}
	    	}
	    }
	    
	    public void updateOptions(String name) {
	    	String[] newUserSet = new String[users.length - 1];
	    	int count = 0;
	    	for (int i = 0; i < users.length; i++) {
	    		if(!(users[i].equals(name))) {
					newUserSet[count] = users[i];
					count++;
	    		}
			}
	    	setOptions(newUserSet);
		}
	    
	    @FXML
	    private void QuitHandler() {
	    	for (ChatBox c : chatBoxes.values()) {
	    		c.chatStage.close();
	    	}
	    	
	    	writer.println("quit	" + Name);
	    	writer.flush();
	    	
	    	try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	stage.close();
	    	System.exit(0);
	    }
	    

	    @FXML
	    private void sendButtonHandler(){
	    	String msg = MessageBox.getText();
			if(msg.compareToIgnoreCase("") != 0 && msg != null){
		        currentMessage = MessageBox.getText();

		        URL resource = getClass().getResource("ding.wav");
		    	AudioClip ding = new AudioClip(resource.toString());
		    	ding.play();
		    	
		    	Chat.appendText("\n" + Name + ":	" + currentMessage);
		        MessageBox.setText("");
		        
		        writer.println("global" + "	" + Name + "	" + msg);
				writer.flush();
			} 
	    }

	    @FXML
	    private void DirectMessageHandler() {
	    	String names = "";
			for(int i = 0; i < boxes.length; i++) {
				if (boxes[i].isSelected())
					names += boxes[i].getText() + ",";
			}
			if (names.equals("")) {
				return;
			}
			System.out.println(names);
			writer.println( "new	" + Name + "	" + names);
			writer.flush();
	    }

		
	   
}
