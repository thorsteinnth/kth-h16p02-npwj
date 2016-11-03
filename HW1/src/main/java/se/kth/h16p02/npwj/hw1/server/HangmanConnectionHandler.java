package main.java.se.kth.h16p02.npwj.hw1.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
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
        BufferedInputStream in;
        BufferedOutputStream out;

        try
        {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new BufferedOutputStream(clientSocket.getOutputStream());
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
            return;
        }

        /*
        // Sleep test
        try
        {
            System.out.println("Server connection handler sleeping for 20 sec");
            Thread.sleep(20000);
        }
        catch (InterruptedException ie)
        {
            System.out.println("Interrupted exception caught");
        }
        */

        try
        {
            byte[] msg = new byte[4096];
            int bytesRead = 0;
            int n;

            while ((n = in.read(msg, bytesRead, 256)) != -1)
            {
                bytesRead += n;

                if (bytesRead == 4096)
                {
                    break;
                }
                if (in.available() == 0)
                {
                    break;
                }
            }

            for (int i = bytesRead; i > 0; i--)
            {
                out.write(msg[i - 1]);
            }

            out.flush();

        }
        catch (IOException e)
        {
            System.out.println(e.toString());
        }

        try
        {
            out.close();
            in.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
