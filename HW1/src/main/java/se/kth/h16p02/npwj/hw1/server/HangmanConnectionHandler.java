package main.java.se.kth.h16p02.npwj.hw1.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.*;

import java.io.*;
import java.net.Socket;

public class HangmanConnectionHandler extends Thread
{
    private Socket clientSocket;

    public HangmanConnectionHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    public void run()
    {
        BufferedReader br;
        BufferedWriter bw;

        try
        {
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
            return;
        }

        try
        {
            String incomingLine;
            while ((incomingLine = br.readLine()) != null && incomingLine.length() > 0)
            {
                System.out.println("Received: " + incomingLine);
                String response = handleRequest(incomingLine);
                bw.write(response);
                bw.newLine();
            }

            // Send a second newline to indicate that we are done sending
            bw.newLine();
            bw.flush();
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
        }

        try
        {
            br.close();
            bw.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String handleRequest(String incomingRequest)
    {
        // TODO Should return response object

        try
        {
            Request request = deserializeRequest(incomingRequest);

            if (request instanceof ReqCreatePlayerAndStartGame)
            {
                return createPlayerAndStartGame();
            }
            else if (request instanceof ReqGuess)
            {
                ReqGuess req = (ReqGuess) request;
                System.out.println(req.toString());
                return req.toString();
            }
            else
            {
                throw new InvalidRequestException();
            }
        }
        catch (InvalidRequestException ex)
        {
            System.err.println("Invalid request: " + incomingRequest);
            return "Invalid request";
        }
    }

    private RequestType getRequestTypeFromJson(String requestJson)
    {
        JsonObject requestJsonObj = new Gson().fromJson(requestJson, JsonObject.class);
        String sRequestType = requestJsonObj.get("requestType").getAsString();

        try
        {
            return RequestType.valueOf(sRequestType);
        }
        catch (IllegalArgumentException ex)
        {
            System.err.println("Unable to parse request type: " + sRequestType);
            return RequestType.Unknown;
        }
    }

    private Request deserializeRequest(String requestJson) throws InvalidRequestException
    {
        // Get the request type
        RequestType requestType = getRequestTypeFromJson(requestJson);
        System.out.println("PARSED REQUEST TYPE: " + requestType);

        // Deserialize to appropriate request object
        switch (requestType)
        {
            case CreatePlayerAndStartGame:
                return new Gson().fromJson(requestJson, ReqCreatePlayerAndStartGame.class);
            case Guess:
                return new Gson().fromJson(requestJson, ReqGuess.class);
            case Unknown:
                throw new InvalidRequestException();
            default:
                throw new InvalidRequestException();
        }
    }

    private String createPlayerAndStartGame()
    {
        // TODO Return a response object
        return "This should be a create player and start game response :)";
    }
}
