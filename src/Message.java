import java.io.Serializable;
import java.util.Date;

/**
 * Creates a Message object to encapsulate a text-based message. Can include optional metadata, such as a Date object, a string
 * to represent the sender of the message, and a string to represent the recipient of the message. May consider removing
 * commType as a constructor parameter, as a Message object would only ever have the commType Communication.MESSAGE.
 */
public class Message implements Communication, Serializable
{

    /**
     * The type of communication, in this case, Communication.MESSAGE.
     */
    private int commType;
    /**
     * The name of the sender of the Message object.
     */
    private String sender;
    /**
     * The name of the recipient of the Message object.
     */
    private String recipient;
    /**
     * The text-based message to be sent--the main piece of the Message object
     */
    private String message;
    /**
     * The Date object used to allow timestamps for sent/received messages.
     */
    private Date date;

    /**
     * The default constructor. Encapsulates a textual message and the type of communication in a Message object.
     * @param message The textual message to be encapsulated
     * @param commType The type of communication, in this case, Communication.MESSAGE. May be removed in a future iteration.
     */
    public Message(String message, int commType)
    {

        this.message = message;
        this.commType = commType;

    }

    /**
     * Overloaded constructor. Adds a Date parameter.
     * @param message The textual message to be encapsulated
     * @param date The Date object to allow for timestamps of messages
     * @param commType The type of communication, in this case, Communication.MESSAGE. May be removed in a future iteration.
     */
    public Message(String message, Date date, int commType)
    {

        this.message = message;
        this.date = date;
        this.commType = commType;

    }

    /**
     *
     * @param message The textual message to be encapsulated
     * @param sender The textual name of the sender of the Message object
     * @param recipient The textual name of the recipient of the Message object
     * @param date The date object to allow for timestamps of messages
     * @param commType The type of communication, in this case, Communication.MESSAGE. May be removed in a future iteration.
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
     * Getter method for the textual message.
     * @return The textual message.
     */
    public String getMessage()
    {

        return message;

    }

    /**
     * Getter method for the textual name of the sender of the Message object
     * @return the textual name of the sender of the Message object
     */
    public String getSender()
    {

        return sender;

    }

    /**
     * Getter method for the textual name of the recipient of the Message object
     * @return the textual name of the recipient of the Message object
     */
    public String getRecipient()
    {

        return recipient;

    }

    /**
     * Getter method for the Date object
     * @return the Date object
     */
    public Date getDate()
    {

        return date;

    }

    /**
     * Required overridden getter method for the type of communication--in this case, Communication.MESSAGE
     * @return The type of communication--specifically Communication.MESSAGE
     */
    @Override
    public int getCommType()
    {
        return commType;
    }
}
