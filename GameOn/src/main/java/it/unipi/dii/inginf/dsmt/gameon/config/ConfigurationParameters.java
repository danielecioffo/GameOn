package it.unipi.dii.inginf.dsmt.gameon.config;

/**
 * Class used to store the configuration parameters retrieved from the config.xml
 * There is no need to modify this value, so there are only getters methods
 */
public class ConfigurationParameters {
    private String pathDatabase;
    private int howManySecondsForEachTurn;
    private int howManySkippedRoundsToStopTheGame;
    private int howManyUsersToSeeInTheRanking;

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
