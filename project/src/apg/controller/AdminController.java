package apg.controller;

import apg.model.Item;
import apg.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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
}