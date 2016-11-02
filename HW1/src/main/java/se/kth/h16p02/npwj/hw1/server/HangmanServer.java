package main.java.se.kth.h16p02.npwj.hw1.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HangmanServer
{
    public static void main(String[] args) throws IOException
    {
        boolean listening = true;
        ServerSocket serverSocket = null;

        try
        {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e)
        {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        while (listening)
        {
            Socket clientSocket = serverSocket.accept();
            (new HangmanConnectionHandler(clientSocket)).start();
        }

        serverSocket.close();
    }
}
