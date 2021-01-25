package it.unipi.dii.inginf.dsmt.gameon.filter.generic;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationFilterTest extends Mockito {

    @Test
    void doFilter() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        HttpSession session = mock(HttpSession.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        // When there is the request for the session, pass this mocked session
        when(request.getSession()).thenReturn(session);
        // Where there is the request for the write, pass this mocked writer
        when(response.getWriter()).thenReturn(writer);

        // When there is the request for the attribute "loggedUser", pass null
        // In this case the output must be the alert "Invalid operation"
        when(request.getSession().getAttribute("loggedUser")).thenReturn(null);

        AuthenticationFilter filter = new AuthenticationFilter();
        filter.doFilter(request, response, filterChain);

        assertTrue(stringWriter.toString().contains("alert"));

        // When loggedUser != null -> chain.doFilter()
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        session = mock(HttpSession.class);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession().getAttribute("loggedUser")).thenReturn("User"); // different from null
        filter = new AuthenticationFilter();
        filter.doFilter(request, response, filterChain);
        // assert that the alert will not be displayed
        assertFalse(stringWriter.toString().contains("alert"));
    }
}