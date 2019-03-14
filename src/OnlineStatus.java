import java.io.Serializable;

/**
 * Creates an OnlineStatus object to encapsulate a boolean value that denotes the
 * connection status of a given Client
 */

public class OnlineStatus implements Communication, Serializable
{
    /**
     * The boolean value that denotes whether the originating Client is online
     */
    private boolean online;

    /**
     * The String identifier for the originating client
     */
    private String clientID;

    /**
     * Setter method for the String identifier for the originating client
     * @param clientID The String identifier for the originating client
     */
    public void setClientID(String clientID)
    {
        this.clientID = clientID;
    }

    /**
     * Getter method for the String identifier for the originating client
     * @return The String identifier for the originating client
     */
    public String getClientID()
    {
        return clientID;
    }

    /**
     * Setter method for the boolean online status of the originating client
     * @param online the boolean online status of the originating client
     */
    public void setOnline(boolean online)
    {
        this.online = online;
    }

    /**
     * Getter method for the boolean online status of the originating client
     * @return the boolean online status of the originating client
     */
    public boolean isOnline()
    {
        return online;
    }

    public int getCommType()
    {

        return Communication.ONLINESTATUS;

    }

    public OnlineStatus (boolean online, String clientID)
    {

        setClientID(clientID);
        setOnline(online);


    }

}
