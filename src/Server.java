import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Creates a server to process incoming communications from connected clients and route them to the
 *  appropriate client recipient
 */
public class Server extends JFrame
{
    /**
     *  The main text box, used to display log information for the server, such as client connect
     *  events, updates to clients' user lists, etc.
     */
    private JTextArea serverLogs = new JTextArea();

    /**
     *  A list of ServeAClient objects to allow the server to reference the correct client when
     *  receiving or transmitting a Communication object.
     */
    private List<ServeAClient> activeClientList = new ArrayList<>();

    /**
     *  A list of String names for each user who is currently connected to the server. Used to
     *  easily transmit updates of new connected users to each connected client.
     */
    private List<String> activeUsers = new ArrayList<>();

    /**
     *  The main method. Creates the Server object.
     *  @param args Arguments passed when called via a terminal. Not used in this implementation.
     */
    public static void main(String[] args)
    {
        new Server();
    }

    /**
     *  The default constructor. Initializes the GUI for the Server object and calls the
     *  startServer() method.
     */
    public Server()
    {

        setSize(500, 500);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setSize(getWidth(), getHeight());

        serverLogs.setEditable(false);
        serverLogs.setBackground(new Color(238,238, 238));
        serverLogs.setSize(500, 500);
        serverLogs.setWrapStyleWord(true);
        serverLogs.setLineWrap(true);
        panel.add(serverLogs, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);

        startServer();

    }
    /**
     *  Method to initiate server functionality. Creates a server socket connection and waits
     *  for connection requests from clients. Creates a thread pool to handle each individual client connection.
     *  Upon connection, displays connection logging information on the serverLogs text box. Creates a new
     *  ServeAClient object to handle incoming and outgoing communications between the server and the client.
     *  Gives each connecting client a name. Adds each ServeAClient object to the activeClientList for easy referencing.
     *  Starts each ServeAClient task. Calls the updateAllClientUserLists method to inform each client of the new client
     *  connection.
     */
    private void startServer()
    {

        try
        {

            ServerSocket serverSocket = new ServerSocket(8000);
            serverLogs.append("Server started at " + new Date() + '\n');
            ExecutorService executor = Executors.newCachedThreadPool();

            int clientNo = 1;

            while (true)
            {

                Socket socket =  serverSocket.accept();

                serverLogs.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');

                InetAddress inetAddress = socket.getInetAddress();
                serverLogs.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
                serverLogs.append("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + '\n');

                String clientName = "Client " + clientNo;

                ServeAClient task = new ServeAClient(socket, clientName);

                activeClientList.add(task);

                executor.execute(task);

                activeUsers.add(clientName);

                updateAllClientUserLists();

                clientNo++;

            }

        } catch(IOException ex)
        {

            System.err.println(ex);

        }

    }

    /**
     * Method to process a Communication object into an appropriate child object of the Communication interface. <p></p>
     * Currently only supports Message objects.
     * @param communication The Communication object to be processed
     */
    private void processACommunication (Communication communication)
    {

        switch (communication.getCommType())
        {

            case (Communication.MESSAGE):
                Message message = (Message)communication;
                serverLogs.append(message.getMessage() + "\n");
                if (message.getRecipient() != null)
                {
                    routeAMessage(message);
                }
                break;

        }
        //TODO: add code for alternative communication types.

    }

    /**
     * Method to route a Message object to its intended recipient client.
     * @param message The Message object to be routed.
     */
    private void routeAMessage(Message message)
    {

        for (int i = 0; i < activeClientList.size(); i++)
        {

            if (message.getRecipient().equals(activeClientList.get(i).getClientName()))
            {

                activeClientList.get(i).send(message);
                serverLogs.append("Routing message from " + message.getSender() + " to " + message.getRecipient() + '\n');

            }

        }

    }

    /**
     * Method to send an updated UserList object to all connected clients. Called anytime a new client
     * connects
     */
    private void updateAllClientUserLists()
    {
        serverLogs.append("Updating all client user lists.\n");
        UserList userList = new UserList(activeUsers);
        serverLogs.append("Userlist to be sent to all clients: " + userList.getUserList().toString() + "\n");

        for (int i = 0; i < activeClientList.size(); i++)
        {

            userList.setClientID(activeClientList.get(i).clientName);
            activeClientList.get(i).send(userList);
            serverLogs.append("Userlist sent to " + activeClientList.get(i).getClientName() + ": " + userList.getUserList().toString() + "\n");
            serverLogs.append(activeClientList.get(i).getClientName() + " user list has been updated.\n");

        }
        serverLogs.append("All client user lists updated.\n");

    }

    /**
     * Inner class designed to manage communications from each individual client that is connected to the
     * server. Each instantiation of the class handles a single client.
     */
    class ServeAClient implements Runnable
    {

        private Socket socket;
        private String clientName;
        private ObjectInputStream inputFromClient;
        private ObjectOutputStream outputToClient;

        /**
         * The default constructor. Initializes each instantiation with a socket connection and a name for the
         * client it is serving, both of which are passed from the Server.
         * @param socket The socket connection to the assigned client
         * @param clientName The name of the assigned client.
         */
        public ServeAClient(Socket socket, String clientName)
        {

            this.socket = socket;
            this.clientName = clientName;
            try
            {

                inputFromClient = new ObjectInputStream(socket.getInputStream());
                outputToClient = new ObjectOutputStream(socket.getOutputStream());

            }catch (IOException ioe)
            {

            }

        }

        /**
         * Override of the Runnable.run() method. Constantly listens for communications from the assigned
         * client.
         */
        @Override
        public void run()
        {



            while (true)
            {

                Communication communication = receive();
                processACommunication(communication);

                //serverLogs.append("[" + message.getDate().getHours() + ":" + message.getDate().getMinutes() + "] " + message.getMessage() + '\n');

                //Message response = new Message("Message received.", new Date(), Communication.MESSAGE);

                //outputToClient.writeObject(response);

            }



        }

        /**
         * Method to handle incoming communications from the assigned client
         * @return Communication object to be processed into a child object of Communication
         */
        private Communication receive()
        {
            Communication communication;
            try
            {

               communication = (Communication) inputFromClient.readObject();
               return communication;

            } catch (IOException ioe)
            {
                System.err.println(ioe);
            } catch (ClassNotFoundException cnfe)
            {
                System.err.println(cnfe);
            }

            return null;

        }

        /**
         * Method to send a Communication object to the assigned client.
         * @param communication The Communication object to be sent to the assigned client
         */
        private void send(Communication communication)
        {
            try
            {
                outputToClient.writeObject(communication);
                outputToClient.reset(); // reset the output stream to allow multiple transfers of the same object type

            }catch (IOException ex)
            {
                System.err.println(ex);
            }

        }

        /**
         * Getter method for the client name. Used to identify the ServeAClient object associated with
         * a specific client.
         * @return String clientName - the name of the assigned client.
         */
        private String getClientName()
        {

            return clientName;

        }

    }

}
