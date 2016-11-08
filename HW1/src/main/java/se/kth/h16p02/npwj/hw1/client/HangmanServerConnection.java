package main.java.se.kth.h16p02.npwj.hw1.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class HangmanServerConnection {
    private BufferedInputStream in;
    private BufferedOutputStream out;

    public HangmanServerConnection (String host, int port) {
        try{
            Socket clientSocket = new Socket(host,port);
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new BufferedOutputStream(clientSocket.getOutputStream());
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

    String callServer(String strToServer) throws IOException {
        // Send the string to server
        byte[] msg = strToServer.getBytes();
        out.write(msg,0,msg.length);
        out.flush();
        // Receive respond from server and return it
        // TODO we need to do something here to handle the different sizes of the incoming msg
        // TODO This is a problem that I need to fix

        byte[] fromServer = new byte[msg.length];
        int n = in.read(fromServer,0,fromServer.length);
        if(n != fromServer.length){
            throw new IOException("Failed to reverse, some data was lost.");
        }
        return new String(fromServer);
    }
}
