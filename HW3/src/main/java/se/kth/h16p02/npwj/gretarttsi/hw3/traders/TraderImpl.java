package se.kth.h16p02.npwj.gretarttsi.hw3.traders;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import se.kth.h16p02.npwj.gretarttsi.hw3.controllers.HomeController;
import se.kth.h16p02.npwj.gretarttsi.hw3.controllers.LoginController;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

public class TraderImpl extends UnicastRemoteObject implements Trader
{
    private static final String WISH_LIST_AVAILABLE = "The item %s that you have in your wish list is now available in the marketplace";
    private static final String ITEM_SOLD = "Your item %s has been sold on the marketplace. Deposit has been made to your account";

    private static final int DEFAULT_PORT = 1099;
    private static final String DEFAULT_HOST = "localhost";

    private Bank bankobj;
    private MarketPlace marketplaceobj;
    private String bankname = "Nordea";
    private String username;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TraderImpl trader = (TraderImpl) o;

        return username.equals(trader.username);
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }

    public TraderImpl(String username, String host, int port) throws RemoteException
    {
        super();

        this.username = username;

        try
        {
            try
            {
                LocateRegistry.getRegistry(host,port).list();
                System.out.println("Found registry at: " + host + " at port " + port);
            }
            catch (RemoteException e)
            {
                if(host.equals(DEFAULT_HOST))
                {
                    LocateRegistry.createRegistry(DEFAULT_PORT);
                    System.out.println("Created registry at: " + DEFAULT_HOST + " at port " + DEFAULT_PORT);
                }
                else
                {
                    System.out.println(e);
                    System.exit(1);
                }
            }

            //Properties props = System.getProperties();
            //props.setProperty("java.rmi.server.hostname", host);
            bankobj = (Bank) Naming.lookup(this.bankname);
            marketplaceobj = (MarketPlace)Naming.lookup("MarketPlace");

        }
        catch (Exception e)
        {
            System.out.println("The runtime failed: " + e.getMessage());
            System.exit(0);
        }

        System.out.println(this.username + " - Connected to bank: " + bankname);
        System.out.println(this.username + " - Connected to marketplace");
    }

    @Override
    public String getUsername()
    {
        return this.username;
    }

    @Override
    public MarketPlace getMarketPlace() throws RemoteException
    {
        return this.marketplaceobj;
    }

    @Override
    public Bank getBank() throws RemoteException
    {
        return this.bankobj;
    }

    @Override
    public void wishListAvailableNotification(String itemName) throws RemoteException {
        System.out.println("\n" + String.format(WISH_LIST_AVAILABLE,itemName));
        // TODO Print the correct console output depending on what controller he is in
    }

    @Override
    public void itemSoldNotification(String itemName) throws RemoteException {
        System.out.println("\n" + String.format(ITEM_SOLD,itemName));
        // TODO Print the correct console output depending on what controller he is in
    }

    public void run()
    {
        //new HomeController(this).run();
        new LoginController(null).run();
    }
}
