package apg.model;

import javax.persistence.*;

@Entity
@Table(name = "SCITEM")
public class ShoppingCartItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ITEM_SKU", nullable = false)
    private Item item;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="USER_ID", nullable = false)
    private User user;
    @Column(name = "QTY", nullable = false)
    private int quantity;

    public ShoppingCartItem()
    {}

    public ShoppingCartItem(User user, Item item)
    {
        this.user = user;
        this.item = item;
        this.quantity = 1;
    }

    public Long getId()
    {
        return id;
    }

    public Item getItem()
    {
        return item;
    }

    public User getUser()
    {
        return user;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public int getTotalAmount()
    {
        return this.item.getPrice() * quantity;
    }

    @Override
    public String toString()
    {
        return "ShoppingCartItem{" +
                "id=" + id +
                ", item=" + item +
                ", user=" + user +
                ", quantity=" + quantity +
                '}';
    }
}
