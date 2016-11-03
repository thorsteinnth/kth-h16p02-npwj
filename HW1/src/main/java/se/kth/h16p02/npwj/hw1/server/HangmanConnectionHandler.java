package main.java.se.kth.h16p02.npwj.hw1.server;

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
                bw.write(new StringBuilder(incomingLine).reverse().toString());
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
}
