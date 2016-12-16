package apg.controller;

import apg.model.User;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class LoginController
{
    @PersistenceContext(unitName = "apgPU")
    private EntityManager em;

    public void createTestUser()
    {
        User user = new User("gretarg09@gmail.com", "mypassword", true, false);
        em.persist(user);
    }
}
