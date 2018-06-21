import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    public static void main(String[] args)
    {

        new Client();

    }

    /**
     * The default constructor. Initializes the GUI for Client objects and defines
     */
    public Client()
    {

        serverIP = "10.0.0.69"; //JOptionPane.showInputDialog("Enter the LAN IP of the server. Client will not connect without this.");
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

            //TODO: enter code to update user list UPDATE: I think I may have already done this? Uncertain.
            UserList userList = (UserList)communication;

            if (jcUserList.getItemCount() == 0)
            {

                for (int i = 0; i < userList.getUserList().size(); i++)
                {

                    jcUserList.addItem(userList.getUserList().get(i));

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

            Message message = new Message(msgText.getText(), new Date(), Communication.MESSAGE);
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
