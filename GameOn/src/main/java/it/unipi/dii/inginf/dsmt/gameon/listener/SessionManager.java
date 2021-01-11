package it.unipi.dii.inginf.dsmt.gameon.listener;

import it.unipi.dii.inginf.dsmt.gameon.model.User;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.*;

@WebListener()
/*
  Listener to handle the sessions
 */
public class SessionManager implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    // In case of unordered elements, a set is more efficient than a list
    // Note: not all the session are session of logged users!
    // At the creation, the user is not already logged in, this will happen later during the login procedure
    private final Set<HttpSession> activeSessions;
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
     * Function that returns a list of all online users
     * @return      A list of users
     */
    public List<User> getAllOnlineUsers() {
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

    /**
     * Function that returns the list of the online users waiting for a Connected Four game
     * @return  The list of the users
     */
    public List<User> getOnlineUsersConnectFour ()
    {
        return getOnlineUsers("connectFour");
    }

    /**
     * Function that returns the list of the online users waiting for a Battleship game
     * @return  The list of the users
     */
    public List<User> getOnlineUsersBattleShip ()
    {
        return getOnlineUsers("battleShip");
    }

    /**
     * Function that returns a list of the users online for a specific game
     * @param gameName      Name of the game
     * @return              List of the users
     */
    private List<User> getOnlineUsers (String gameName)
    {
        synchronized (activeSessions)
        {
            List<User> users = new ArrayList<>();
            Iterator<HttpSession> iterator = activeSessions.iterator();
            while (iterator.hasNext())
            {
                HttpSession session = iterator.next();
                User user = (User) session.getAttribute("loggedUser");
                String name = (String) session.getAttribute("gameName");
                // If this is a session of a logged user and he is in the waiting room of this game
                if (user != null && gameName.equals(name))
                {
                    users.add(user);
                }
            }
            return users;
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
