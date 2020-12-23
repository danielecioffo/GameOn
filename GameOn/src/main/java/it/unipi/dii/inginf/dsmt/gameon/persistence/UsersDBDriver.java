package it.unipi.dii.inginf.dsmt.gameon.persistence;

import it.unipi.dii.inginf.dsmt.gameon.model.User;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import java.io.File;
import java.io.IOException;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

/**
 * Class that contains the function used to interact with the Key-Value DB
 * Pattern of the keys: user:username:field
 */
public class UsersDBDriver {
    private static DB db = null;

    /**
     * Function that open the connection with the Key-Value DB
     */
    private static void openDB() {
        Options options = new Options();
        options.createIfMissing(true);
        try {
            db = factory.open(new File("./../webapps/GameOn_war/resources/users"), options);
        } catch (IOException e) {
            closeDB();
        }
    }

    /**
     * Function that close the connection with the Key-Value DB
     */
    private static void closeDB() {
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
    private static void putValue (String key, String value)
    {
        db.put(bytes(key), bytes(value));
    }

    /**
     * Function that returns the value given the key
     * @param key       Key of the tuple
     * @return          A string representation of the value
     */
    private static String getValue (String key)
    {
        return asString(db.get(bytes(key)));
    }

    /**
     * Function that deletes the value given the key
     * @param key       Key of the tuple
     */
    private static void deleteValue (String key)
    {
        db.delete(bytes(key));
    }

    /**
     * Function that returns the user
     * @param username      Username of the user
     * @return              The user if he is in the database, otherwise null
     */
    public static User getUserFromUsername (final String username)
    {
        openDB();
        User user = null;
        String password = getValue("user:" + username + ":password");
        // If this user is present in the DB
        if (password != null)
        {
            user = new User(username, password,
                    Integer.parseInt(getValue("user:" + username + ":battleShipWins")),
                    Integer.parseInt(getValue("user:" + username + ":connectFourWins")));
        }
        closeDB();
        return user;
    }

    /**
     * Function that returns checks if it is possible to do the login
     * @param username      Username to chec
     * @param password      Password to check
     * @return              The User, or null if it's not possible to do the login
     */
    public static User login (final String username, final String password)
    {
        User user = getUserFromUsername(username);
        openDB();
        // If doesn't exist a User registered with that username, or if the password doesn't match
        if (user == null || !user.getPassword().equals(password))
        {
            return null;
        }
        closeDB();
        return user;
    }

    /**
     * Function that returns if exists a user with this username in the DB
     * @param username      Username of the user
     * @return              True if exists, otherwise false
     */
    public static boolean isRegistered (final String username)
    {
        boolean registered = false;
        openDB();
        String value = getValue("user:" + username + ":password");
        if (value != null)
            registered = true;
        closeDB();
        return registered;
    }

    /**
     * Function that is used to register the new user in the DB
     * @param username      Username of the user
     * @param password      Password of the user
     */
    public static void register (final String username, final String password)
    {
        openDB();
        putValue("user:" + username + "password", password);
        putValue("user:" + username + "battleShipWins", String.valueOf(0));
        putValue("user:" + username + "connectFourWins", String.valueOf(0));
        closeDB();
    }

    /**
     * Utility method used to delete all database content
     */
    public static void truncateDatabase() {
        openDB();

        try (final DBIterator iterator = db.iterator()) {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                String key = asString(iterator.peekNext().getKey());
                deleteValue(key);
            }
        } catch (final IOException e) {
            System.out.println("Error truncating the database.");
            e.printStackTrace();
        } finally {
            closeDB();
        }
    }

    /**
     * Prints all the content of the database.
     */
    public static void printDatabase() {
        // open database file
        openDB();

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
        } finally {
            closeDB();
        }
    }
}
