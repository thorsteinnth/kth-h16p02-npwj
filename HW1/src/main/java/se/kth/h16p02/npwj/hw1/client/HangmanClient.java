package main.java.se.kth.h16p02.npwj.hw1.client;

import com.google.gson.Gson;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.ReqCreatePlayerAndStartGame;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.ReqGuess;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class HangmanClient
{
    public static void main(String[] args) throws IOException
    {
        Socket clientSocket = null;

        try
        {
            clientSocket = new Socket(args[0], 4444);
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host: " + args[0] + ".");
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for " + "the connection to: " + args[0] + "");
            System.exit(1);
        }

        BufferedReader br;
        BufferedWriter bw;

        try
        {
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            //bw.write(args[1]);
            bw.write(getRequestJson());
            bw.newLine();
            // Send a second newline to indicate that we are done sending
            bw.newLine();
            bw.flush();

            String incomingLine;
            while ((incomingLine = br.readLine()) != null && incomingLine.length() > 0)
            {
                System.out.println("Received: " + incomingLine);
            }

            br.close();
            bw.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
            System.exit(1);
        }
    }

    // TODO Just for testing purposes
    private static String getRequestJson()
    {
        //return new Gson().toJson(new ReqCreatePlayerAndStartGame());
        return new Gson().toJson(new ReqGuess("this is the game id", "this is the guess"));
    }
}
