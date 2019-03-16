

public class Invitation implements Communication
{

    /**
     * The integer unique identifier of the originating Conversation
     */
    private int convID;

    /**
     * The String list of clients involved in the originating Conversation
     */
    private String[] clientIDs;

    /**
     * The String unique identifier for the recipient of the Invitation
     */
    private String recipientID;

    /**
     * The String unique identifier for the user sending the Invitation
     */
    private String senderID;

    /**
     * The optional String message provided by a user from the originating Conversation
     */
    private String message;

    /**
     * Constructor that initializes an Invitation object with the provided conversation ID, client IDs, recipient ID,
     * sender ID, and a default invitation message.
     * @param convID The integer unique identifier for the originating Conversation
     * @param clientIDs The String list of unique identifiers for the users involved in the originating Conversation
     * @param recipientID The String unique identifier for the recipient of the Invitation
     * @param senderID The String unique identifier for the user sending the Invitation
     */
    public Invitation (int convID, String[] clientIDs, String recipientID, String senderID)
    {

        this.convID = convID;
        this.clientIDs = clientIDs;
        this.recipientID = recipientID;
        this.message = senderID + "has sent you an invitation to join them in a conversation with ";

        for (int i = 0; i < clientIDs.length; i++)
        {

            if (!clientIDs[i].equals(senderID))
            {

                if (i < clientIDs.length - 1 && clientIDs.length == 2)
                {

                    this.message += "" + clientIDs[i] + "and ";


                } else if (i < clientIDs.length - 1 && clientIDs.length > 2)
                {

                    this.message += "" + clientIDs[i] + ", ";

                } else if (i == clientIDs.length - 1 && clientIDs.length == 2)
                {

                    this.message += "" + clientIDs[i] + "!";

                } else if (i == clientIDs.length - 1 && clientIDs.length > 2)
                {

                    this.message += ", and" + clientIDs[i] + "!";

                }
            }
        }
    }

    public Invitation (int convID, String[] clientIDs, String recipientID, String senderID, String message)
    {

        this.convID = convID;
        this.clientIDs = clientIDs;
        this.recipientID = recipientID;
        this.senderID = senderID;

    }

    /**
     * Setter method for the integer ID of the originating Conversation
     * @param convID The integer ID of the originating Conversation
     */
    public void setConvID(int convID)
    {
        this.convID = convID;
    }

    /**
     * Getter method for the integer ID of the originating Conversation
     * @return The integer ID of the originating Conversation
     */
    public int getConvID()
    {
        return convID;
    }

    /**
     * Setter method for the String list of clients involved in the originating Conversation
     * @param clientIDs The String list of clients involved in the originating Conversation
     */
    public void setClientIDs(String[] clientIDs)
    {
        this.clientIDs = clientIDs;
    }

    /**
     * Getter method for the String list of clients involved in the originating Conversation
     * @return The String list of clients involved in the originating Conversation
     */
    public String[] getClientIDs()
    {
        return clientIDs;
    }

    /**
     * Setter method for the String unique identifier of the recipient of the Invitation
     * @param recipientID The String unique identifier of the recipient of the Invitation
     */
    public void setRecipientID(String recipientID)
    {
        this.recipientID = recipientID;
    }

    /**
     * Getter method for the String unique identifier of the recipient of the Invitation
     * @return The String unique identifier of the recipient of the Invitation
     */
    public String getRecipientID()
    {
        return recipientID;
    }

    /**
     * Setter method for the optional String message provided by a user from the originating Conversation
     * @param message The optional String message provided by a user from the originating Conversation
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * Getter method for the optional String message provided by a user from the originating Conversation
     * @return The optional String message provided by a user from the originating Conversation
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Required overridden getter method for the type of communication--in this case, Communication.INVITATION
     * @return The type of communication--specifically Communication.INVITATION
     */
    @Override
    public int getCommType()
    {
        return Communication.INVITATION;
    }

}
