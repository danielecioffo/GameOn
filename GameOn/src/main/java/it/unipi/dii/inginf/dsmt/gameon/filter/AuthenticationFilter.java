package it.unipi.dii.inginf.dsmt.gameon.filter;

import it.unipi.dii.inginf.dsmt.gameon.utils.Utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Filter used to avoid access if the user is not logged in
 */
@WebFilter(filterName = "AuthenticationFilter",
        servletNames = {"ChooseGameServlet", "GameServlet",
            "ResultServlet"},
        urlPatterns = {"/chooseGame.jsp", "/gameSelected.jsp", "/connectFour.jsp", "/ticTacToe.jsp"})
public class AuthenticationFilter implements Filter {
    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        if (session.getAttribute("loggedUser") != null)
            chain.doFilter(req, resp);
        else
        {
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Invalid operation');");
            out.println("document.location.href='./logout-servlet';"); // forced logout
            out.println("</script>");
        }
    }

    public void init(FilterConfig config) throws ServletException {}
}
