package apg.view;

import apg.controller.LoginController;
import apg.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named("loginManager")
@RequestScoped
public class LoginManager
{
    @EJB
    LoginController loginController;

    private String email;
    private String password;

    private String showErrorEmail;
    private String emailErrorMsg;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static final String userOrPasswordIncorrectErrorMSG = "Either username or password is incorrect";
    public static final String emailSyntexError = "Email syntex Error";

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

    public String getShowErrorEmail() {
        return showErrorEmail;
    }

    public void setShowErrorEmail(String showErrorEmail) {
        this.showErrorEmail = showErrorEmail;
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
        if(isEmailValid())
        {
            try
            {
                User user = loginController.getUser("bla", "bla");
                return "success";
            }
            catch (javax.ejb.EJBException ejbException)
            {
                if(ejbException.getCausedByException() instanceof javax.persistence.NoResultException)
                {
                    setShowErrorEmail("syntexError");
                    setEmailErrorMsg(userOrPasswordIncorrectErrorMSG);
                }
                else
                {
                    throw ejbException;
                }
                return "failure";
            }
            catch (Exception e)
            {
                System.err.println(e);
                return "failure";
            }
        }
        else
        {
            setShowErrorEmail("syntexError");
            setEmailErrorMsg(emailSyntexError);
            return "failure";
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
