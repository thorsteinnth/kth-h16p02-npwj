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
import javafx.scene.text.Text;

/**
 * Created by GretarAtli on 03/11/2016.
 */
public class HangmanFXMLClientController implements Initializable{

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
    private  TextField charTextField;

    @FXML
    private  Text nrOfAttemptsLeftText;

    @FXML
    private Text charText;

    @FXML
    private Text gameBoardText;

    @FXML
    private TextArea gameTextArea;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        guessButton.setDisable(true);
        charTextField.setDisable(true);
        nrOfAttemptsLeftText.setText("");
        gameBoardText.setText("");
        charText.setText(PRESS_START_TO_BEGINN);
        gameTextArea.setDisable(true);
    }

    @FXML
    private void onStartButtonPress(ActionEvent ae){
        startButton.setDisable(true);
        charText.setText(STARTING_GAME);
        new HangmanConcurrentService(
                charTextField,
                nrOfAttemptsLeftText,
                charText,
                gameBoardText,
                gameTextArea,
                guessButton).start();
    }

    @FXML
    private void onGuessButtonPressed(ActionEvent ae){
        guessButton.setDisable(true);
    }

    // Use the concurrent service of javaFX

    private static class HangmanConcurrentService extends Service<String>{

        private HangmanConcurrentService(TextField charTextField,
                                         Text nrOfAttemptsLeftText,
                                         Text charText,
                                         Text gameBoardText,
                                         TextArea gameTextArea,
                                         Button guessButton) {
            setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent e){
                    //DO some work after the service call
                    gameTextArea.setText("Here we need to add some gameboard");
                    charTextField.setDisable(false);
                    guessButton.setDisable(false);
                    charText.setText((String) e.getSource().getValue());
                    gameBoardText.setText(GAME_BOARD);
                    nrOfAttemptsLeftText.setText(NUMBER_OF_ATTEMPTS_LEFT + 8);

                }
            });
        }

        @Override
        protected Task<String> createTask(){
            return new Task<String>() {
                @Override
                protected String call() throws Exception {
                    Thread.sleep(5000);
                    // This is the place to do some network call
                    return ENTER_CHAR_OR_WORD;
                }
            };
        }
    }



}
