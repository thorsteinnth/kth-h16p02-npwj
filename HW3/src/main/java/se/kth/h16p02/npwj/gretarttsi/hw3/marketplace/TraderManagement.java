package se.kth.h16p02.npwj.gretarttsi.hw3.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * A wrapper for the traders (remote objects).
 * We have a remote trader object for the traders that are logged in.
 * */
public class TraderManagement
{
    // TODO We make it so all accesses to traders go through here.
    // Then we could catch connection exceptions for traders that have gone offline, and then
    // log them out automatically (by removing them from our list of logged in traders)

    private static ArrayList<Trader> loggedInTraders;

    static
    {
        loggedInTraders = new ArrayList<>();
    }

    public static void loginTrader(Trader trader)
    {
        loggedInTraders.add(trader);
    }

    public static void logoutTrader(Trader trader)
    {
        loggedInTraders.remove(trader);
    }

    public static Trader getLoggedInTraderByUsername(String username)
    {
        Trader foundTrader = null;

        for (Trader trader : loggedInTraders)
        {
            try
            {
                if (trader.getUsername().equals(username))
                {
                    foundTrader = trader;
                    break;
                }
            }
            catch (RemoteException ex)
            {
                System.err.println(ex);

                if (ex instanceof ConnectException)
                {
                    // We assume we have lost connection to the trader
                    // Let's log him out
                    System.out.println("Logging out trader");
                    logoutTrader(trader);
                }
            }
        }

        return foundTrader;
    }
}
