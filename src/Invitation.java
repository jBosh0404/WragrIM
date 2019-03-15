

public class Invitation implements Communication
{

    /**
     * The integer ID of the originating Conversation
     */
    private int convID;

    /**
     * The String list of clients involved in the originating Conversation
     */
    private String[] clientIDs;

    /**
     * The String recipient of the Invitation
     */
    private String recipientID;

    /**
     * The optional String message provided by a user from the originating Conversation
     */
    private String message;

    /**
     * Constructor that initializes an Invitation object with the provided conversation ID and client IDs and
     * a default invitation message
     * @param convID The integer 
     * @param clientIDs
     */
    public Invitation (int convID, String[] clientIDs)
    {

        this.convID = convID;
        this.clientIDs = clientIDs;
        this.message = "You have an invitation to join a conversation with ";

        for (int i = 0; i < clientIDs.length; i++)
        {

            if (i < clientIDs.length - 1 && clientIDs.length == 2)
            {

                this.message += "" + clientIDs[i] + "and ";

            } else if (i < clientIDs.length - 1 && clientIDs.length > 2)
            {

                this.message += "" + clientIDs[i] + ", ";

            } else if  (i == clientIDs.length - 1 && clientIDs.length == 2)
            {

                this.message += "" + clientIDs[i] + "!";

            } else if (i == clientIDs.length - 1 && clientIDs.length > 2)
            {

                this.message += ", and" + clientIDs[i] + "!";

            }

        }

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
