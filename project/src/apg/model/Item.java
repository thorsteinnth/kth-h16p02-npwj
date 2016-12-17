package apg.model;

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
    @Column(name="DESCRIPTION", nullable = false)
    private String description;
    @Column(name="PRICE", nullable = false)
    private int price;

    public Item()
    {}

    public Item(String SKU, String description, int price)
    {
        this.SKU = SKU;
        this.description = description;
        this.price = price;
    }

    public String getSKU()
    {
        return SKU;
    }

    public String getDescription()
    {
        return description;
    }

    public int getPrice()
    {
        return price;
    }

    @Override
    public String toString()
    {
        return "Item{" +
                "SKU='" + SKU + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
