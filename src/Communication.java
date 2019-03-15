/**
 * Interface to allow generic communication objects that can be processed into children objects Message and UserList.
 * Will eventually include child object Registration in future iterations.
 */
public interface Communication
{

    /**
     * Public constant to denote a Communication type as a Message.
     */
    public static final int MESSAGE = 0;
    /**
     * Public constant to denote a Communication type as a UserList
     */
    public static final int USERLIST = 1;
    /**
     * Public constant to denote a Communication type as a Registration. Will be implemented in future iterations
     * of the WragrIM project.
     */
    public static final int REGISTRATION = 2;

    /**
     * Public constant to denote a Communication type as an OnlineStatus. Used by Server and Client to establish
     * that a connection is active
     */

    public static final int ONLINESTATUS = 3;

    /**
     * Public constant to denote a Communication type as an Invitation. Generated when participants of an active
     * Conversation request additional users to join the Conversation
     */
    public static final int INVITATION = 4;

    /**
     * Getter method stub for the type of communication.
     * @return The type of communication, currently possibilities are Communication.MESSAGE and Communication.USERLIST.
     * Communication.REGISTRATION will be implemented in future iterations of the WragrIM project.
     */
    public int getCommType();


}
