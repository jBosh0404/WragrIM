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

public class Server extends JFrame
{

    private JTextArea serverLogs = new JTextArea();
    private List<ServeAClient> activeClientList = new ArrayList<>();
    private List<String> activeUsers = new ArrayList<>();

    public static void main(String[] args)
    {
        new Server();
    }

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

    private void processACommunication (Communication communication)
    {

        switch (communication.getCommType())
        {

            case (Communication.MESSAGE):
                Message message = (Message)communication;
                serverLogs.append(message.getMessage() + "\n");
                routeAMessage(message);
                break;

        }

    }

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

    private void updateAllClientUserLists()
    {
        serverLogs.append("Updating all client user lists.\n");
        UserList userList = new UserList(activeUsers, Communication.USERLIST);
        serverLogs.append("Userlist to be sent to all clients: " + userList.getUserList().toString() + "\n");

        for (int i = 0; i < activeClientList.size(); i++)
        {

            activeClientList.get(i).send(userList);
            serverLogs.append("Userlist sent to " + activeClientList.get(i).getClientName() + ": " + userList.getUserList().toString() + "\n");
            serverLogs.append(activeClientList.get(i).getClientName() + " user list has been updated.\n");

        }
        serverLogs.append("All client user lists updated.\n");

    }

    class ServeAClient implements Runnable
    {

        private Socket socket;
        private String clientName;
        private ObjectInputStream inputFromClient;
        private ObjectOutputStream outputToClient;

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

        private void send(Communication communication)
        {
            try
            {
                outputToClient.writeObject(communication);

            }catch (IOException ex)
            {
                System.err.println(ex);
            }

        }

        private String getClientName()
        {

            return clientName;

        }

    }

}