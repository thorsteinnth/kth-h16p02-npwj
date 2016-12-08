package converter.view;

import converter.model.Currency;


/*
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("se.gretarttsi.CurrencyUIConverter")
public class CurrencyUIConverter implements Converter
{
    // NOTE: Couldn't get this to work. It is called but we don't see it in the UI.
    // Not in use.

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
    {
        // Just returning a new currency here. Should ideally get it from the entity manager though.
        return new Currency(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
    {
        if (o instanceof Currency)
        {
            return ((Currency)o).getCode();
        }
        else
        {
            return null;
        }
    }
}
*/