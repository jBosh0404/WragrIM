import java.io.Serializable;
import java.util.List;

/**
 * Creates a UserList object to encapsulate a list of Clients that are actively connected to the Server.
 */
public class UserList implements Communication, Serializable
{
    private String clientID;

    /**
     * Constant identifier for Communication subclass UserList
     */
    private final int COMMTYPE = Communication.USERLIST;
    /**
     * The textual list of users to be encapsulated.
     */
    private List<String> userList;

    /**
     * The default constructor.
     * @param userList The textual list of users to be encapsulated
     */
    public UserList(List<String> userList)
    {
        this.userList = userList;
    }

    /**
     * Required overridden getter method for the type of communication--in this case Communication.USERLIST.
     * @return The type of communication--Communication.USERLIST
     */
    @Override
    public int getCommType()
    {
        return COMMTYPE;
    }

    /**
     * Getter method for the textual list of names of actively connected Clients
     * @return The textual list of names of actively connected Clients.
     */
    public List<String> getUserList()
    {
        return userList;
    }

    public String getClientID()
    {

        return clientID;

    }

    public void setClientID(String clientID)
    {

        this.clientID = clientID;

    }

}
