import java.io.Serializable;

public class OnlineStatus implements Communication, Serializable
{

    private boolean online;

    public void setOnline(boolean online)
    {
        this.online = online;
    }

    public boolean isOnline()
    {
        return online;
    }

    public int getCommType()
    {

        return Communication.ONLINESTATUS;

    }

}
