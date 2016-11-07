package main.java.se.kth.h16p02.npwj.hw1.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Game;
import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Player;
import main.java.se.kth.h16p02.npwj.hw1.server.Service.GameNotFoundException;
import main.java.se.kth.h16p02.npwj.hw1.server.Service.GameService;
import main.java.se.kth.h16p02.npwj.hw1.server.Service.PlayerNotFoundException;
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

        // Create player for testing purposes
        //this.playerService.addPlayer();

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
            // TODO Need more elegant way to keep connection open or break out of loop when connection closes
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
            else if (request instanceof ReqStartGame)
            {
                ReqStartGame req = (ReqStartGame) request;
                ResGameState response = startGame(req);
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
            // TODO Return a properly formatted error message for the client
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
            case StartGame:
                return new Gson().fromJson(requestJson, ReqStartGame.class);
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

    private ResGameState startGame(ReqStartGame request) throws InvalidRequestException
    {
        try
        {
            int playerId = Integer.parseInt(request.getPlayerId());
            Player foundPlayer = this.playerService.getPlayer(playerId);
            Game newGame = this.gameService.addGame(foundPlayer);
            return new ResGameState(newGame);
        }
        catch (PlayerNotFoundException ex)
        {
            System.err.println("HangmanConnectionHandler.startGame() - Player not found: " + request.getPlayerId());
            throw new InvalidRequestException();
        }
        catch (IllegalStateException|NumberFormatException ex)
        {
            System.err.println("HangmanConnectionHandler.startGame(): " + ex.toString());
            throw new InvalidRequestException();
        }
    }

    private ResGameState guess(ReqGuess request) throws InvalidRequestException
    {
        try
        {
            int gameId = Integer.parseInt(request.getGameId());
            Game updatedGame = this.gameService.addGuessToGame(gameId, request.getGuess());
            return new ResGameState(updatedGame);
        }
        catch (GameNotFoundException ex)
        {
            System.err.println("HangmanConnectionHandler.guess() - Game not found: " + request.getGameId());
            throw new InvalidRequestException();
        }
        catch (IllegalStateException|NumberFormatException ex)
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
