package se.kth.h16p02.npwj.gretarttsi.hw3.traders;

import se.kth.h16p02.npwj.gretarttsi.hw3.controllers.LoginController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TraderClient
{
    private static final int DEFAULT_PORT = 1099;
    private static final String DEFAULT_HOST = "localhost";

    public TraderClient() {
    }

    public static void main (String[] args)
    {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        if (args.length == 2)
        {
            try
            {
                host = args[0];
                port = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e)
            {
                System.out.println(e);
                System.exit(1);
            }
        }

        new LoginController(host, port).run();

        /*
        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
        String clientName = "";

        while (clientName == "")
        {

            System.out.print("Please insert a username: ");
            try {
                String userInput = consoleIn.readLine();

                if(userInput != null && userInput != "")
                {
                    clientName = userInput;
                    new TraderImpl(clientName, host, port).run();
                    break;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }*/
    }


}