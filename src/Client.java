import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Creates an IM client to send and receive messages to and from other clients via the Server.
 */
public class Client extends JFrame
{
    /**
     * Text box to display sent and received messages.
     */
    private JTextArea srText = new JTextArea();
    /**
     * The String name of a client.
     */
    private String clientID = "";
    /**
     * Text box to enter text to be sent to another client via the Server.
     */
    private JTextArea msgText = new JTextArea();
    /**
     * Button to initiate the process of sending the text in msgText to the intended recipient and display it in the srText box
     */
    private JButton sendBtn = new JButton();
    /**
     * Combo box populated with active users to allow the user to select the recipient to send a message to.
     * <p>Will be removed when a proper Buddy List has been implemented</p>
     */
    private JComboBox<String> jcUserList = new JComboBox<>();
    /**
     * Label for the jcUserList combo box.
     * <p>Will be removed when a proper Buddy List has been implemented</p>
     */
    private JLabel userListLabel = new JLabel("Recipient: ");
    /**
     * Field to simplify denoting a shift key event
     */
    private int shift = KeyEvent.VK_SHIFT;
    /**
     * Field to simplify denoting an enter key event
     */
    private int enter = KeyEvent.VK_ENTER;
    /**
     * Field to indicate that shift was pressed
     */
    private boolean shiftPressed = false;
    /**
     * Field to indicate that enter was pressed
     */
    private boolean enterPressed = false;
    /**
     * Provides the connection to the Server
     */
    private Socket socket;
    /**
     * Used to send Communication objects to the Server to be processed and routed to the recipient
     */
    private ObjectOutputStream objectOutputStream;
    /**
     * Used to receive Communication objects from the Server
     */
    private ObjectInputStream objectInputStream;
    /**
     * Stores the IP address of the Server
     */
    private String serverIP;

    /**
     * The main method. Creates a Client object.
     * @param args Arguments passed when called via a terminal -- not used in this implementation.
     */
    public static void main(String[] args) { new Client(); }

    /**
     * The default constructor. Initializes the GUI for Client objects and defines action listeners for the Send button
     */
    public Client()
    {

        serverIP = "10.0.0.248"; //JOptionPane.showInputDialog("Enter the LAN IP of the server. Client will not connect without this.");
        setSize(500, 350);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(getWidth()-5, getHeight()-55);


        srText.setSize(500, 200);
        srText.setEditable(false);
        srText.setBackground(new Color(238, 238, 238));
        srText.setLineWrap(true);
        srText.setWrapStyleWord(true);
        srText.setRows(10);


        msgText.setSize(430, 70);
        msgText.setEditable(true);
        msgText.setLineWrap(true);
        msgText.setWrapStyleWord(true);


        sendBtn.setSize(70, 70);
        sendBtn.setText("Send");
        sendBtn.addActionListener(new MessageBtnListener());
        msgText.addKeyListener(new KeyListener()
            {

                /**
                 * The method called when a key press event is detected. Checks to see if the event was triggered
                 * by either the enter key or the shift key. If enter was pressed and shift was not, triggers a click
                 * event on the sendBtn.
                 */
                @Override
                public void keyPressed(KeyEvent e)
                {

                    if (e.getKeyCode() == enter)
                    {

                        enterPressed = true;

                    }

                    if (e.getKeyCode() == shift)
                    {

                        shiftPressed = true;

                    }

                    if (enterPressed && !shiftPressed) {

                        sendBtn.doClick();

                    }
                }

                /**
                 * The method called when a key release event is detected. If the enter key was released and shift was
                 * pressed, append a newline character to the msgText box. If shift was not pressed, clear the text of
                 * msgText box. Resets the boolean values of enterPressed and shiftPressed.
                 */
                @Override
                public void keyReleased(KeyEvent e)
                {

                    if (e.getKeyCode() == enter)
                    {

                        if (shiftPressed)
                        {
                            msgText.append("\n");
                            enterPressed = false;

                        } else
                        {

                            enterPressed = false;
                            msgText.setText("");

                        }
                    }

                    if (e.getKeyCode() == shift)
                    {

                        shiftPressed = false;

                    }
                }

                /**
                 * Required override of the KeyListener.keyTyped() method. Unused in this implementation.
                 */
                @Override
                public void keyTyped(KeyEvent e){}

            });

        JPanel userPanel = new JPanel(new BorderLayout());

        userPanel.setSize(getWidth()-5, getHeight() - 305);
        userPanel.add(userListLabel, BorderLayout.WEST);
        userPanel.add(jcUserList, BorderLayout.CENTER);

        panel.add(srText, BorderLayout.NORTH);
        panel.add(msgText, BorderLayout.CENTER);
        panel.add(sendBtn, BorderLayout.EAST);

        add(userPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        startClient();

    }

    /**
     * Method to initiate a Client. Initializes the socket connection to the Server as well as the related input and output
     * streams. Constantly listens for a communication from the Server.
     */
    private void startClient()
    {

        try
        {

            socket = new Socket(serverIP, 8000);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());


            while(true)
            {

                receiveCommunication();

            }

        } catch(Exception ex)
        {

            srText.append(ex.toString());
            /*srText.append("\nProgram will exit in 5 seconds.");
            System.err.println(ex);
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException ie) {};
            this.dispose();*/

        }
    }

    /**
     * The method that listens for communications from the server and sends them to be processed into the appropriate
     * child object of the Communication interface. Also includes a flavorful message in the event of a ClassNotFoundException.
     * @throws IOException Allows any potential IOException to be handled by the calling statement rather than
     * within the method declaration.
     */
    private void receiveCommunication() throws IOException
    {
        try
        {

            Communication communication = (Communication) objectInputStream.readObject();
            processCommunication(communication);

        }catch (ClassNotFoundException cnfe)
        {

            System.err.println("What the fuck, man? We couldn't find that class. GG, buddy.\n" + cnfe);

        }
    }

    /**
     * The method that processes a received communication into the appropriate child object of the Communication
     * class. Currently the two supported object types are UserList and Message.
     * @param communication The Communication object to be processed.
     */
    private void processCommunication(Communication communication)
    {

        if (communication.getCommType() == Communication.MESSAGE)
        {

            Message message = (Message)communication;
            srText.append("[" + message.getDate().getHours() + ":" + message.getDate().getMinutes() + "] " + message.getSender() + ": " + message.getMessage() + '\n');

        } else if (communication.getCommType() == Communication.USERLIST)
        {

            UserList userList = (UserList)communication;

            if (clientID.equals(""))
            {

                clientID = userList.getClientID();
                setTitle(userList.getClientID());

            }

            if (jcUserList.getItemCount() == 0)
            {

                for (int i = 0; i < userList.getUserList().size(); i++)
                {
                    if (!userList.getUserList().get(i).equals(clientID))
                    {
                        jcUserList.addItem(userList.getUserList().get(i));
                    }
                }

            } else
            {

                jcUserList.removeAllItems();
                for (int i = 0; i < userList.getUserList().size(); i++)
                {

                    jcUserList.addItem(userList.getUserList().get(i));

                }
            }
        }
    }

    private String getClientID() {

        return clientID;

    }

    /**
     * Custom ActionListener inner class to handle click events on the sendBtn.
     */
    class MessageBtnListener implements ActionListener
    {

        /**
         * The method called when an action event is detected from the assigned object--in this case, the sendBtn.
         * Creates a Message object from the text within the msgText box as well as a new Date object and the type of communication,
         * in this case, Communication.MESSAGE. Appends a timestamp and the contents of msgText to the srText box, then clears the contents
         * of the msgText box. Attempts to send the generated Message object through the objectOutputStream to the Server. I may consider moving
         * this last bit of functionality, namely the sending of the Message object, into its own method.
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {

            Message message = new Message(msgText.getText(), getClientID(), jcUserList.getItemAt(jcUserList.getSelectedIndex()), new Date());
            srText.append("[" + new Date().getHours() + ":" + new Date().getMinutes() + "] Me: " + msgText.getText() + '\n');
            msgText.setText("");

            try
            {

                objectOutputStream.writeObject(message);

            } catch(Exception ex)
            {

                System.err.println(ex);

            }
        }
    }
}
/**
 * ***NOTES***
 * To implement a buddy list, it is likely I will have to encapsulate much of the code within this Server class within a new class BuddyList.
 * I will need to devise a unique identifier for each conversation window that may be created so that the BuddyList will know which conversation
 * should get which message. To that end I will need to establish a separate Conversation class that encapsulates an exchange of messages between
 * two (or more--need to think about that) clients. I think unique long integers randomly generated when a message is first sent is the best way
 * to denote which Conversation a message is intended for. I think the Server would be responsible for generating these unique identifiers and
 * providing them to the BuddyLists involved in the conversation to be assigned to the Conversation object, and the Server would be responsible for keeping
 * track of every unique Conversation identifier, though I may need to consider the ramifications of this. The current scope of this project shouldn't place
 * too great of a resource strain on the server due to managing these Conversation identifiers, but should this project move beyond my current plans, it could
 * prove to be a problem.
 *
 * Further to the implementation of a BuddyList, I will need to create a login window that is the first graphical manifestation of the client program
 * that end-users will encounter. This does not need to be its own separate class, I think. I believe I can accomplish this with an inner class inside the
 * BuddyList class, or possibly without the use of a class at all. I should be able to design the BuddyList class such that it creates the login window
 * and does not generate the actual BuddyList interface until a successful login has been established. This will be the point at which the BuddyList class
 * initializes the connection with the Server. The login interface should have an option for registration, which will open a new window with a form to be
 * completed. Fields for this form would be name, username (which will serve as the clientID), password fields (though I may decide against this as it
 * could pose a security risk since I'm making no efforts to encrypt anything, and it is likely that some people may use passwords that they have used
 * for other applications), and possibly a few others I haven't thought of just yet. Once the registration form has been filled out, it will be encapsulated
 * in a Registration object that implements the Communications interface and then sent to the Server to be stored and referenced for login attempts. I will also
 * need to implement some way of allowing users to alter their registration details after they have registered, and since I think I want registration information
 * stored solely on the server side, I will likely need to design another Communication type that queries the server for registration information. This would be
 * triggered anytime a user opened a Profile section of the BuddyList, which I think might need to be its own class, though possibly an inner class of the BuddyList
 * class. When a change is made to the profile information and saved, the updated registration information can be retransmitted to the server as a Registration
 * object, and I can include a field in the Registration object that denotes the status of the Registration object; whether it is a new registration or an
 * updated one.
 *
 * In the event another participant enters the Conversation (though at present I don't know how this would be accomplished), the unique identifier would
 * automatically be provided, though the question of having more than 2 clients involved in a Conversation raises questions about message routing. I may need
 * to alter the Message class to allow for a list of intended recipients of a message and modify the Server to allow for a message to be sent to multiple
 * recipients--though this shouldn't be too difficult; a simple 'for' loop to iterate through the list of recipients should suffice. A separate constructor
 * for the Conversation object will need to be implemented as the appearance of a Conversation window should change when more than 2 clients are involved
 * (Conversations involving 3 or more users shall henceforth be referred to as multicons). A list of users involved in the conversation should be presented in
 * some way--ideally in an unobtrusive fashion, such as a dropdown list of some kind. When a multicon is generated from an existing Conversation, the message
 * history of the conversation should be preserved for those users who had originally been involved.
 *
 * The initiation of a multicon will likely require the use of a new Communication type I think I will call an Invitation. The Invitation object should include
 * the clientIDs of all users currently involved in the conversation, the ConversationID, and possibly an optional invitation message. To that end, I think the
 * Invitation class should extend the Message class. Upon receipt of an Invitation object by a client, a dialog box should be displayed with the optional message
 * and button options yes and no to indicate whether the receiving client would like to join the conversation. The BuddyList would then create the multicon Conversation
 * object from the provided clientIDs and ConversationID of the Invitation object.
 */