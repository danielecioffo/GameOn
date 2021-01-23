package it.unipi.dii.inginf.dsmt.gameon.filter.servlet;

import it.unipi.dii.inginf.dsmt.gameon.utils.Utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "ResultServletFilter", servletNames = {"ResultServlet"})
public class ResultServletFilter implements Filter {

    public void destroy() {
    }

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
            Utils.printErrorAlertAccessDenied(out);
        }

        out.close();
    }

    public void init(FilterConfig config) throws ServletException {

    }
}
