package assignment7;

import java.io.PrintWriter;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

public class BoxUIController {
	
	PrintWriter writer;
	String name;
	int number;
	
	
	@FXML
    public Button SendButton;
    @FXML
    public TextArea Chat;
    @FXML
    public TextField MessageBox;
    @FXML
    public Button Quit;
    
    @FXML
    private void QuitHandler() {
    	writer.println("close	" + number);
		writer.flush();
    }

    
    @FXML
    private void SendHandle() {
    	String s = MessageBox.getText();
		if (s != null) {
			URL resource = getClass().getResource("ding.wav");
	    	AudioClip ding = new AudioClip(resource.toString());
	    	ding.play();
		    	
			writer.println(number + "	" + name + "	" + s);
			writer.flush();

			MessageBox.clear();
		}
    }

}
