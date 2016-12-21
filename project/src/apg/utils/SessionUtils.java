package apg.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// Adapted from: http://www.journaldev.com/7252/jsf-authentication-login-logout-database-example
public class SessionUtils
{
    public static final String unknownUser = "unknownuser";

    public static HttpSession getSession()
    {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    public static HttpServletRequest getRequest()
    {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public static String getUsername()
    {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        // if a user is logged in then his username is retrieved from the session
        // if the user is not logged in then unknownuser is returned;

        if (session.getAttribute("username") == null || session.getAttribute("username") == "") {
            return unknownUser;
        } else {
            return session.getAttribute("username").toString();
        }
    }

    public static String getUserId()
    {
        HttpSession session = getSession();
        if (session != null)
            return (String) session.getAttribute("userid");
        else
            return null;
    }
}
