package main.java.se.kth.h16p02.npwj.hw1.client.Services;


import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import main.java.se.kth.h16p02.npwj.hw1.client.CommandInterface;
import main.java.se.kth.h16p02.npwj.hw1.client.HangmanServerConnection;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.ReqGuess;
import main.java.se.kth.h16p02.npwj.hw1.shared.responses.ResGameState;

public class GuessService extends Service<ResGameState>{

    HangmanServerConnection server;
    CommandInterface commandInterface;
    String gameId;
    String guess;

    public GuessService(HangmanServerConnection server, CommandInterface commandInterface, String gameId, String guess) {
        this.server = server;
        this.commandInterface = commandInterface;
        this.gameId = gameId;
        this.guess = guess;

        setOnSucceeded((WorkerStateEvent event) -> {
            ResGameState newResGameState = getValue();
            this.commandInterface.onSucceeded(newResGameState);
            System.out.println("we are getting the respond from the server in GuessService");
            System.out.println(newResGameState.toString());

        });

        setOnFailed((WorkerStateEvent event) -> {
            System.out.println(getException().getMessage());
        });
    }

    @Override
    protected Task<ResGameState> createTask(){
        return new Task<ResGameState>() {
            @Override
            protected ResGameState call() throws Exception {
                Thread.sleep(2000);
                System.out.println("Guessing");
                Gson gson = new Gson();
                String request = gson.toJson(new ReqGuess(gameId, guess));
                String respond = server.callServer(request);
                ResGameState resGameState = new ServiceHelper().HandleRespond(respond);
                return resGameState;
            }
        };
    }

}
