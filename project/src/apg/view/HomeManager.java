package apg.view;


import apg.controller.HomeController;
import apg.model.Item;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("homeManager")
@SessionScoped
public class HomeManager implements Serializable{

    @EJB
    HomeController homeController;

    private List<Item> items;
    private Item item;

    //region ########## Getter and Setter ##########
    public List<Item> getItems() {

        /*
        items = new ArrayList<Item>();
        items.add(new Item("Þorsteinn","small with small hat",10));
        items.add(new Item("Stinni","big with big hat",10));
        */

        if(items == null)
        {
            try
            {
                items = homeController.getAllItems();
            }
            catch (Exception e)
            {
                System.err.println(e);
            }
            //TODO fara í controller og ná í items
        }
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    //endregion

    public void plusOnClickEventHandler()
    {
        System.out.println("yeah I am here");
    }
}
