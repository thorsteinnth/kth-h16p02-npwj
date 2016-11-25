package se.kth.h16p02.npwj.gretarttsi.hw2.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.MarketPlace;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MarketPlaceServer
{
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1099;

    public MarketPlaceServer(String host, int port)
    {
        try
        {
            MarketPlace marketPlace = new MarketPlaceImpl(host, port);

            try
            {
                LocateRegistry.getRegistry(host, port).list();
                System.out.println("Found registry at: " + host + " " + port);
            }
            catch (RemoteException e)
            {
                if (host.equals(DEFAULT_HOST))
                {
                    LocateRegistry.createRegistry(DEFAULT_PORT);
                    System.out.println("Created registry at: " + DEFAULT_HOST + " " + DEFAULT_PORT);
                }
                else
                {
                    System.err.println(e);
                    System.exit(1);
                }
            }

            Naming.rebind("MarketPlace", marketPlace);
            System.out.println("Marketplace is ready.");
        }
        catch (RemoteException|MalformedURLException ex)
        {
            System.err.println(ex.toString());
        }
    }

    public static void main(String[] args)
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
            catch (NumberFormatException ex)
            {
                System.err.println(ex);
                System.exit(1);
            }
        }

        new MarketPlaceServer(host, port);
    }
}
