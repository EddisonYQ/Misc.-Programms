
import javax.swing.*;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
	
	
public class ServerChattingInterface extends JFrame implements ActionListener {
    static ServerSocket ss;
    static Socket s;
    static DataInputStream dataInputStream;
    static DataOutputStream dataOutputStream;
    
    private JButton sendBtn;
    private JScrollPane serverMainScrollPane;
    private static JTextArea chattingTextArea;
    private JTextField messagingTextField;

    public ServerChattingInterface() {
    	serverMainScrollPane = new JScrollPane();
    	chattingTextArea = new JTextArea("Welcome To the Chat, Server!");
    	chattingTextArea.setEditable(false);
    	chattingTextArea.setLineWrap(true);
    	chattingTextArea.setBackground(Color.PINK);
    	Font times = new Font("Times New Roman", Font.BOLD, 20); 
    	chattingTextArea.setFont(times);
    	chattingTextArea.setForeground(Color.DARK_GRAY);
    	messagingTextField = new JTextField();
        sendBtn = new JButton();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server");
        chattingTextArea.setColumns(20);
        chattingTextArea.setRows(5);
        serverMainScrollPane.setViewportView(chattingTextArea);
        sendBtn.setText("Send");
        sendBtn.addActionListener(this);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        .addComponent(serverMainScrollPane)
        .addGroup(layout.createSequentialGroup()
        .addComponent(messagingTextField, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        
        .addComponent(sendBtn,GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
        .addComponent(serverMainScrollPane,GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
        .addComponent(messagingTextField,GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE,GroupLayout.PREFERRED_SIZE)
        .addComponent(sendBtn))
        .addGap(0, 11, Short.MAX_VALUE)));
        
      //  serverMainScrollPane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        
         pack();
    }
    public static void main(String args[]) {
        new ServerChattingInterface().setVisible(true);
        try {
        	
            ss = new ServerSocket(8888);
            s = ss.accept();
            if(s.isConnected()){
                JOptionPane.showMessageDialog(null, "Chat Started","Title",JOptionPane.INFORMATION_MESSAGE);
            }
            dataInputStream = new DataInputStream(s.getInputStream());
            dataOutputStream = new DataOutputStream(s.getOutputStream());
            String messageIn = " ", messageOut = " ";
            while (!messageIn.equalsIgnoreCase("exit")) {
                messageIn = dataInputStream.readUTF();
                chattingTextArea.setText(chattingTextArea.getText().trim() + "\n" + messageIn);
            }
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void actionPerformed(ActionEvent e) {
        String messageOut = "";
        try {
                messageOut = messagingTextField.getText().trim();
                dataOutputStream.writeUTF("Server: " + messageOut);
                chattingTextArea.setText(chattingTextArea.getText().trim()+ "\n" + "You: " + messageOut);
                dataOutputStream.flush();
            if (messageOut.equalsIgnoreCase("exit")) {
                dataOutputStream.writeUTF("Server Has Left The Chat");
                dataOutputStream.flush();
                JOptionPane.showMessageDialog(null, "The Chat has ended, program will close now!", "Title", JOptionPane.INFORMATION_MESSAGE);
                s.close();
                System.exit(0);
            }
        } catch (Exception serverMessagingException) {
        	System.out.println(serverMessagingException.getMessage());
        }
    }
   
}
