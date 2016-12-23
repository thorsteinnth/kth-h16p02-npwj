package apg.view;

import apg.controller.AdminController;
import apg.model.Item;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("adminManagerEditItem")
@RequestScoped
public class AdminManagerEditItem implements Serializable
{
    /*
    * We have the edit item functionality in its own managed bean
    * The create new item functionality however is in the main admin managed bean
    * */

    @EJB
    AdminController controller;

    private Item itemToEdit;

    private String sku;
    private String description;
    private int price;
    private int stock;
    private boolean illegalSku;
    private boolean itemAlreadyExists;
    private boolean editItemSuccess;

    public Item getItemToEdit()
    {
        return itemToEdit;
    }

    public void setItemToEdit(Item itemToEdit)
    {
        this.itemToEdit = itemToEdit;
        this.sku = itemToEdit.getSKU();
        this.description = itemToEdit.getDescription();
        this.price = itemToEdit.getPrice();
        this.stock = itemToEdit.getStock();
    }

    public String getSku()
    {
        return sku;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
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

    public boolean isIllegalSku()
    {
        return illegalSku;
    }

    public boolean isItemAlreadyExists()
    {
        return itemAlreadyExists;
    }

    public boolean isEditItemSuccess()
    {
        return editItemSuccess;
    }

    public void save()
    {
        this.controller.editItem(this.itemToEdit, this.description, this.price, this.stock);
    }
}
