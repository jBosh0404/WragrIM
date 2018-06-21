import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class UserList implements Communication, Serializable
{
    /**
     *
     */
    private int commType;
    /**
     *
     */
    private List<String> userList;

    /**
     *
     * @param userList
     * @param commType
     */
    public UserList(List<String> userList, int commType)
    {
        this.userList = userList;
        this.commType = commType;
    }

    /**
     *
     * @return
     */
    @Override
    public int getCommType()
    {
        return commType;
    }

    /**
     *
     * @return
     */
    public List<String> getUserList()
    {
        return userList;
    }

}
