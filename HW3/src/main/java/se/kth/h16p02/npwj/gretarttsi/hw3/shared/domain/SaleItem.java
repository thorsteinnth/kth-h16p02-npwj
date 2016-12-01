package se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain;

import java.io.Serializable;

public class SaleItem implements Serializable
{
    private Item item;
    private String sellerName;

    public SaleItem(Item item, String sellerName)
    {
        this.item = item;
        this.sellerName = sellerName;
    }

    public Item getItem()
    {
        return item;
    }

    public String getSellerName()
    {
        return sellerName;
    }

    @Override
    public String toString()
    {
        return "SaleItem{" +
                "item=" + item +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }
}
