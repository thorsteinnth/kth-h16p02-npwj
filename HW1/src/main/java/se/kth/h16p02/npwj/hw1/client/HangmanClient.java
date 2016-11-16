package main.java.se.kth.h16p02.npwj.hw1.client;

import com.google.gson.Gson;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.ReqCreatePlayerAndStartGame;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.ReqEndGame;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.ReqGuess;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.ReqStartGame;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Test client to test the server.
 * */
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

            /*
            bw.write(args[1]);
            bw.newLine();
            // Send a second newline to indicate that we are done sending
            bw.newLine();
            bw.flush();

            String incomingLine;
            while ((incomingLine = br.readLine()) != null && incomingLine.length() > 0)
            {
                System.out.println("Received: " + incomingLine);
            }
            */

            testCreatePlayerAndStartGameAndGuessWord(br, bw);
            //testStartGameAndGuessWord(br, bw);
            //testCreatePlayerAndStartGameAndGuessWordAndEndGame(br, bw);

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

    private static void sendJson(BufferedWriter bw, String json)
    {
        try
        {
            bw.write(json);
            bw.newLine();
            // Send a second newline to indicate that we are done sending
            bw.newLine();
            bw.flush();
        }
        catch (IOException ex)
        {
            System.out.println(ex.toString());
            System.exit(1);
        }
    }

    private static String receiveMessage(BufferedReader br)
    {
        try
        {
            StringBuilder sb = new StringBuilder();

            String incomingLine;
            while ((incomingLine = br.readLine()) != null && incomingLine.length() > 0)
            {
                sb.append(incomingLine);
            }

            return sb.toString();
        }
        catch (IOException ex)
        {
            System.out.println(ex.toString());
            System.exit(1);
            return "";
        }
    }

    //region Tests

    private static void testCreatePlayerAndStartGameAndGuessWord(BufferedReader br, BufferedWriter bw)
    {
        // We assume the word is "Gretar"
        // We assume the player ID will be 0
        // We assume the game ID will be 0

        Gson gson = new Gson();
        String response;

        sendJson(bw, gson.toJson(new ReqCreatePlayerAndStartGame()));
        response = receiveMessage(br);
        System.out.println("Received: " + response);

        sendJson(bw, gson.toJson(new ReqGuess("0", "e")));
        response = receiveMessage(br);
        System.out.println("Received: " + response);

        sendJson(bw, gson.toJson(new ReqGuess("0", "a")));
        response = receiveMessage(br);
        System.out.println("Received: " + response);

        sendJson(bw, gson.toJson(new ReqGuess("0", "Gretar")));
        response = receiveMessage(br);
        System.out.println("Received: " + response);
    }

    private static void testStartGameAndGuessWord(BufferedReader br, BufferedWriter bw)
    {
        // We assume the word is "Gretar"
        // We assume that there exists a player with ID 0
        // We assume the game ID will be 0

        Gson gson = new Gson();
        String response;

        sendJson(bw, gson.toJson(new ReqStartGame("0")));
        response = receiveMessage(br);
        System.out.println("Received: " + response);

        sendJson(bw, gson.toJson(new ReqGuess("0", "e")));
        response = receiveMessage(br);
        System.out.println("Received: " + response);

        sendJson(bw, gson.toJson(new ReqGuess("0", "a")));
        response = receiveMessage(br);
        System.out.println("Received: " + response);

        sendJson(bw, gson.toJson(new ReqGuess("0", "Gretar")));
        response = receiveMessage(br);
        System.out.println("Received: " + response);
    }

    private static void testCreatePlayerAndStartGameAndGuessWordAndEndGame(BufferedReader br, BufferedWriter bw)
    {
        // We assume the word is "Gretar"
        // We assume the player ID will be 0
        // We assume the game ID will be 0

        Gson gson = new Gson();
        String response;

        sendJson(bw, gson.toJson(new ReqCreatePlayerAndStartGame()));
        response = receiveMessage(br);
        System.out.println("Received: " + response);

        sendJson(bw, gson.toJson(new ReqGuess("0", "e")));
        response = receiveMessage(br);
        System.out.println("Received: " + response);

        sendJson(bw, gson.toJson(new ReqGuess("0", "a")));
        response = receiveMessage(br);
        System.out.println("Received: " + response);

        sendJson(bw, gson.toJson(new ReqEndGame("0")));
        response = receiveMessage(br);
        System.out.println("Received: " + response);
    }

    //endregion
}
