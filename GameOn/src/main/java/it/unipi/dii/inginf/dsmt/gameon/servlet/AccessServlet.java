package it.unipi.dii.inginf.dsmt.gameon.servlet;

import it.unipi.dii.inginf.dsmt.gameon.model.User;
import it.unipi.dii.inginf.dsmt.gameon.persistence.UsersDBDriver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", value = "/access-servlet")
public class AccessServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username=request.getParameter("username");
        String password=request.getParameter("password");

        // If the user has required a login operation
        if (request.getParameter("loginButton") != null)
        {
            User user = UsersDBDriver.login(username, password);
            if (user != null)
            {
                out.println("<html><body>");
                out.println("<h1>" + username + " " + password + " correctly logged" + "</h1>");
                out.println("</body></html>");
            }
            else{
                out.print("Sorry username or password error");
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
                requestDispatcher.include(request, response);
            }
        }
        else // If the user has required a register operation
        {
            RequestDispatcher requestDispatcher;
            if (UsersDBDriver.isRegistered(username)) //The username is already in use
            {
                out.print("Sorry, the username is already in use!");
                requestDispatcher = request.getRequestDispatcher("index.jsp");
                requestDispatcher.include(request, response);
            }
            else
            {
                UsersDBDriver.register(username, password);
                out.println("<html><body>");
                out.println("<h1>" + username + " " + password + " correctly logged" + "</h1>");
                out.println("</body></html>");
            }
        }

        out.close();
}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
