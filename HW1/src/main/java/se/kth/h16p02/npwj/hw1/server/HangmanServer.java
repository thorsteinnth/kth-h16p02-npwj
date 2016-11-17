package main.java.se.kth.h16p02.npwj.hw1.server;

import main.java.se.kth.h16p02.npwj.hw1.server.Service.Repository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HangmanServer
{
    public static void main(String[] args) throws IOException
    {
        new Repository();

        boolean listening = true;
        ServerSocket serverSocket = null;

        int portNumber = 4444;  // Default port number

        // Get port number from args, if any
        if (args.length == 1)
        {
            try
            {
                portNumber = Integer.valueOf(args[0]);
            }
            catch (NumberFormatException ex)
            {
                System.err.println(ex.toString());
            }
        }

        try
        {
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException e)
        {
            System.err.println(e.toString());
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
