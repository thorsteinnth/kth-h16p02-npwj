package converter.controller;

import converter.model.Currency;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 * A controller. All calls to the model that are executed because of an action taken by
 * the cashier pass through here.
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class ConverterController {
    @PersistenceContext(unitName = "converterPU")
    private EntityManager em;

    public void createCurrency() {
        Currency EURCurrency = new Currency("EUR");
        Currency USDCurrency = new Currency("USD");
        Currency ISKCurrency = new Currency("ISK");
        Currency SEKRCurrency = new Currency("SEK");

        em.persist(EURCurrency);
        em.persist(USDCurrency);
        em.persist(ISKCurrency);
        em.persist(SEKRCurrency);
    }
}

