package it.unipi.dii.inginf.dsmt.gameon.utils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

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

    /**
     * Function used to show an alert with some text that explains the problem
     * @param out       PrintWriter used to print on the document
     */
    public static void printErrorAlertAccessDenied (PrintWriter out)
    {
        out.println("<script type=\"text/javascript\">");
        out.println("alert('Invalid operation!');");
        out.println("document.location.href='./logout-servlet';"); // forced logout
        out.println("</script>");
    }

    /**
     * Function that sorts an HashMap on descending order on values of Integer types
     * @param hashMap   HashMap to be sorted in descending order on values
     * @param limit number of records to be returned
     * @return  a sorted HashMap
     */
    public static HashMap<String, Integer> sortHashMap(HashMap<String, Integer> hashMap, int limit) {
        if (hashMap == null)
        {
            return null;
        }

        Comparator<Map.Entry<String, Integer>> valueComparator = (o1, o2) -> {
            Integer i1 = o1.getValue();
            Integer i2 = o2.getValue();
            return i2.compareTo(i1);
        };

        Set<Map.Entry<String, Integer>> entries = hashMap.entrySet();

        // Sort method needs a List, so let's first convert Set to List in Java
        List<Map.Entry<String, Integer>> listOfEntries = new ArrayList<>(entries);

        // sorting HashMap by values using comparator
        listOfEntries.sort(valueComparator);
        HashMap<String, Integer> sortedByValue = new LinkedHashMap<>(listOfEntries.size());

        // copying entries from List to Map with limited records
        int i = 0;
        for (Map.Entry<String, Integer> entry : listOfEntries) {
            if(i >= limit) break;
            sortedByValue.put(entry.getKey(), entry.getValue());
            i++;
        }

        return sortedByValue;
    }
}
