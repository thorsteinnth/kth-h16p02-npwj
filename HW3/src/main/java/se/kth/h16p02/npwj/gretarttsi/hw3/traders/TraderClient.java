package se.kth.h16p02.npwj.gretarttsi.hw3.traders;

import se.kth.h16p02.npwj.gretarttsi.hw3.controllers.LoginController;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.MarketPlace;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class TraderClient
{
    private static final int DEFAULT_PORT = 1099;
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_BANK_NAME = "Nordea";
    public static boolean goToLogin = false;
    public static State state;

    public enum State
    {
        bank,
        marketplace,
        home
    }

    public TraderClient()
    {}

    public static void main (String[] args)
    {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        if (args.length == 2)
        {
            try
            {
                host = args[0];
                port = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e)
            {
                System.out.println(e);
                System.exit(1);
            }
        }

        // Get bank and marketplace remote objects

        System.out.println("TraderClient: Starting up ...");

        Bank bank;
        MarketPlace marketPlace;

        try
        {
            try
            {
                LocateRegistry.getRegistry(host, port).list();
                System.out.println("TraderClient: Found registry at: " + host + " at port " + port);
            }
            catch (RemoteException e)
            {
                if (host.equals(DEFAULT_HOST))
                {
                    LocateRegistry.createRegistry(DEFAULT_PORT);
                    System.out.println("TraderClient: Created registry at: " + DEFAULT_HOST + " at port " + DEFAULT_PORT);
                }
                else
                {
                    System.out.println(e);
                    System.exit(1);
                }
            }

            //Properties props = System.getProperties();
            //props.setProperty("java.rmi.server.hostname", host);
            bank = (Bank) Naming.lookup(DEFAULT_BANK_NAME);
            marketPlace = (MarketPlace)Naming.lookup("MarketPlace");

            System.out.println("TraderClient - Connected to bank: " + DEFAULT_BANK_NAME);
            System.out.println("TraderClient - Connected to marketplace");

            new LoginController(bank, marketPlace).run();
        }
        catch (Exception e)
        {
            System.err.println(e);
            e.printStackTrace();
            System.out.println("TraderClient: The runtime failed: " + e.getMessage());
            System.exit(0);
        }
    }
}