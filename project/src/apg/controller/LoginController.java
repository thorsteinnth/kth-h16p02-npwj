package apg.controller;

import apg.exceptions.UserAlreadyExistException;
import apg.model.User;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class LoginController
{
    @PersistenceContext(unitName = "apgPU")
    private EntityManager em;

    private static final String userAlreadyExistSTR = "User already exists";


    public void createTestUser()
    {
        User user = new User("gretarg09@gmail.com", "mypassword", true, false);
        em.persist(user);
    }

    public void createUser(String email, String password) throws UserAlreadyExistException
    {

        List<User> users = em.createNamedQuery("findUserByEmail", User.class)
                .setParameter("email",email).getResultList();

        if(users.size() == 0)
        {
            User user = new User(email, password, true, false);
            em.persist(user);
        }
        else
        {
            throw new UserAlreadyExistException (userAlreadyExistSTR);
        }
    }

    public User getUser(String email, String password)
    {
        Query query = em.createNamedQuery("findUser", User.class);
        query.setParameter("email",email);
        query.setParameter("password",password);

        User user = (User)query.getSingleResult();

        return user;
    }
}
