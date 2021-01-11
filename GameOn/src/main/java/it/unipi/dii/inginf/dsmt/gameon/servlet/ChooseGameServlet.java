package it.unipi.dii.inginf.dsmt.gameon.servlet;
import it.unipi.dii.inginf.dsmt.gameon.listener.SessionManager;
import it.unipi.dii.inginf.dsmt.gameon.model.User;
import it.unipi.dii.inginf.dsmt.gameon.persistence.KeyValueDBDriver;
import it.unipi.dii.inginf.dsmt.gameon.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        if(request.getParameter("battleShipButton") != null){
            HttpSession session = request.getSession();
            String gameName = "battleShip";
            session.setAttribute("gameName", gameName);
        }else{
            HttpSession session = request.getSession();
            String gameName = "connectFour";
            session.setAttribute("gameName", gameName);
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
        request.setAttribute("users", list);
        if(session.getAttribute("gameName").equals("battleShip"))
            request.setAttribute("ranking", keyValueDBDriver.getBattleshipRanking(5));  // cambiare con parametro da ParametriDiConfigurazione
        else
            request.setAttribute("ranking", keyValueDBDriver.getConnectFourRanking(5)); // cambiare con parametro da ParametriDiConfigurazione

        Utils.goToPage("gameSelected.jsp", request, response);
    }
}
