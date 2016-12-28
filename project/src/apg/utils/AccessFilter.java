package apg.utils;

import apg.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AccessFilter implements Filter
{
    // Adapted from:
    // http://stackoverflow.com/a/14582031
    // http://balusc.omnifaces.org/2007/03/user-session-filter.html

    @PersistenceContext(unitName = "apgPU")
    private EntityManager em;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        boolean adminRequest = request.getRequestURI().startsWith(request.getContextPath() + "/admin/");

        if (adminRequest)
        {
            boolean loggedIn = session != null
                    && session.getAttribute("username") != null
                    && !session.getAttribute("username").equals("");
            boolean userIsAdmin = false;

            if (loggedIn)
            {
                String username = (String)session.getAttribute("username");
                User user = em.find(User.class, username);
                if (user != null)
                    userIsAdmin = user.isAdmin();
            }

            if (!loggedIn || !userIsAdmin)
            {
                // Deny access - 401 Unauthorized
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Admin privileges required");
            }
            else
            {
                // Everything is in order. Allow request through.

                // Prevent browser from caching restricted resources. See also http://stackoverflow.com/q/4194207/157882
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                response.setDateHeader("Expires", 0); // Proxies.

                // Continue request
                chain.doFilter(request, response);
            }
        }
        else
        {
            // Continue request
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {}

    @Override
    public void destroy()
    {}
}
