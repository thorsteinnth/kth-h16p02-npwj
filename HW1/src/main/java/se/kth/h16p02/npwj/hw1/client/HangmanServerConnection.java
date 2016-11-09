package main.java.se.kth.h16p02.npwj.hw1.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class HangmanServerConnection {
    private BufferedReader in;
    private BufferedWriter out;

    public HangmanServerConnection (String host, int port) {
        try{
            Socket clientSocket = new Socket(host,port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }
        catch (UnknownHostException e){
            System.err.println("Don't know about host: " + host + ".");
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + host + ".");
            System.exit(1);
        }
    }

    /**
     * The functions takes in a string as a parameter
     * Sends the string to server using pre initialized socket
     * Returns the respond from the server
     */

    public String callServer(String jsonToServer) throws IOException {

        out.write(jsonToServer);
        out.newLine();
        // Send a second newline to indicate that we are done sending
        out.newLine();
        out.flush();

        String respond = receiveMessage();

        return new String(respond);
    }

    private String receiveMessage()
    {
        try
        {
            StringBuilder sb = new StringBuilder();

            String incomingLine;
            while ((incomingLine = in.readLine()) != null && incomingLine.length() > 0)
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

}
