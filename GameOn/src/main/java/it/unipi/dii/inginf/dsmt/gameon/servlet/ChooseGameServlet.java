package it.unipi.dii.inginf.dsmt.gameon.servlet;
import it.unipi.dii.inginf.dsmt.gameon.config.ConfigurationParameters;
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

@WebServlet(name = "ChooseGameServlet", value = "/chooseGame-servlet")
public class ChooseGameServlet extends HttpServlet{

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("ticTacToeButton") != null){
            HttpSession session = request.getSession();
            String gameName = "ticTacToe";
            session.setAttribute("gameName", gameName);
        }else if (request.getParameter("connectFourButton") != null){
            HttpSession session = request.getSession();
            String gameName = "connectFour";
            session.setAttribute("gameName", gameName);
        }

        KeyValueDBDriver keyValueDBDriver = KeyValueDBDriver.getInstance();
        ConfigurationParameters configurationParameters = ConfigurationParameters.getInstance();
        HttpSession session = request.getSession();

        if(session.getAttribute("gameName").equals("ticTacToe"))
            request.setAttribute("ranking", keyValueDBDriver.
                    getTicTacToeRanking(configurationParameters.getHowManyUsersToSeeInTheRanking()));
        else
            request.setAttribute("ranking", keyValueDBDriver.
                    getConnectFourRanking(configurationParameters.getHowManyUsersToSeeInTheRanking()));

        Utils.goToPage("gameSelected.jsp", request, response);
    }
}
