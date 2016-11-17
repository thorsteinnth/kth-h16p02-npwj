package main.java.se.kth.h16p02.npwj.hw1.client.Services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import main.java.se.kth.h16p02.npwj.hw1.client.ConnectServiceInterface;
import main.java.se.kth.h16p02.npwj.hw1.client.HangmanServerConnection;


public class ConnectService extends Service<HangmanServerConnection> {

    private String serverIP;
    private String portNumber;
    private ConnectServiceInterface connectServiceInterface;

    public ConnectService(String serverIP, String portNumber, ConnectServiceInterface connectServiceInterface) {

        this.serverIP = serverIP;
        this.portNumber = portNumber;
        this.connectServiceInterface = connectServiceInterface;

        setOnSucceeded((WorkerStateEvent event) -> {
            this.connectServiceInterface.onSucceeded(getValue());
        });

        setOnFailed((WorkerStateEvent event) -> {
            System.out.println(getException().getMessage());
            this.connectServiceInterface.onFailure();
        });
    }

    @Override
    protected Task<HangmanServerConnection> createTask() {
        return new Task<HangmanServerConnection>() {
            @Override
            protected HangmanServerConnection call() throws Exception {
                //Thread.sleep(3000);
                System.out.println("Running and trying to start a connection");

                return new HangmanServerConnection(serverIP, Integer.parseInt(portNumber));
            }
        };
    }
}
