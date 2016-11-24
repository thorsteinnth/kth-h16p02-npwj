package se.kth.h16p02.npwj.gretarttsi.hw2.bank;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Bank;

public class BankServer
{
    private static final String BANK = "Nordea";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1099;

    public BankServer(String bankName, String host, int port)
    {
        try
        {
            Bank bankobj = new BankImpl(bankName);

            // Register the newly created object at rmiregistry.
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

            Naming.rebind(bankName, bankobj);
            System.out.println(bankobj + " is ready.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

        String bankName;
        if (args.length > 0)
        {
            bankName = args[0];
        }
        else
        {
            bankName = BANK;
        }

        new BankServer(bankName, host, port);
    }
}
