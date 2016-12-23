package apg.model;

import apg.exceptions.NotEnoughStockException;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(
                name = "findItemBySku",
                query = "SELECT item FROM Item item WHERE item.SKU LIKE :SKU"
        ),
        @NamedQuery(
                name = "getAllItems",
                query = "SELECT item FROM Item item"
        )
})

@Entity
@Table(name = "ITEM")
public class Item implements Serializable
{
    @Id
    @Column(name="SKU", nullable = false)
    private String SKU;
    @Column(name="DESCRIPTION", nullable = true)
    private String description;
    @Column(name="PRICE", nullable = false)
    private int price;
    @Column(name="STOCK", nullable = false)
    private int stock;

    public Item()
    {}

    public Item(String SKU, String description, int price, int stock)
    {
        this.SKU = SKU;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public String getSKU()
    {
        return SKU;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public int getStock()
    {
        return stock;
    }

    public void setStock(int stock)
    {
        this.stock = stock;
    }

    public void withdraw(int amount) throws NotEnoughStockException
    {
        if(amount > stock)
            throw new NotEnoughStockException("Overdraft attempted, stock: " + stock + ", amount: " + amount);

        stock = stock - amount;
    }

    @Override
    public String toString()
    {
        return "Item{" +
                "SKU='" + SKU + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
