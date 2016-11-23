package se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain;

import java.math.BigDecimal;

public class Item
{
    private final int id;
    private final String name;
    private BigDecimal price;

    public Item (int id, String name, BigDecimal price)
    {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    @Override
    public String toString()
    {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
