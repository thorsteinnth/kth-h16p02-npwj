package se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.startup;

import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.database.BankDBException;
import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.model.BankImpl;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {
    private static final int REGISTRY_PORT_NUMBER = 1099;
    private static final String USAGE = "java bankjdbc.Server [rmi-URL of a bank] "
            + "[database] [dbms: derby, mysql]";
    private String bankName = "Nordea";
    private String datasource = "Banks";
    private String dbms = "sqlite"; // Changed to sqlite from derby by Thorsteinn

    public static void main(String[] args) {
        Server server = new Server();
        server.parseCommandLineArgs(args);
        server.startRMIServant();
    }

    private void startRMIServant() {
        try {
            try {
                LocateRegistry.getRegistry(REGISTRY_PORT_NUMBER).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(REGISTRY_PORT_NUMBER);
            }
            BankImpl bankobj = new BankImpl(datasource, dbms);
            java.rmi.Naming.rebind(bankName, bankobj);
            System.out.println(bankobj + " is ready.");
        } catch (RemoteException | MalformedURLException |
                BankDBException e) {
            System.out.println("Failed to start bank server.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void parseCommandLineArgs(String[] args) {
        if (args.length > 3 || (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        if (args.length > 0) {
            bankName = args[0];
        }

        if (args.length > 1) {
            datasource = args[1];
        }

        if (args.length > 2) {
            dbms = args[2];
        }
    }
}
