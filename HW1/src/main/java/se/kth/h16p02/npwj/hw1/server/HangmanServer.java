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
        final int portNumber = 4444;

        try
        {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e)
        {
            System.err.println("Could not listen on port: " + portNumber);
            System.exit(1);
        }

        while (listening)
        {
            System.out.println("Server listening on port: " + portNumber);
            Socket clientSocket = serverSocket.accept();
            (new HangmanConnectionHandler(clientSocket)).start();
            System.out.println("Client connection accepted");
        }

        serverSocket.close();
    }
}
