package main.java.se.kth.h16p02.npwj.hw1.client;

import main.java.se.kth.h16p02.npwj.hw1.shared.responses.ResGameState;

/**
 * Created by GretarAtli on 08/11/2016.
 */
public interface CommandInterface {
    void onSucceeded(ResGameState resGameState);
}
