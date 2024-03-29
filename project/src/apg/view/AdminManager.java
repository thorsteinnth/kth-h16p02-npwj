package apg.view;

import apg.controller.AdminController;
import apg.exceptions.IllegalSkuException;
import apg.exceptions.ItemAlreadyExistsException;
import apg.model.Item;
import apg.model.User;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("adminManager")
@RequestScoped
public class AdminManager implements Serializable
{
    @EJB
    AdminController controller;

    private String newItemSKU;
    private String newItemName;
    private String newItemDescription;
    private int newItemPrice;
    private int newItemStock;
    private boolean newItemIllegalSku;
    private boolean newItemItemAlreadyExists;
    private boolean newItemItemCreatedSuccess;

    private Item itemToDelete;
    private boolean itemDeleteSuccess;

    private User selectedUser;

    //region Getters and setters

    public String getNewItemSKU()
    {
        return newItemSKU;
    }

    public void setNewItemSKU(String newItemSKU)
    {
        this.newItemSKU = newItemSKU;
    }

    public String getNewItemName()
    {
        return newItemName;
    }

    public void setNewItemName(String newItemName)
    {
        this.newItemName = newItemName;
    }

    public String getNewItemDescription()
    {
        return newItemDescription;
    }

    public void setNewItemDescription(String newItemDescription)
    {
        this.newItemDescription = newItemDescription;
    }

    public int getNewItemPrice()
    {
        return newItemPrice;
    }

    public void setNewItemPrice(int newItemPrice)
    {
        this.newItemPrice = newItemPrice;
    }

    public int getNewItemStock()
    {
        return newItemStock;
    }

    public void setNewItemStock(int newItemStock)
    {
        this.newItemStock = newItemStock;
    }

    public boolean isNewItemIllegalSku()
    {
        return newItemIllegalSku;
    }

    public boolean isNewItemItemAlreadyExists()
    {
        return newItemItemAlreadyExists;
    }

    public boolean isNewItemItemCreatedSuccess()
    {
        return newItemItemCreatedSuccess;
    }

    public Item getItemToDelete()
    {
        return itemToDelete;
    }

    public void setItemToDelete(Item itemToDelete)
    {
        this.itemToDelete = itemToDelete;
    }

    public boolean isItemDeleteSuccess()
    {
        return itemDeleteSuccess;
    }

    public User getSelectedUser()
    {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser)
    {
        this.selectedUser = selectedUser;
    }

    //endregion

    //region Methods

    public void createNewItem()
    {
        this.newItemItemCreatedSuccess = false;
        this.newItemIllegalSku = false;
        this.newItemItemAlreadyExists = false;

        try
        {
            this.controller.createNewItem(newItemSKU, newItemName, newItemDescription, newItemPrice, newItemStock);
            this.newItemItemCreatedSuccess = true;
        }
        catch (IllegalSkuException ex)
        {
            this.newItemIllegalSku = true;
        }
        catch (ItemAlreadyExistsException ex)
        {
            this.newItemItemAlreadyExists = true;
        }
    }

    public void deleteItem()
    {
        this.controller.deleteItem(itemToDelete);
        this.itemDeleteSuccess = true;
    }

    public List<Item> getAllItems()
    {
        return this.controller.getAllItems();
    }

    public List<User> getAllUsers()
    {
        return this.controller.getAllUsers();
    }

    public void toggleUserBanned()
    {
        this.controller.toggleBanUser(selectedUser);
    }

    //endregion

    public void validateNumberInput(FacesContext context, UIComponent comp, Object value)
    {
        if (value instanceof Long)
        {
            Long longValue = (Long)value;

            if (longValue < 0)
            {
                FacesMessage msg = new FacesMessage("Number error", "Number should be positive");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }
}
