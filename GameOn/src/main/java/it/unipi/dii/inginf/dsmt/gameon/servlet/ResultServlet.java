package it.unipi.dii.inginf.dsmt.gameon.servlet;

import it.unipi.dii.inginf.dsmt.gameon.model.User;
import it.unipi.dii.inginf.dsmt.gameon.persistence.KeyValueDBDriver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ResultServlet", value = "/result-servlet")
public class ResultServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        KeyValueDBDriver db = KeyValueDBDriver.getInstance();

        if (request.getParameter("hasWonConnectFour") != null)
        {
            if (request.getParameter("hasWonConnectFour").equals("true"))
                db.addUserWinConnectFour(user);

            request.setAttribute("connectFourButton", "connectFour");
            response.sendRedirect(request.getContextPath() + "/chooseGame-servlet");
        }
        else if (request.getParameter("hasWonTicTacToe") != null)
        {
            if (request.getParameter("hasWonTicTacToe").equals("true"))
                db.addUserWinTicTacToe(user);

            request.setAttribute("ticTacToeButton", "ticTacToe");
            response.sendRedirect(request.getContextPath() + "/chooseGame-servlet");
        }
    }
}
