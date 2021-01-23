package it.unipi.dii.inginf.dsmt.gameon.filter.servlet;

import it.unipi.dii.inginf.dsmt.gameon.utils.Utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "AccessServletFilter", servletNames = {"AccessServlet"})
public class AccessServletFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        if ((request.getParameter("username") != null) && (request.getParameter("password") != null) &&
                ((request.getParameter("loginButton") != null) || (request.getParameter("registerButton") != null)))
        {
            // Only the first time i need to do the login process
            chain.doFilter(req, resp);
        }
        else if (session.getAttribute("loggedUser") != null)
        {
            Utils.goToPage("chooseGame.jsp", request, response);
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
