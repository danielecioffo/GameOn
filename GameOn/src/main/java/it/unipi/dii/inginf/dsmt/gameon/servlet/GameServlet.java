package it.unipi.dii.inginf.dsmt.gameon.servlet;

import it.unipi.dii.inginf.dsmt.gameon.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GameServlet", value = "/game-servlet")
public class GameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String opponent = request.getParameter("opponent");
        int howManyMatchesServer = 0; // Value of howManyMatches on the server side

        if (session.getAttribute("gameName").equals("connectFour"))
        {
            howManyMatchesServer = (int) session.getAttribute("howManyMatchesConnectFour");
        }
        else if (session.getAttribute("gameName").equals("ticTacToe"))
        {
            howManyMatchesServer = (int) session.getAttribute("howManyMatchesTicTacToe");
        }

        if ((opponent != null) && (request.getParameter("howManyMatches") != null))
        {
            // How many matches from the client
            int howManyMatches = Integer.parseInt(request.getParameter("howManyMatches"));

            if (howManyMatches > howManyMatchesServer)
            {
                if (session.getAttribute("gameName").equals("connectFour"))
                {
                    Utils.goToPage("connectFour.jsp", request, response);
                    session.setAttribute("howManyMatchesConnectFour", howManyMatches);
                }
                else if (session.getAttribute("gameName").equals("ticTacToe"))
                {
                    Utils.goToPage("ticTacToe.jsp", request, response);
                    session.setAttribute("howManyMatchesTicTacToe", howManyMatches);
                }
            }
            else
            {
                response.sendRedirect(request.getContextPath() + "/chooseGame-servlet");
            }
        }
        else
        {
            response.sendRedirect(request.getContextPath() + "/chooseGame-servlet");
        }

        // Avoid the possibility of caching the page
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setDateHeader("Expires", -1);
        out.close();
    }
}
