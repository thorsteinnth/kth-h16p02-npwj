package apg.view;

import apg.controller.AdminController;
import apg.model.Item;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;

@ManagedBean(name = "adminManagerEditItem")
@ViewScoped
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
    private String name;
    private String description;
    private int price;
    private int stock;
    private boolean editItemSuccess;

    public Item getItemToEdit()
    {
        return itemToEdit;
    }

    public void setItemToEdit(Item itemToEdit)
    {
        this.itemToEdit = itemToEdit;
        this.sku = itemToEdit.getSKU();
        this.name = itemToEdit.getName();
        this.description = itemToEdit.getDescription();
        this.price = itemToEdit.getPrice();
        this.stock = itemToEdit.getStock();
    }

    public String getSku()
    {
        return sku;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public boolean isEditItemSuccess()
    {
        return editItemSuccess;
    }

    public void save()
    {
        this.editItemSuccess = false;
        this.controller.editItem(this.itemToEdit, this.name, this.description, this.price, this.stock);
        this.editItemSuccess = true;
    }

    public void validateParameters()
    {
        // Verify that we have the URL request parameter

        FacesContext context = FacesContext.getCurrentInstance();
        String parameter = context.getExternalContext().getRequestParameterMap().get("editItemSku");

        if (parameter == null || parameter.equals(""))
        {
            context.getExternalContext().setResponseStatus(404);
            context.responseComplete();
        }
    }

    public void validateObject(FacesContext context, UIComponent component, Object value)
    {
        // Gets called before serving page. Validate the object that will be displayed.
        // (URL request parameter -> converter -> object value)
        // If the object is not valid, reply with 404 not found

        if (value == null || !(value instanceof Item))
        {
            context.getExternalContext().setResponseStatus(404);
            context.responseComplete();
        }
    }

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
