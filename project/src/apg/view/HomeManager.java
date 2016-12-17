package apg.view;


import apg.model.Item;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("homeManager")
@SessionScoped
public class HomeManager implements Serializable{

    private List<Item> items;






    //region ########## Getter and Setter ##########
    public List<Item> getItems() {

        items = new ArrayList<Item>();
        items.add(new Item("Þorsteinn","small with small hat",10));
        items.add(new Item("Stinni","big with big hat",10));

        if(items == null)
        {
            //TODO fara í controller og ná í items
        }
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    //endregion
}
