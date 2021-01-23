package it.unipi.dii.inginf.dsmt.gameon.filter.generic;

import it.unipi.dii.inginf.dsmt.gameon.utils.Utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "DenyAccessFilter", urlPatterns = {"/ticTacToe.jsp", "/connectFour.jsp", "/gameSelected.jsp"})
public class DenyAccessFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        Utils.printErrorAlertAccessDenied(resp.getWriter());
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
