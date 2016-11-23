package se.kth.h16p02.npwj.gretarttsi.hw2.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.MarketPlace;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MarketPlaceServer
{
    public MarketPlaceServer()
    {
        try
        {
            MarketPlace marketPlace = new MarketPlaceImpl();

            try
            {
                LocateRegistry.getRegistry(1099).list();
            }
            catch (RemoteException e)
            {
                // Assuming that the registry doesn't exist
                LocateRegistry.createRegistry(1099);
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
        new MarketPlaceServer();
    }
}
