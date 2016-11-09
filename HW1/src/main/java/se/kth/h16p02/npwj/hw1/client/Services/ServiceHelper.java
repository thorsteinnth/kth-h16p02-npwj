package main.java.se.kth.h16p02.npwj.hw1.client.Services;

import com.google.gson.Gson;
import main.java.se.kth.h16p02.npwj.hw1.shared.responses.InvalidRespondException;
import main.java.se.kth.h16p02.npwj.hw1.shared.responses.ResGameState;

public class ServiceHelper {

    public static ResGameState HandleRespond (String respond) throws InvalidRespondException
    {
        try {
            ResGameState resGameState = new Gson().fromJson(respond, ResGameState.class);
            return resGameState;
        }
        catch (Exception e) {
            throw new InvalidRespondException();
        }
    }
}
