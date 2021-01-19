package it.unipi.dii.inginf.dsmt.gameon.config;

import com.thoughtworks.xstream.XStream;
import it.unipi.dii.inginf.dsmt.gameon.utils.Utils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class used to store the configuration parameters retrieved from the config.xml
 * There is no need to modify this value, so there are only getters methods
 */
public class ConfigurationParameters {
    public static volatile ConfigurationParameters instance;
    private String pathDatabase;
    private int howManySecondsForEachTurn;
    private int howManySkippedRoundsToStopTheGame;
    private int howManyUsersToSeeInTheRanking;

    private ConfigurationParameters (){
        if (validConfigurationParameters())
        {
            XStream xs = new XStream();

            String text = null;
            try {
                text = new String(Files.readAllBytes(Paths.get(getFileFromResource("config.xml").getPath())));
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }

            instance = (ConfigurationParameters) xs.fromXML(text);
        }
        else
        {
            System.exit(1); //If i can't read the configuration file I can't continue with the program
        }

        // Non sono sicurissimo di questi assegnamenti qui sotto
        pathDatabase = instance.pathDatabase;
        howManySecondsForEachTurn = instance.howManySecondsForEachTurn;
        howManySkippedRoundsToStopTheGame = instance.howManySkippedRoundsToStopTheGame;
        howManyUsersToSeeInTheRanking = instance.howManyUsersToSeeInTheRanking;
   }

   public static ConfigurationParameters getInstance(){
        if(instance == null)
        {
            synchronized (ConfigurationParameters.class)
            {
                if(instance==null)
                {
                    instance = new ConfigurationParameters();
                }
            }
        }
        return instance;
   }

    /**
     * This function is used to read the config.xml file
     * @return  ConfigurationParameters instance
     */
    private static ConfigurationParameters readConfigurationParameters ()
    {
        if (validConfigurationParameters())
        {
            XStream xs = new XStream();

            String text = null;
            try {
                text = new String(Files.readAllBytes(Paths.get(getFileFromResource("config.xml").getPath())));
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }

            return (ConfigurationParameters) xs.fromXML(text);
        }
        else
        {
            System.exit(1); //If i can't read the configuration file I can't continue with the program
        }
        return null;
    }

    /**
     * This function is used to validate the config.xml with the config.xsd
     * @return  true if config.xml is well formatted, otherwise false
     */
    private static boolean validConfigurationParameters()
    {
        try
        {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document document = documentBuilder.parse(getFileFromResource("config.xml"));
            Schema schema = schemaFactory.newSchema(getFileFromResource("config.xsd"));
            schema.newValidator().validate(new DOMSource(document));
        }
        catch (Exception e)
        {
            if (e instanceof SAXException)
                System.out.println("Validation Error: " + e.getMessage());
            else
                System.out.println(e.getMessage());

            return false;
        }
        return true;
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

    public String getPathDatabase() {
        return pathDatabase;
    }

    public int getHowManySecondsForEachTurn() {
        return howManySecondsForEachTurn;
    }

    public int getHowManySkippedRoundsToStopTheGame() {
        return howManySkippedRoundsToStopTheGame;
    }

    public int getHowManyUsersToSeeInTheRanking() {
        return howManyUsersToSeeInTheRanking;
    }
}
