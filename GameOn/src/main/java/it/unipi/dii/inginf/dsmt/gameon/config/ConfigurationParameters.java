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

    public static ConfigurationParameters getInstance(){
        if(instance == null)
        {
            synchronized (ConfigurationParameters.class)
            {
                if(instance==null)
                {
                    instance = readConfigurationParameters();
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
                text = new String(Files.readAllBytes(Paths.get(Utils.getFileFromResource("config.xml").getPath())));
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
            Document document = documentBuilder.parse(Utils.getFileFromResource("config.xml"));
            Schema schema = schemaFactory.newSchema(Utils.getFileFromResource("config.xsd"));
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
