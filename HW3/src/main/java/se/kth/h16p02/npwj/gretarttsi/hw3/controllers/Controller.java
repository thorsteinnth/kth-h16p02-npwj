package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

public abstract class Controller
{
    protected BufferedReader consoleIn;
    protected Trader user;
    protected String username;

    public Controller(Trader user)
    {
        this.user = user;
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
    }

    protected abstract void run();
    protected abstract void printConsolePrompt();
    protected abstract Command parse(String userInput);
}
