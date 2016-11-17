package main.java.se.kth.h16p02.npwj.hw1.client;

public interface ConnectServiceInterface {
    void onSucceeded(HangmanServerConnection server);
    void onFailure();
}
