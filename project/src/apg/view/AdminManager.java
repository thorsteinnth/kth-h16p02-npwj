package apg.view;

import apg.controller.AdminController;
import apg.model.Item;
import apg.model.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("adminManager")
@SessionScoped
public class AdminManager implements Serializable
{
    @EJB
    AdminController controller;

    public List<Item> getAllItems()
    {
        return this.controller.getAllItems();
    }

    public List<User> getAllUsers()
    {
        return this.controller.getAllUsers();
    }
}
