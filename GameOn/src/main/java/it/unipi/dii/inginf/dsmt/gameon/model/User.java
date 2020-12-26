package it.unipi.dii.inginf.dsmt.gameon.model;

/**
 * Bean for the User
 */
public class User {
    private String username;
    private String password;
    private int battleShipWins;
    private int connectFourWins;

    public User (final String username, final String password, final int battleShipWins, final int connectFourWins)
    {
        this.username = username;
        this.password = password;
        this.battleShipWins = battleShipWins;
        this.connectFourWins = connectFourWins;
    }

    public User (final String username, final String password)
    {
        this(username, password, 0, 0);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBattleShipWins() {
        return battleShipWins;
    }

    public void setBattleShipWins(int battleShipWins) {
        this.battleShipWins = battleShipWins;
    }

    public int getConnectFourWins() {
        return connectFourWins;
    }

    public void setConnectFourWins(int connectFourWins) {
        this.connectFourWins = connectFourWins;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", battleShipWins=" + battleShipWins +
                ", connectFourWins=" + connectFourWins +
                '}';
    }
}
