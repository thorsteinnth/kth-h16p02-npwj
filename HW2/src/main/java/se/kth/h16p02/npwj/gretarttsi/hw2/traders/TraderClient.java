package se.kth.h16p02.npwj.gretarttsi.hw2.traders;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Exceptions.RejectedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TraderClient
{
    private static final String USAGE = "java bankrmi.TraderClient <bank_url>";
    private static final String DEFAULT_BANK_NAME = "Nordea";

    public TraderClient() {
    }

    public static void main (String[] args)
    {
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
        String clientName = "";

        while (clientName == "") {

            System.out.print("Please insert a username: ");
            try {
                String userInput = consoleIn.readLine();

                if(userInput != null && userInput != "") {
                    clientName = userInput;
                    new TraderImpl(clientName).run();
                    break;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}