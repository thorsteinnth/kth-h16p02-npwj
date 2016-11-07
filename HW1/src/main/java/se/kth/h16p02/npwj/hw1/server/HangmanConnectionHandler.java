package main.java.se.kth.h16p02.npwj.hw1.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Game;
import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Player;
import main.java.se.kth.h16p02.npwj.hw1.server.Service.GameNotFoundException;
import main.java.se.kth.h16p02.npwj.hw1.server.Service.GameService;
import main.java.se.kth.h16p02.npwj.hw1.server.Service.PlayerService;
import main.java.se.kth.h16p02.npwj.hw1.shared.requests.*;
import main.java.se.kth.h16p02.npwj.hw1.shared.responses.ResGameState;
import main.java.se.kth.h16p02.npwj.hw1.shared.responses.Response;

import java.io.*;
import java.net.Socket;

public class HangmanConnectionHandler extends Thread
{
    private Socket clientSocket;
    private PlayerService playerService;
    private GameService gameService;

    public HangmanConnectionHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        this.playerService = new PlayerService();
        this.gameService = new GameService();
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
            // TODO Need more elegant way to keep connection open
            while (true)
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
        try
        {
            Request request = deserializeRequest(incomingRequest);

            if (request instanceof ReqCreatePlayerAndStartGame)
            {
                ResGameState response = createPlayerAndStartGame();
                return getResponseJson(response);
            }
            else if (request instanceof ReqGuess)
            {
                ReqGuess req = (ReqGuess) request;
                ResGameState response = guess(req);
                return getResponseJson(response);
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

    //region Request deserialization

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
        RequestType requestType = getRequestTypeFromJson(requestJson);

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

    //endregion

    //region Request handling

    private ResGameState createPlayerAndStartGame()
    {
        Player newPlayer = this.playerService.addPlayer();
        Game newGame = this.gameService.addGame(newPlayer);
        return new ResGameState(newGame);
    }

    private ResGameState guess(ReqGuess request) throws InvalidRequestException
    {
        // TODO Catch exception
        int gameId = Integer.parseInt(request.getGameId());

        try
        {
            Game foundGame = this.gameService.getGame(gameId);
            foundGame.addGuess(request.getGuess());
            return new ResGameState(foundGame);
        }
        catch (GameNotFoundException ex)
        {
            System.err.println("HangmanConnectionHandler.guess() - Game not found: " + gameId);
            throw new InvalidRequestException();
        }
        catch (IllegalStateException ex)
        {
            System.err.println("HangmanConnectionHandler.guess(): " + ex.toString());
            throw new InvalidRequestException();
        }
    }

    private String getResponseJson(Response response)
    {
        return new Gson().toJson(response);
    }

    //endregion
}
