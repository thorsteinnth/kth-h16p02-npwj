package apg.controller;

import apg.exceptions.IllegalSkuException;
import apg.exceptions.ItemAlreadyExistsException;
import apg.model.Item;
import apg.model.User;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateful
public class AdminController
{
    @PersistenceContext(unitName = "apgPU")
    private EntityManager em;

    public List<Item> getAllItems()
    {
        Query query = em.createNamedQuery("getAllItems", Item.class);
        return query.getResultList();
    }

    public List<User> getAllUsers()
    {
        Query query = em.createNamedQuery("getAllUsers", User.class);
        return query.getResultList();
    }

    public void createNewItem(String sku, String name, String description, int price, int quantity)
            throws ItemAlreadyExistsException, IllegalSkuException
    {
        if (sku.equals(""))
            throw new IllegalSkuException();

        List<Item> itemsWithSameSku = em.createNamedQuery("findItemBySku", Item.class)
                .setParameter("SKU", sku).getResultList();

        if (itemsWithSameSku.size() > 0)
            throw new ItemAlreadyExistsException();

        Item newItem = new Item(sku, name, description, price, quantity);
        em.persist(newItem);
    }

    public void deleteItem(Item item)
    {
        // Need to find the item, make sure that we have an attached instance of it
        Item itemToRemove = em.getReference(Item.class, item.getSKU());
        em.remove(itemToRemove);
    }

    public void editItem(Item item, String newName, String newDescription, int newPrice, int newStock)
    {
        item.setName(newName);
        item.setDescription(newDescription);
        item.setPrice(newPrice);
        item.setStock(newStock);

        if (!em.contains(item))
        {
            em.merge(item);
        }
    }

    public void toggleBanUser(User user)
    {
        user.setBanned(!user.isBanned());

        if (!em.contains(user))
        {
            em.merge(user);
        }
    }
}
