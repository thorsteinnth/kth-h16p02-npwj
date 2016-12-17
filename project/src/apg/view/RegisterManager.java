package apg.view;

import apg.controller.LoginController;
import apg.exceptions.UserAlreadyExistException;
import apg.model.User;
import apg.utils.SessionUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named("registerManager")
@RequestScoped
public class RegisterManager
{
    @EJB
    LoginController loginController;

    private String email;
    private String password;

    private String showErrorEmail;
    private String showErrorPassword;

    private final int minPasswordLength = 8;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //region ########## getters and setters ##########

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

    public String getShowErrorPassword() {
        return showErrorPassword;
    }

    public void setShowErrorPassword(String showErrorPassword) {
        this.showErrorPassword = showErrorPassword;
    }

    //endregion

    //region ########## Action Handlers ##########

    public String register()
    {
        if (isEmailValid() && isPasswordValid())
        {
            try
            {
                User user = loginController.createUser(email, password);
                HttpSession session = SessionUtils.getSession();
                session.setAttribute("username", user.getEmail());
                return "registration-success";
            }
            catch (UserAlreadyExistException userAlreadyExistException)
            {
                //TODO handle that shit
                return "registration-failure";
            }
            catch (Exception e)
            {
                System.err.println(e);
                return "registration-failure";
            }
        }
        else
        {
            if (!isEmailValid())
            {
                setShowErrorEmail("syntexError");
            }

            if (!isPasswordValid())
            {
                setShowErrorPassword("syntexError");
            }

            return "registration-failure";
        }
    }

    //endregion

    //region ########## Functions ##########

    private boolean isEmailValid()
    {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private boolean isPasswordValid()
    {
        if (password.length() < this.minPasswordLength)
        {
            return false;
        }

        return true;
    }

    //endregion
}

