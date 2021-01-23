package it.unipi.dii.inginf.dsmt.gameon.filter;

import it.unipi.dii.inginf.dsmt.gameon.utils.Utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "GameServletFilter", servletNames = {"GameServlet"})
public class GameServletFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        if ((session.getAttribute("gameName") != null) && (session.getAttribute("loggedUser") != null)
                && (session.getAttribute("howManyMatchesConnectFour") != null)
                && (session.getAttribute("howManyMatchesTicTacToe") != null))
        {
            chain.doFilter(req, resp);
        }
        else {
            Utils.printErrorAlertAccessDenied(out);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }
}
