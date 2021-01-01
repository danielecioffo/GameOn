package it.unipi.dii.inginf.dsmt.gameon.servlet;
import it.unipi.dii.inginf.dsmt.gameon.HelloServlet;
import it.unipi.dii.inginf.dsmt.gameon.listener.SessionManager;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet(name = "ChooseGameServlet", value = "/chooseGame-servlet")
public class ChooseGameServlet extends HttpServlet{
    private final KeyValueDBDriver keyValueDBDriver = KeyValueDBDriver.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher;
        PrintWriter out = response.getWriter();

        if(request.getParameter("battleShipButton") != null){
            HttpSession session = request.getSession();
            String gameName = "battleShip";
            session.setAttribute("gameName", gameName);
            requestDispatcher = request.getRequestDispatcher("gameSelected.jsp");
            requestDispatcher.include(request, response);
        }else{
            HttpSession session = request.getSession();
            String gameName = "connectFour";
            session.setAttribute("gameName", gameName);
            requestDispatcher = request.getRequestDispatcher("gameSelected.jsp");
            requestDispatcher.include(request, response);
        }
        HttpSession session = request.getSession();
        SessionManager sessionManager =
                (SessionManager) session.getServletContext().getAttribute("sessionManager");
        List<User> users;
        System.out.println(session.getAttribute("gameName"));
        if(session.getAttribute("gameName").equals("battleShip"))
            users = sessionManager.getOnlineUsersBattleShip();
        else
            users = sessionManager.getOnlineUsersConnectFour();
        System.out.println(users);
        List<String> list = new ArrayList<>();
        for (User k: users
             ) {
            list.add(k.getUsername());
        }
        request.setAttribute("usrs", list);
        if(session.getAttribute("gameName").equals("battleShip"))
            request.setAttribute("ranking", keyValueDBDriver.getBattleshipRanking(5));  // cambiare con parametro da ParametriDiConfigurazione
        else
            request.setAttribute("ranking", keyValueDBDriver.getConnectFourRanking(5)); // cambiare con parametro da ParametriDiConfigurazione
        request.getRequestDispatcher("/gameSelected.jsp").forward(request, response);
    }
}
