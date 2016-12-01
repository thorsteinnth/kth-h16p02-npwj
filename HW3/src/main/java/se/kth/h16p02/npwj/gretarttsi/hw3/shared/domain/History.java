package se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable
{
    private ArrayList<SaleItem> soldItems;
    private ArrayList<SaleItem> boughtItems;

    public History(ArrayList<SaleItem> soldItems, ArrayList<SaleItem> boughtItems)
    {
        this.soldItems = soldItems;
        this.boughtItems = boughtItems;
    }

    public ArrayList<SaleItem> getSoldItems()
    {
        return soldItems;
    }

    public ArrayList<SaleItem> getBoughtItems()
    {
        return boughtItems;
    }

    public int getNumberOfSoldItems()
    {
        return this.soldItems.size();
    }

    public int getNumberOfBoughtItems()
    {
        return this.boughtItems.size();
    }

    public String toDisplayString()
    {
        StringBuilder sb = new StringBuilder();

        if (getNumberOfSoldItems() == 0 && getNumberOfBoughtItems() == 0)
        {
            sb.append("Your history is empty");
            return sb.toString();
        }

        sb.append(System.lineSeparator());

        sb.append("Number of sold items: " + getNumberOfSoldItems() + "\n");
        for (SaleItem soldItem : this.soldItems)
        {
            sb.append(soldItem.toDisplayString() + "\n");
        }

        sb.append(System.lineSeparator());

        sb.append("Number of bought items: " + getNumberOfBoughtItems() + "\n");
        for (SaleItem boughtItem : this.boughtItems)
        {
            sb.append(boughtItem.toDisplayString() + "\n");
        }

        sb.append(System.lineSeparator());

        return sb.toString();
    }

    @Override
    public String toString()
    {
        return "History{" +
                "soldItems=" + soldItems +
                ", boughtItems=" + boughtItems +
                '}';
    }
}
