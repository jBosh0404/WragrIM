import java.io.Serializable;
import java.util.Date;

public class Message implements Communication, Serializable
{

    private int commType;
    private String sender;
    private String recipient;
    private String message;
    private Date date;

    public Message(String message, int commType)
    {

        this.message = message;
        this.commType = commType;

    }

    public Message(String message, Date date, int commType)
    {

        this.message = message;
        this.date = date;
        this.commType = commType;

    }

    public Message(String message, String sender, String recipient, Date date, int commType)
    {

        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
        this.commType = commType;

    }

    public String getMessage()
    {

        return message;

    }

    public String getSender()
    {

        return sender;

    }

    public String getRecipient()
    {

        return recipient;

    }

    public Date getDate()
    {

        return date;

    }

    @Override
    public int getCommType()
    {
        return commType;
    }
}
