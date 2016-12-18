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

    public void createNewItem(String sku, String description, int price, int quantity)
            throws ItemAlreadyExistsException, IllegalSkuException
    {
        if (sku.equals(""))
            throw new IllegalSkuException();

        List<Item> itemsWithSameSku = em.createNamedQuery("findItemBySku", Item.class)
                .setParameter("SKU", sku).getResultList();

        if (itemsWithSameSku.size() > 0)
            throw new ItemAlreadyExistsException();

        Item newItem = new Item(sku, description, price, quantity);
        em.persist(newItem);
    }
}
