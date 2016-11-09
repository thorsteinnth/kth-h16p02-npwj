package main.java.se.kth.h16p02.npwj.hw1.client.Services;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import main.java.se.kth.h16p02.npwj.hw1.client.CommandInterface;
import main.java.se.kth.h16p02.npwj.hw1.client.HangmanServerConnection;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.ReqStartGame;
import main.java.se.kth.h16p02.npwj.hw1.shared.responses.ResGameState;


public class StartGameService extends Service<ResGameState>{
    HangmanServerConnection server;
    CommandInterface commandInterface;
    String playerId;

    public StartGameService(HangmanServerConnection server, CommandInterface commandInterface, String playerId) {
        this.server = server;
        this.commandInterface = commandInterface;
        this.playerId = playerId;

        setOnSucceeded((WorkerStateEvent event) -> {
            ResGameState newResGameState = getValue();
            this.commandInterface.onSucceeded(newResGameState);
            System.out.println("we are getting the response from the server in StartGameService");
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
                System.out.println("Starting game");
                Gson gson = new Gson();
                String request = gson.toJson(new ReqStartGame(playerId));
                String respond = server.callServer(request);
                ResGameState resGameState = new ServiceHelper().HandleRespond(respond);
                return resGameState;
            }
        };
    }

}
