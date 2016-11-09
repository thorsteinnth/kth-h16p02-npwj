package main.java.se.kth.h16p02.npwj.hw1.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.java.se.kth.h16p02.npwj.hw1.client.Services.ConnectService;
import main.java.se.kth.h16p02.npwj.hw1.client.Services.CreateNewPlayerAndStartGameService;
import main.java.se.kth.h16p02.npwj.hw1.client.Services.EndGameService;
import main.java.se.kth.h16p02.npwj.hw1.client.Services.StartGameService;
import main.java.se.kth.h16p02.npwj.hw1.client.Services.GuessService;
import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Game;
import main.java.se.kth.h16p02.npwj.hw1.shared.responses.ResGameState;


public class HangmanFXMLClientController implements Initializable{

    private HangmanServerConnection server;
    private ResGameState resGameState;

    private static final String PRESS_START_TO_BEGINN = "Please press start to begin the fantastic game of hangman";
    private static final String PRESS_CONNECT_TO_CONTINUE = "Please press connect to continue";
    private static final String STARTING_GAME = "Starting a new game ....";
    private static final String ENDING_GAME = "Ending the game ....";
    private static final String POSTING_GUESS = "Posting the guess ....";
    private static final String CONNECTING_TO_SERVER = "Wait. Connecting to server";
    private static final String CONNECT = "Connect";
    private static final String DISCONNECT = "Disconnect";
    private static final String START_GAME = "Start game";
    private static final String END_GAME = "End game";
    private static final String PLAYER_ID = "Player Id: ";
    private static final String PLAYER_SCORE = "Player score: ";
    private static final String GAME_ID = "Game Id: ";
    private static final String ENTER_CHAR_OR_WORD = "Enter a character or a word";
    private static final String NUMBER_OF_ATTEMPTS_LEFT = "Number of attempts left: ";
    private static final String GAME_BOARD = "THE HANGMAN GAME BOARD";
    private static final String PLAYER_WON = "You won";
    private static final String PLAYER_LOST = "You lost";
    private static final String GUESSED_CHARACTERS_DEF = "Guesses: ";
    private static final String SHOWCASE_BUTTON = "Showcase button";

    @FXML
    private  Button startEndButton;

    @FXML
    private Button connectionButton;

    @FXML
    private Text guessText;

    @FXML
    private  Button guessButton;

    @FXML
    private  TextField guessTextField;

    @FXML
    private Text infoText;

    @FXML
    private Text gameBoardDesText;

    @FXML
    private Text gameBoardText;

    @FXML
    private Text nrOfAttemptsLeftText;

    @FXML
    private Text guessedCharactersDef;

    @FXML
    private Text guessedCharacters;

    @FXML
    private Button showCaseButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        guessButton.setDisable(true);
        startEndButton.setDisable(true);
        startEndButton.setText(START_GAME);
        guessTextField.setDisable(true);
        infoText.setText("");
        nrOfAttemptsLeftText.setText("");
        gameBoardDesText.setText("");
        gameBoardText.setText("");
        guessedCharactersDef.setText(GUESSED_CHARACTERS_DEF);
        guessedCharacters.setText("");
        showCaseButton.setText(SHOWCASE_BUTTON);

    }

    @FXML
    private void onStartEndButtonPress(ActionEvent ae){

        if(this.startEndButton.getText().equals(START_GAME) && this.resGameState != null){
            CommandInterface onSucceed = (resGameState) -> {
                this.resGameState = resGameState;
                startingGameGUIControl();
            };

            new StartGameService(this.server, onSucceed, this.resGameState.getPlayerId()).start();
        }
        else if(this.startEndButton.getText().equals(END_GAME) && this.resGameState != null){
            guessText.setText(ENDING_GAME);
            DisableEverything();

            CommandInterface onSucceed = (resGameState) -> {
                System.out.println("Ending game complete with resGameState: " + resGameState);
                initializeTexts();
                this.resGameState = resGameState;
                this.guessText.setText(PRESS_START_TO_BEGINN);
                this.startEndButton.setText(START_GAME);
                this.startEndButton.requestFocus();
                this.startEndButton.setDisable(false);
                this.connectionButton.setDisable(false);
                setInfoText(false);
            };

            new EndGameService(this.server, onSucceed, this.resGameState.getGameId()).start();
        }
        else {
            startEndButton.setDisable(true);
            guessText.setText(STARTING_GAME);

            CommandInterface onSucceed = (resGameState) -> {
                this.resGameState = resGameState;
                startingGameGUIControl();
            };

            new CreateNewPlayerAndStartGameService(this.server, onSucceed).start();
        }
    }

    @FXML
    private void onGuessButtonPressed(ActionEvent ae){
        if(!this.guessTextField.getText().isEmpty() && !this.guessTextField.getText().equals("")) {
            guessButton.setDisable(true);
            //TODO do stuff before
            DisableEverything();
            this.guessText.setText(POSTING_GUESS);

            CommandInterface onSucceed = (resGameState) -> {
                this.resGameState = resGameState;
                handleGuessRespond();
            };

            new GuessService(this.server, onSucceed, this.resGameState.getGameId(), this.guessTextField.getText()).start();
        }
    }

    @FXML
    private void onConnectionButtonPress(ActionEvent ea) {

        System.out.println("The connection button pressed event handler with connection button text set to: " + connectionButton.getText());
        if(connectionButton.getText().equals(CONNECT)){
            System.out.println("Inside the connection button press");
            guessText.setText(CONNECTING_TO_SERVER);
            connectionButton.setDisable(true);

            ConnectServiceInterface onSucceeded = (HangmanServerConnection server) -> {
                this.server = server;

                if(this.resGameState == null){
                    guessText.setText(PRESS_START_TO_BEGINN);
                    startEndButton.requestFocus();
                    startEndButton.setText(START_GAME);
                    connectionButton.setDisable(false);
                    startEndButton.setDisable(false);
                }
                else {
                    startEndButton.setText(END_GAME);
                    guessText.setText(ENTER_CHAR_OR_WORD);
                    EnableEverything();
                }

                connectionButton.setText(DISCONNECT);
            };

            new ConnectService("localhost", "4444", onSucceeded).start();
        }
        else if (connectionButton.getText() == DISCONNECT){
            this.server = null;
            DisableEverything();
            connectionButton.setText(CONNECT);
            connectionButton.setDisable(false);
            this.guessText.setText(PRESS_CONNECT_TO_CONTINUE);
        }
    }

    private void DisableEverything ()
    {
        connectionButton.setDisable(true);
        startEndButton.setDisable(true);
        guessTextField.setDisable(true);
        guessButton.setDisable(true);
    }

    private void EnableEverything ()
    {
        connectionButton.setDisable(false);
        startEndButton.setDisable(false);
        guessTextField.setDisable(false);
        guessButton.setDisable(false);
    }

    private void initializeTexts ()
    {
        this.guessText.setText("");
        this.infoText.setText("");
        this.gameBoardDesText.setText("");
        this.gameBoardText.setText("");
        this.nrOfAttemptsLeftText.setText("");
        this.guessedCharacters.setText("");
    }

    private void setInfoText(boolean withGameId)
    {
        if(withGameId){
            this.infoText.setText(
                    GAME_ID + resGameState.getGameId() + ", " +
                            PLAYER_ID + resGameState.getPlayerId() + ", " +
                            PLAYER_SCORE + resGameState.getPlayerScore()
            );
        }
        else{
            this.infoText.setText(
                    PLAYER_ID + resGameState.getPlayerId() + ", " +
                    PLAYER_SCORE + resGameState.getPlayerScore()
            );
        }
    }

    private void startingGameGUIControl()
    {
        this.startEndButton.setText(END_GAME);
        this.startEndButton.setDisable(false);
        this.guessText.setText(ENTER_CHAR_OR_WORD);
        this.guessTextField.setDisable(false);
        this.guessButton.setDisable(false);
        this.gameBoardDesText.setText(GAME_BOARD);
        updatingGameGUI();
    }

    private void updatingGameGUI ()
    {
        this.gameBoardText.setText(resGameState.getGameStateString().replace("", " ").trim());
        setInfoText(true);
        this.nrOfAttemptsLeftText.setText(NUMBER_OF_ATTEMPTS_LEFT + resGameState.getNumberOfGuessesLeft());
        this.guessTextField.setText("");
        this.guessedCharacters.setText(guessedCharactersToString());
    }

    private String guessedCharactersToString()
    {
        return resGameState.getGuessedCharacters().toString().replace(""," ").trim();
    }


    private void handleGuessRespond ()
    {
        if(this.resGameState.getGameState() == Game.GameState.Won)
        {
            playerWon();
        }
        else if (this.resGameState.getGameState() == Game.GameState.Lost)
        {
            playerLost();
        }
        else if (this.resGameState.getGameState() == Game.GameState.Cancelled)
        {

        }
        else
        {
            //The game is still in progress
            EnableEverything();
            this.guessText.setText(ENTER_CHAR_OR_WORD);
            updatingGameGUI();
        }
    }

    private void playerWon ()
    {
        this.startEndButton.setText(START_GAME);
        EnableEverything();
        updatingGameGUI ();
        this.guessText.setText(PRESS_START_TO_BEGINN);
        this.guessTextField.setDisable(true);
        this.guessButton.setDisable(true);
        this.gameBoardDesText.setText(PLAYER_WON);
        this.nrOfAttemptsLeftText.setText("");
    }

    private void playerLost ()
    {
        this.startEndButton.setText(START_GAME);
        EnableEverything();
        updatingGameGUI ();
        this.guessText.setText(PRESS_START_TO_BEGINN);
        this.guessTextField.setDisable(true);
        this.guessButton.setDisable(true);
        this.gameBoardDesText.setText(PLAYER_LOST);
        this.nrOfAttemptsLeftText.setText("");
    }
}
