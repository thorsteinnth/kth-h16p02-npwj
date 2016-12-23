package apg.utils;

import apg.model.Item;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ManagedBean
@ApplicationScoped
public class ItemConverter implements Converter
{
    // Adapted from: http://stackoverflow.com/a/21229935

    @PersistenceContext(unitName = "apgPU")
    private EntityManager em;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value == null || value.isEmpty())
        {
            return null;
        }

        // TODO Handle if I can't find the item

        Item foundItem = em.find(Item.class, value);
        return foundItem;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value == null)
        {
            return "";
        }

        if (!(value instanceof Item))
        {
            throw new ConverterException("The value is not a valid Item instance: " + value);
        }

        String sku = ((Item)value).getSKU();
        return sku;
    }
}
