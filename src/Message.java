import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class Message implements Communication, Serializable
{

    private int commType;
    private String sender;
    private String recipient;
    private String message;
    private Date date;

    /**
     *
     * @param message
     * @param commType
     */
    public Message(String message, int commType)
    {

        this.message = message;
        this.commType = commType;

    }

    /**
     *
     * @param message
     * @param date
     * @param commType
     */
    public Message(String message, Date date, int commType)
    {

        this.message = message;
        this.date = date;
        this.commType = commType;

    }

    /**
     *
     * @param message
     * @param sender
     * @param recipient
     * @param date
     * @param commType
     */
    public Message(String message, String sender, String recipient, Date date, int commType)
    {

        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
        this.commType = commType;

    }

    /**
     *
     * @return
     */
    public String getMessage()
    {

        return message;

    }

    /**
     *
     * @return
     */
    public String getSender()
    {

        return sender;

    }

    /**
     *
     * @return
     */
    public String getRecipient()
    {

        return recipient;

    }

    /**
     *
     * @return
     */
    public Date getDate()
    {

        return date;

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
}
