package main.java.se.kth.h16p02.npwj.hw1.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * Created by GretarAtli on 03/11/2016.
 */
public class HangmanFXMLClientController implements Initializable{

    private HangmanServerConnection server;

    private static final String PRESS_START_TO_BEGINN = "Please press start to begin the fantastic game of hangman";
    private static final String STARTING_GAME = "Wait!!! We are starting a new game ....";
    private static final String ENTER_CHAR_OR_WORD = "Hei!!! make a guess by entering a character or a word";
    private static final String NUMBER_OF_ATTEMPTS_LEFT = "Number of attempts left: ";
    private static final String GAME_BOARD = "THE HANGMAN GAME BOARD";

    @FXML
    private  Button startButton;

    @FXML
    private  Button guessButton;

    @FXML
    private  TextField guessTextField;

    @FXML
    private Text infoText;

    @FXML
    private  Text nrOfAttemptsLeftText;

    @FXML
    private Text charText;

    @FXML
    private Text gameBoardText;

    @FXML
    private TextArea gameBoardTextArea;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        guessButton.setDisable(true);
        guessTextField.setDisable(true);
        infoText.setText("");
        nrOfAttemptsLeftText.setText("");
        gameBoardText.setText("");
        charText.setText(PRESS_START_TO_BEGINN);
        gameBoardTextArea.setDisable(true);
    }

    @FXML
    private void onStartButtonPress(ActionEvent ae){
        startButton.setDisable(true);
        charText.setText(STARTING_GAME);
        new ConnectService().start();
    }

    @FXML
    private void onGuessButtonPressed(ActionEvent ae){
        guessButton.setDisable(true);
        new GuessService().start();
    }



    private class ConnectService extends Service<HangmanServerConnection> {
        private ConnectService() {
            setOnSucceeded((WorkerStateEvent event) -> {
                server = getValue();
                System.out.println("Connection to the server has been established");
                gameBoardTextArea.setDisable(false);
                charText.setText(ENTER_CHAR_OR_WORD);
                guessTextField.setDisable(false);
                guessButton.setDisable(false);
                gameBoardText.setText(GAME_BOARD);
                nrOfAttemptsLeftText.setText(NUMBER_OF_ATTEMPTS_LEFT + 8);
            });
        }

        @Override
        protected Task<HangmanServerConnection> createTask() {
            return new Task<HangmanServerConnection>() {
                @Override
                protected HangmanServerConnection call() throws Exception{
                    Thread.sleep(5000);
                    return new HangmanServerConnection("localhost",
                            Integer.parseInt("4444"));
                }
            };
        }
    }

    // Use the concurrent service of javaFX
    private class GuessService extends Service<String>{

        private GuessService() {
            setOnSucceeded((WorkerStateEvent event) -> {
                gameBoardTextArea.setText(getValue());
                guessButton.setDisable(false);
                System.out.println(getValue());

            });

            setOnFailed((WorkerStateEvent event) -> {
                System.out.println(getException().getMessage());
            });
        }

        @Override
        protected Task<String> createTask(){
            return new Task<String>() {
                @Override
                protected String call() throws Exception {
                    System.out.println("calling the server with string = " + guessTextField.getText());
                    return server.callServer(guessTextField.getText());
                }
            };
        }
    }
}
