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

public class Client extends JFrame
{

    private JTextArea srText = new JTextArea();
    private JTextArea msgText = new JTextArea();
    private JButton sendBtn = new JButton();
    private JComboBox<String> jcUserList = new JComboBox<>();
    private JLabel userListLabel = new JLabel("Recipient: ");
    private int shift = KeyEvent.VK_SHIFT;
    private int enter = KeyEvent.VK_ENTER;
    private boolean shiftPressed = false;
    private boolean enterPressed = false;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private String serverIP;

    public static void main(String[] args)
    {

        new Client();

    }

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

        //Message message;

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

    private void processCommunication(Communication communication)
    {

        if (communication.getCommType() == Communication.MESSAGE)
        {

            Message message = (Message)communication;
            srText.append("[" + message.getDate().getHours() + ":" + message.getDate().getMinutes() + "] " + message.getSender() + ": " + message.getMessage() + '\n');

        } else if (communication.getCommType() == Communication.USERLIST)
        {

            //TODO: enter code to update user list
            UserList userList = (UserList)communication;

            if (jcUserList.getItemCount() == 0)
            {

                for (int i = 0; i < userList.getUserList().size(); i++)
                {

                    jcUserList.addItem(userList.getUserList().get(i));

                }
                this.repaint();

            } else
            {

                jcUserList.removeAllItems();
                for (int i = 0; i < userList.getUserList().size(); i++)
                {

                    jcUserList.addItem(userList.getUserList().get(i));

                }
                this.repaint();

            }

        }

    }

    class MessageBtnListener implements ActionListener
    {

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
