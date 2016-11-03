package main.java.se.kth.h16p02.npwj.hw1.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Created by GretarAtli on 03/11/2016.
 */
public class HangmanFXMLClientController implements Initializable{

    private static final String PRESS_START_TOBEGINN = "Please press start to begin the fantastic game of hangman";

    @FXML
    private  Button startButton;

    @FXML
    private  Button guessButton;

    @FXML
    private  TextField charTextField;

    @FXML
    private  Text nrOfAttemptsLeftText;

    @FXML
    private Text charText;

    @FXML
    private TextArea gameTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        guessButton.setDisable(true);
        charTextField.setDisable(true);
        nrOfAttemptsLeftText.setText("");
        charText.setText(PRESS_START_TOBEGINN);
        gameTextArea.setDisable(true);
    }

    @FXML
    private void onStartButtonPress(ActionEvent ae){
        startButton.setDisable(true);
    }

    @FXML
    private void onGuessButtonPressed(ActionEvent ae){
        guessButton.setDisable(true);
    }
}
