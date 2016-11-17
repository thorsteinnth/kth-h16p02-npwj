package main.java.se.kth.h16p02.npwj.hw1.client.Services;

import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import main.java.se.kth.h16p02.npwj.hw1.client.CommandInterface;
import main.java.se.kth.h16p02.npwj.hw1.client.HangmanServerConnection;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.ReqCreatePlayerAndStartGame;
import main.java.se.kth.h16p02.npwj.hw1.shared.responses.ResGameState;

//public class CreateNewPlayerAndStartGameService extends Service<ResGameState>

// Use the concurrent service of javaFX
public class CreateNewPlayerAndStartGameService extends Service<ResGameState>{

    HangmanServerConnection server;
    CommandInterface commandInterface;

    public CreateNewPlayerAndStartGameService(HangmanServerConnection server, CommandInterface commandInterface) {
        this.server = server;
        this.commandInterface = commandInterface;

        setOnSucceeded((WorkerStateEvent event) -> {
            ResGameState resGameState = getValue();
            this.commandInterface.onSucceeded(resGameState);
            System.out.println("we are getting the response from the server");
            System.out.println(resGameState.toString());

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
                //Thread.sleep(2000);
                Gson gson = new Gson();
                String request = gson.toJson(new ReqCreatePlayerAndStartGame());
                String respond = server.callServer(request);
                ResGameState resGameState = new ServiceHelper().HandleRespond(respond);
                return resGameState;
            }
        };
    }
}