package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public abstract class Controller
{
    private static final int DEFAULT_PORT = 1099;
    private static final String DEFAULT_HOST = "localhost";

    private String bankname = "Nordea";
    private String host;
    private int port;

    protected BufferedReader consoleIn;
    protected Trader user;
    protected String username;

    protected Bank bank;
    protected MarketPlace marketPlace;

    public Controller(Trader user, String host, int port)
    {
        this.user = user;
        this.host = host;
        this.port = port;
        this.consoleIn = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            if (this.user != null)
                this.username = this.user.getUsername();
            else
                this.username = "UnknownUser";
        }
        catch (RemoteException ex)
        {
            System.err.println(ex);
            this.username = "UnknownUser";
        }

        // Get bank and marketplace remote objects

        try
        {
            try
            {
                LocateRegistry.getRegistry(host, port).list();
                System.out.println("Controller: Found registry at: " + host + " at port " + port);
            }
            catch (RemoteException e)
            {
                if (host.equals(DEFAULT_HOST))
                {
                    LocateRegistry.createRegistry(DEFAULT_PORT);
                    System.out.println("Controller: Created registry at: " + DEFAULT_HOST + " at port " + DEFAULT_PORT);
                }
                else
                {
                    System.out.println(e);
                    System.exit(1);
                }
            }

            //Properties props = System.getProperties();
            //props.setProperty("java.rmi.server.hostname", host);
            bank = (Bank) Naming.lookup(this.bankname);
            marketPlace = (MarketPlace)Naming.lookup("MarketPlace");
        }
        catch (Exception e)
        {
            System.out.println("Controller: The runtime failed: " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Controller - Connected to bank: " + bankname);
        System.out.println("Controller - Connected to marketplace");
    }

    protected abstract void run();
    protected abstract void printConsolePrompt();
    protected abstract Command parse(String userInput);
}
