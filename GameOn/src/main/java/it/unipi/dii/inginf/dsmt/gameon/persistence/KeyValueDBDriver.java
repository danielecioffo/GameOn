package it.unipi.dii.inginf.dsmt.gameon.persistence;

import it.unipi.dii.inginf.dsmt.gameon.config.ConfigurationParameters;
import it.unipi.dii.inginf.dsmt.gameon.model.User;
import it.unipi.dii.inginf.dsmt.gameon.utils.Utils;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

/**
 * Class that contains the function used to interact with the Key-Value DB
 * Pattern of the keys: user:username:field
 */
public class KeyValueDBDriver {
    private static volatile KeyValueDBDriver instance; //Singleton instance
    private static final ConfigurationParameters configurationParameters = ConfigurationParameters.getInstance();
    private DB db;
    private String pathDatabase;

    /**
     * Private constructor
     */
    private KeyValueDBDriver(String pathDatabase)
    {
        this.pathDatabase = pathDatabase;
        openDB();
    }

    /**
     * Thread safe getInstance
     * @return  Always the same instance of a KeyValeDBDriver
     */
    public static KeyValueDBDriver getInstance() {
        if (instance == null)
        {
            synchronized (KeyValueDBDriver.class)
            {
                if (instance == null)
                {
                    instance = new KeyValueDBDriver(configurationParameters.getPathDatabase());
                }
            }
        }
        return instance;
    }

    /**
     * Function that open the connection with the Key-Value DB
     */
    private void openDB() {
        Options options = new Options();
        options.createIfMissing(true);
        try {
            db = factory.open(new File(pathDatabase), options);
        } catch (IOException e) {
            closeDB();
        }
    }

    /**
     * Function that close the connection with the Key-Value DB
     */
    public void closeDB() {
        try {
            if (db != null) {
                db.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that inserts a value given the key
     * @param key       Key of the tuple
     * @param value     Value of the tuple
     */
    private void putValue (String key, String value)
    {
        db.put(bytes(key), bytes(value));
    }

    /**
     * Function that returns the value given the key
     * @param key       Key of the tuple
     * @return          A string representation of the value
     */
    private String getValue (String key)
    {
        return asString(db.get(bytes(key)));
    }

    /**
     * Function that deletes the value given the key
     * @param key       Key of the tuple
     */
    private void deleteValue (String key)
    {
        db.delete(bytes(key));
    }

    /**
     * Function that returns the user
     * @param username      Username of the user
     * @return              The user if he is in the database, otherwise null
     */
    public User getUserFromUsername (final String username)
    {
        User user = null;
        String password = getValue("user:" + username + ":password");
        // If this user is present in the DB
        if (password != null)
        {
            user = new User(username, password,
                    parseInt(getValue("user:" + username + ":ticTacToeWins")),
                    parseInt(getValue("user:" + username + ":connectFourWins")));
        }
        return user;
    }

    /**
     * Function that returns checks if it is possible to do the login
     * @param username      Username to check
     * @param password      Password to check
     * @return              The User, or null if it's not possible to do the login
     */
    public User login (final String username, final String password)
    {
        User user = getUserFromUsername(username);
        // If doesn't exist a User registered with that username, or if the password doesn't match
        if (user == null || !user.getPassword().equals(password))
        {
            return null;
        }
        return user;
    }

    /**
     * Function that returns if exists a user with this username in the DB
     * @param username      Username of the user
     * @return              True if exists, otherwise false
     */
    public boolean isRegistered (final String username)
    {
        boolean registered = false;
        String value = getValue("user:" + username + ":password");
        if (value != null)
            registered = true;
        return registered;
    }

    /**
     * Function that is used to register the new user in the DB
     * @param username      Username of the user
     * @param password      Password of the user
     */
    public void register (final String username, final String password)
    {
        // I do all the operations in a batch, for the atomicity property
        try (WriteBatch batch = db.createWriteBatch()) {
            batch.put(bytes("user:" + username + ":password"), bytes(password));
            batch.put(bytes("user:" + username + ":ticTacToeWins"), bytes(String.valueOf(0)));
            batch.put(bytes("user:" + username + ":connectFourWins"), bytes(String.valueOf(0)));
            db.write(batch);
        } catch (final IOException e) {
            e.printStackTrace();
            closeDB();
        }
    }

    /**
     * Utility method used to delete all database content
     */
    public void truncateDatabase() {
        try (final DBIterator iterator = db.iterator()) {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                String key = asString(iterator.peekNext().getKey());
                deleteValue(key);
            }
        } catch (final IOException e) {
            System.out.println("Error truncating the database.");
            e.printStackTrace();
            closeDB();
        }
    }

    /**
     * Prints all the content of the database.
     */
    public void printDatabase() {
        // iterate db content
        try (DBIterator iterator = db.iterator()) {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                String key = asString(iterator.peekNext().getKey());
                String value = asString(iterator.peekNext().getValue());
                System.out.println(key + " -> " + value);
            }
            System.out.println();
        } catch (IOException e) {
            System.out.println("Error printing the database content.");
            e.printStackTrace();
            closeDB();
        }
    }

    /**
     * Function that returns the ranking for Tris
     * @param limit number of records to be returned
     * @return  a sorted HashMap where the key is the username and the value is the number of wins
     */
    public HashMap<String, Integer> getTicTacToeRanking(int limit) {
        return getRanking("ticTacToe", limit);
    }

    /**
     * Function that returns the ranking for Connect Four
     * @param limit number of records to be returned
     * @return  a sorted HashMap where the key is the username and the value is the number of wins
     */
    public HashMap<String, Integer> getConnectFourRanking(int limit) {
        return getRanking("connectFour", limit);
    }

    /**
     * Function that returns the ranking for the game passed as a parameter
     *
     * @param gameName  name of the game to get the ranking of
     * @param limit number of records to be returned
     * @return  a sorted HashMap where the key is the username and the value is the number of wins
     */
    private HashMap<String, Integer> getRanking(String gameName, int limit) {
        HashMap<String, Integer> usersWins = new HashMap<>();
        String gameWins = gameName + "Wins";

        //Iterate DB Content
        try (DBIterator iterator = db.iterator()) {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                String key = asString(iterator.peekNext().getKey());

                // Check if it is the game wins record
                if (key.contains(gameWins)) {
                    String userWins = asString(iterator.peekNext().getValue());

                    // user:'username':'game'Wins" -> parts[1] = username
                    String[] parts = key.split(":");
                    final String username = parts[1];

                    usersWins.put(username, parseInt(userWins));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // There is no direct way to sort a HashMap by values, see sortHashMap implementation
        return sortHashMap(usersWins, limit);
    }

    /**
     * Function that sorts an HashMap on descending order on values of Integer types
     * Code adapted from: https://www.java67.com/2015/01/how-to-sort-hashmap-in-java-based-on.html#ixzz6hp389MJ5
     * @param hashMap   HashMap to be sorted in descending order on values
     * @param limit number of records to be returned
     * @return  a sorted HashMap
     */
    public static HashMap<String, Integer> sortHashMap(HashMap<String, Integer> hashMap, int limit) {
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

    /**
     * Add one point to the user
     * @param loggedUser    The User which value has to be updated
     */
    public void addUserWinTicTacToe (User loggedUser)
    {
        addUserWin(loggedUser, "ticTacToe");
    }

    /**
     * Add one point to the user
     * @param loggedUser    The User which value has to be updated
     */
    public void addUserWinConnectFour (User loggedUser)
    {
        addUserWin(loggedUser, "connectFour");
    }

    private void addUserWin(User loggedUser, String game) {
        String username = loggedUser.getUsername();
        String key = "user:" + username + ":" + game + "Wins";

        String value = getValue(key);
        int wins = Integer.parseInt(value) + 1;

        putValue(key, String.valueOf(wins));
    }
}
