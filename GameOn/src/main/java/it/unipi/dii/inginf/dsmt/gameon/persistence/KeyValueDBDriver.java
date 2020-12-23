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
public class KeyValueDBDriver {
    private static KeyValueDBDriver instance; //Singleton instance
    private DB db;
    private final String URL_DATABASE = "database";

    private KeyValueDBDriver()
    {
        openDB();
        printDatabase();
    }

    public static KeyValueDBDriver getInstance() {
        if (instance == null)
        {
            instance = new KeyValueDBDriver();
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
            db = factory.open(new File(URL_DATABASE), options);
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
        if (db == null)
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
        return user;
    }

    /**
     * Function that returns checks if it is possible to do the login
     * @param username      Username to chec
     * @param password      Password to check
     * @return              The User, or null if it's not possible to do the login
     */
    public User login (final String username, final String password)
    {
        if (db == null)
            openDB();
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
        if (db == null)
            openDB();
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
        if (db == null)
            openDB();
        putValue("user:" + username + ":password", password);
        putValue("user:" + username + ":battleShipWins", String.valueOf(0));
        putValue("user:" + username + ":connectFourWins", String.valueOf(0));
    }

    /**
     * Utility method used to delete all database content
     */
    public void truncateDatabase() {
        if (db == null)
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
    public void printDatabase() {
        if (db == null)
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
            closeDB();
        }
    }
}
