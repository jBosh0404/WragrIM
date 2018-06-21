import java.io.Serializable;
import java.util.List;

/**
 * Creates a UserList object to encapsulate a list of Clients that are actively connected to the Server.
 */
public class UserList implements Communication, Serializable
{
    /**
     * The type of communication--in this case, Communication.USERLIST. May be explicitly assigned in a future iteration,
     * as a UserList object would only ever have the commType Communication.USERLIST.
     */
    private int commType;
    /**
     * The textual list of users to be encapsulated.
     */
    private List<String> userList;

    /**
     * The default constructor.
     * @param userList The textual list of users to be encapsulated
     * @param commType The type of communication--in this case Communication.USERLIST. May be removed as a parameter in
     *                 future iterations.
     */
    public UserList(List<String> userList, int commType)
    {
        this.userList = userList;
        this.commType = commType;
    }

    /**
     * Required overridden getter method for the type of communication--in this case Communication.USERLIST.
     * @return The type of communication--Communication.USERLIST
     */
    @Override
    public int getCommType()
    {
        return commType;
    }

    /**
     * Getter method for the textual list of names of actively connected Clients
     * @return The textual list of names of actively connected Clients.
     */
    public List<String> getUserList()
    {
        return userList;
    }

}
