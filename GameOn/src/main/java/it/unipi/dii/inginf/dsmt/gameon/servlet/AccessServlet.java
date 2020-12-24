package it.unipi.dii.inginf.dsmt.gameon.servlet;

import it.unipi.dii.inginf.dsmt.gameon.model.User;
import it.unipi.dii.inginf.dsmt.gameon.persistence.KeyValueDBDriver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

@WebServlet(name = "AccessServlet", value = "/access-servlet")
public class AccessServlet extends HttpServlet {
    private final KeyValueDBDriver keyValueDBDriver = KeyValueDBDriver.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username=request.getParameter("username");
        String password=request.getParameter("password");

        HttpSession session = request.getSession(true);
        RequestDispatcher requestDispatcher;

        // If the user has required a login operation
        if (request.getParameter("loginButton") != null)
        {
            User user = keyValueDBDriver.login(username, password);
            if (user != null)
            {
                session.setAttribute("loggedUser",user);
                requestDispatcher = request.getRequestDispatcher("ChooseGame.jsp");
                requestDispatcher.include(request, response);
            }
            else{
                out.print("Username or password wrong");
                requestDispatcher = request.getRequestDispatcher("index.jsp");
                requestDispatcher.include(request, response);
            }
        }
        else // If the user has required a register operation
        {
            if (keyValueDBDriver.isRegistered(username)) //The username is already in use
            {
                out.print("Sorry, the username is already in use!");
                requestDispatcher = request.getRequestDispatcher("index.jsp");
                requestDispatcher.include(request, response);
            }
            else
            {
                // If the username is correctly formatted
                if (Pattern.matches("^[a-zA-Z0-9_.]*$", username))
                {
                    keyValueDBDriver.register(username, password);
                    session.setAttribute("loggedUser", new User(username, password, 0, 0));
                    requestDispatcher = request.getRequestDispatcher("ChooseGame.jsp");
                    requestDispatcher.include(request, response);
                }
                else
                {
                    out.print("Username not valid! Please use alphanumeric chars, underscore and dot");
                    requestDispatcher = request.getRequestDispatcher("index.jsp");
                    requestDispatcher.include(request, response);
                }
            }
        }

        out.close();
}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
