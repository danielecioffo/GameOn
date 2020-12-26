package it.unipi.dii.inginf.dsmt.gameon.listener;

import it.unipi.dii.inginf.dsmt.gameon.model.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.*;
import java.util.function.Consumer;

@WebListener()
/**
 * Listener for handle the sessions
 */
public class SessionManager implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    // In case of unordered elements, a set is more efficient than a list
    // Note: not all the session are session of logged users!
    // At the creation, the user is not already logged in, this will happen later during the login procedure
    private Set<HttpSession> activeSessions;
    // Key for this object in the servlet context
    private final String KEY = "sessionManager";

    /**
     * Public constructor is required by servlet spec
     */
    public SessionManager() {
        activeSessions = Collections.synchronizedSet(new HashSet<>());
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /*
        This method is called when the servlet context is
         initialized(when the Web application is deployed).
      */
        // Registration in the servlet context
        sce.getServletContext().setAttribute(KEY, this);
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /*
         This method is invoked when the Servlet Context
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
        if (sce.getServletContext().getAttribute(KEY) != null)
            sce.getServletContext().removeAttribute(KEY);
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------

    /**
     * Function called after the creation of one session
     * At this time, the user is not already logged in
     * @param se    The HttpSessionEvent
     */
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        synchronized (activeSessions) {
            activeSessions.add(session);
        }
    }

    /**
     * Function called after a session has been destroyed
     * @param se    The HttpSessionEvent
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        synchronized (activeSessions) {
            activeSessions.remove(session);
        }
    }

    /**
     * Function that returns the number of active sessions (not active users!)
     * @return  Number of active sessions
     */
    public int getActiveSessionCount() {
        return activeSessions.size();
    }

    /**
     * Function that returns the list of online users
     * @return      A list of users
     */
    public List<User> getOnlineUsers() {
        System.out.println(activeSessions);
        synchronized (activeSessions)
        {
            List<User> onlineUsers = new ArrayList();
            Iterator<HttpSession> iterator = activeSessions.iterator();
            while (iterator.hasNext())
            {
                HttpSession session = iterator.next();
                User user = (User) session.getAttribute("loggedUser");
                if (user != null) // If this is a session of a logged user
                {
                    onlineUsers.add(user);
                }
            }
            return onlineUsers;
        }
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attribute
         is replaced in a session.
      */
    }
}
