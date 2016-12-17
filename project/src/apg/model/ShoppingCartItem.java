package apg.model;

import javax.persistence.*;

@Entity
public class ShoppingCartItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;
    @ManyToOne
    private Item item;
    private int quantity;

    public ShoppingCartItem()
    {}

    public ShoppingCartItem(Item item)
    {
        this.item = item;
        this.quantity = 1;
    }

    public Item getItem()
    {
        return item;
    }

    public void setItem(Item item)
    {
        this.item = item;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    @Override
    public String toString()
    {
        return "ShoppingCartItem{" +
                "item=" + item +
                ", quantity=" + quantity +
                '}';
    }
}
