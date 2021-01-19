package it.unipi.dii.inginf.dsmt.gameon.utils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Class with utility functions
 */
public class Utils {

    public static void goToPage (String page, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(page);
        if (requestDispatcher != null)
        {
            requestDispatcher.include(request, response);
        }
    }

    /**
     * Function that returns a file form the resources folder
     * @param fileName              The name of the file, or path inside the resources folder
     * @return                      The file
     * @throws URISyntaxException   Syntactic error of the URI
     */
    public static File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }
}
