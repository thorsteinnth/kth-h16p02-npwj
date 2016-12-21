package apg.controller;


import apg.exceptions.NotEnoughStockException;
import apg.model.Item;
import apg.model.ShoppingCartItem;
import apg.model.User;
import apg.utils.SessionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import javax.transaction.UserTransaction;

@Stateful
@TransactionManagement(value=TransactionManagementType.BEAN)
public class ShoppingCardController2 {

    User user;
    // I create cache of the items to avoid accessing the database to frequently
    List<ShoppingCartItem> shoppingCartItems;

    @PersistenceContext(unitName = "apgPU")
    private EntityManager em;

    @Inject
    private UserTransaction userTransaction;

    public List<ShoppingCartItem> getAllShoppingCartItemsForUser()
    {
        Query query;

        if(shoppingCartItems == null)
        {
            query = em.createNamedQuery("findScItemsByEmail",ShoppingCartItem.class);
            query.setParameter("email", user.getEmail());
            this.shoppingCartItems = query.getResultList();
        }

        return this.shoppingCartItems;
    }

    public void refreshShoppingCartItems()
    {
        Query query = em.createNamedQuery("findScItemsByEmail",ShoppingCartItem.class);
        query.setParameter("email", user.getEmail());
        this.shoppingCartItems = query.getResultList();
    }


    public User getUser(String email)
    {
        Query query = em.createNamedQuery("findUserWithoutPassword", User.class);
        query.setParameter("email",email);
        User user = (User)query.getSingleResult();

        return user;
    }

    public String getEmail()
    {
        return user.getEmail();
    }

    @PostConstruct
    public void init()
    {
        if(user == null)
        {
            user = getUser(SessionUtils.getUsername());
        }
    }

    public void increaseQuantity(ShoppingCartItem shoppingCartItem) throws Exception
    {
        try
        {
            userTransaction.begin();
            em.joinTransaction();

            ShoppingCartItem scItem = em.find(ShoppingCartItem.class, shoppingCartItem.getId());
            scItem.increaseQuantity();

            //we need to update the cache
            int index = 0;
            for (ShoppingCartItem item : shoppingCartItems) {
                if (item.getId() == shoppingCartItem.getId())
                    break;
                index++;
            }

            //shoppingCartItems.get(index).increaseQuantity();
            //ShoppingCartItem manageItem = shoppingCartItems.get(index);
            //boolean isManage = em.contains(manageItem);
            //boolean isManage2 = em.contains(shoppingCartItems.get(index));

            //em.flush();

            shoppingCartItems.set(index, scItem);

            userTransaction.commit();
        }
        catch (Exception e)
        {
            userTransaction.rollback();
            throw e;
        }
    }

    public void decreaseQuantity(ShoppingCartItem shoppingCartItem) throws Exception
    {
        try {
            userTransaction.begin();
            em.joinTransaction();

            ShoppingCartItem scItem = em.find(ShoppingCartItem.class, shoppingCartItem.getId());
            scItem.decreaseQuantity();

            //we need to update the cache
            int index = 0;
            for (ShoppingCartItem item : shoppingCartItems)
            {
                if (item.getId() == shoppingCartItem.getId())
                    break;
                index++;
            }

            //shoppingCartItems.get(index).increaseQuantity();
            //em.flush();

            // If the item is zero then we drop it out of the basket
            if (scItem.getQuantity() == 0)
            {
                em.remove(scItem);
                shoppingCartItems.remove(index);
            }
            else
            {
                shoppingCartItems.set(index, scItem);
            }

            userTransaction.commit();
        }
        catch (Exception e)
        {
            userTransaction.rollback();
            throw e;
        }
    }

    public boolean buy() throws NotEnoughStockException, Exception
    {
        //TODO skoða synch vandamál

        if(checkInventory())
        {
            try
            {
                userTransaction.begin();
                em.joinTransaction();

                for (ShoppingCartItem scItem : shoppingCartItems) {
                    Item item = em.find(Item.class, scItem.getItem().getSKU());

                    //hérna þurfum við eitthvað transaction til þess að geta rollbackað ef ekki allt heppnast

                    item.withdraw(scItem.getQuantity());
                    removeShoppingCartItemFromDatabase(scItem);
                }
                userTransaction.commit();
            }
            catch (NotEnoughStockException notEnoughStockException)
            {
                userTransaction.rollback();
                throw notEnoughStockException;
            }
            catch (Exception e)
            {
                userTransaction.rollback();
                throw e;
            }

            refreshShoppingCartItems();
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean checkInventory()
    {
        boolean success = true;

        for (ShoppingCartItem scItem : shoppingCartItems) {

            //Query query = em.createNamedQuery("findItemBySku", Item.class);
            //query.setParameter("SKU", scItem.getItem().getSKU());
            //TODO handle if item is not available anymore.
            //Item item  = (Item) query.getSingleResult();

            Item item = em.find(Item.class, scItem.getItem().getSKU());

            if (item == null) {
                //This should never happen
                removeShoppingCartItemFromDatabase(scItem);
                refreshShoppingCartItems();
                success = false;
            }
            else if (item.getStock() < scItem.getQuantity())
            {
                int amountToDecreaseQuantityBy = scItem.getQuantity() - item.getStock();
                ShoppingCartItem shoppingCartItem = em.find(ShoppingCartItem.class, scItem.getId());
                shoppingCartItem.decreaseQuantity(amountToDecreaseQuantityBy);

                // check if quantity is zero or less. This should never happen but if it does then we remove the shoppinglistItem
                if(shoppingCartItem.getQuantity() < 1)
                {
                    removeShoppingCartItemFromDatabase(shoppingCartItem);
                }

                success = false;
            }
        }

        if(success == false)
        {
            // refresh the shoppingCardItems after modifying
            refreshShoppingCartItems();
        }

        return success;
    }

    private void removeShoppingCartItemFromDatabase(ShoppingCartItem shoppingCartItem)
    {
        ShoppingCartItem scItem = em.find(ShoppingCartItem.class,shoppingCartItem.getId());
        em.remove(scItem);
    }
}