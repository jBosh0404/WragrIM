import java.io.Serializable;
import java.util.List;

public class UserList implements Communication, Serializable
{

    private int commType;
    private List<String> userList;

    public UserList(List<String> userList, int commType)
    {
        this.userList = userList;
        this.commType = commType;
    }

    @Override
    public int getCommType()
    {
        return commType;
    }

    public List<String> getUserList()
    {
        return userList;
    }

}
