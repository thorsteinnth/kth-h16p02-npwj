package main.java.se.kth.h16p02.npwj.hw1.client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.net.UnknownHostException;

public class HangmanServerConnection {

    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;

    public HangmanServerConnection (String host, int port) throws IOException {
        try{
            clientSocket = new Socket();
            clientSocket.setSoTimeout(200);
            clientSocket.connect(new InetSocketAddress(host,port),200);

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }
        catch (UnknownHostException e){
            System.err.println("Don't know about host: " + host + ".");
            throw e;
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + host + ".");
            throw e;
        }
    }

    /**
     * The functions takes in a string as a parameter
     * Sends the string to server using pre initialized socket
     * Returns the respond from the server
     */

    public String callServer(String jsonToServer) throws IOException, InterruptedException {
        try {
            out.write(jsonToServer);
            out.newLine();
            // Send a second newline to indicate that we are done sending
            out.newLine();
            out.flush();

            String respond = receiveMessage();

            return new String(respond);
        }
        catch (IOException|InterruptedException ex){
            System.out.println(ex.toString());
            throw ex;
        }
    }

    /**
     * This function handles the respond from the server
     * It returns the respond as a string
     * NOTE: This function throws Exception instead of IOException because
     * java fx wa not catching Java IOException for unknowns reason
     */
    private String receiveMessage() throws IOException, InterruptedException
    {
        try
        {
            //HACK ask teacher
            Thread.sleep(5);

            StringBuilder sb = new StringBuilder();

            String incomingLine;
            while ((incomingLine = in.readLine()) != null && incomingLine.length() > 0)
            {
                sb.append(incomingLine);
            }

            String receivedMessage = sb.toString();
            System.out.println("Received message:");
            System.out.println(receivedMessage);

            return receivedMessage;
        }
        catch (IOException|InterruptedException ex)
        {
            System.out.println(ex.toString());
            throw ex;
        }
    }

    public void closeConnection() {
        try {
            clientSocket.close();
            System.out.println("Client connection successfully closed ");
        }
        catch (IOException e){
            System.out.println("client connection failed to close");
        }
    }
}
