package se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable
{
    private final String name;
    private BigDecimal price;

    public Item (String name, BigDecimal price)
    {
        this.name = name;
        this.price = price;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (!name.equals(item.name)) return false;
        return price.equals(item.price);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public String toDisplayString() {
        return name.toString() +
                " - " +
                "Price: " +  price.toString();
    }
}
