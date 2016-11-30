package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class Controller
{
    protected BufferedReader consoleIn;
    private Trader user;

    protected Trader getUser()
    {
        return this.user;
    }

    public Controller(Trader user)
    {
        this.user = user;
        this.consoleIn = new BufferedReader(new InputStreamReader(System.in));
    }

    protected abstract void run();
    protected abstract void printConsolePrompt();

}
