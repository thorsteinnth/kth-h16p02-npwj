package apg.view;

import apg.controller.LoginController;
import apg.model.User;
import apg.utils.SessionUtils;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named("loginManager")
@RequestScoped
public class LoginManager implements Serializable
{
    @EJB
    LoginController loginController;

    private String email;
    private String password;

    private Boolean showEmailError;
    private String emailErrorMsg;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final String userOrPasswordIncorrectErrorMSG = "Either username or password is incorrect";
    public static final String emailInvalidError = "Email invalid";

    //region ########## Getter and Setter ##########

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getShowEmailError()
    {
        return showEmailError;
    }

    public void setShowEmailError(Boolean showEmailError)
    {
        this.showEmailError = showEmailError;
    }

    public String getEmailErrorMsg() {
        return emailErrorMsg;
    }

    public void setEmailErrorMsg(String emailErrorMsg) {
        this.emailErrorMsg = emailErrorMsg;
    }

    //endregion

    //region ########## Action Handler ##########

    public void test()
    {
        try
        {
            User user = loginController.getUser("bla", "bla");
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }

    public String login()
    {
        if (isEmailValid())
        {
            try
            {
                User user = loginController.getUser(email, password);
                HttpSession session = SessionUtils.getSession();
                session.setAttribute("username", user.getEmail());
                return "login-success";
            }
            catch (javax.ejb.EJBException ejbException)
            {
                if (ejbException.getCausedByException() instanceof javax.persistence.NoResultException)
                {
                    setShowEmailError(true);
                    setEmailErrorMsg(userOrPasswordIncorrectErrorMSG);
                }
                else
                {
                    throw ejbException;
                }

                return "login-failure";
            }
            catch (Exception e)
            {
                System.err.println(e);
                return "login-failure";
            }
        }
        else
        {
            setShowEmailError(true);
            setEmailErrorMsg(emailInvalidError);
            return "login-failure";
        }
    }

    //endregion

    //region ########## Functions ##########

    private boolean isEmailValid()
    {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }
    
    //endregion
}
