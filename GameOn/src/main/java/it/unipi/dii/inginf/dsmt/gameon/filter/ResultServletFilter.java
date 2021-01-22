package it.unipi.dii.inginf.dsmt.gameon.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "ResultServletFilter", servletNames = {"ResultServlet"})
public class ResultServletFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        PrintWriter out = response.getWriter();

        if ((request.getParameter("hasWonConnectFour") != null) || (request.getParameter("hasWonTicTacToe") != null))
        {
            chain.doFilter(req, resp);
        }
        else
        {
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Invalid operation');");
            out.println("document.location.href='./logout-servlet';"); // forced logout
            out.println("</script>");
        }
    }
}
