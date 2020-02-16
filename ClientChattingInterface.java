
import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientChattingInterface extends JFrame implements ActionListener {
    static Socket s;
    static DataInputStream dataInputStream;
    static DataOutputStream dataOutputStream;
    
    private JButton sendBtn;
    private JScrollPane clientMainScrollPane;
    private static JTextArea chattingMessageArea;
    private JTextField messagingTextField;

    public ClientChattingInterface() {
    	clientMainScrollPane = new JScrollPane();
    	chattingMessageArea = new JTextArea("Welcome To the Chat, Client!");
    	chattingMessageArea.setEditable(false);
    	chattingMessageArea.setLineWrap(true);
    	chattingMessageArea.setBackground(Color.PINK);
    	chattingMessageArea.setCaretColor(Color.DARK_GRAY);
    	Font times = new Font("Times New Roman", Font.BOLD, 20); 
    	chattingMessageArea.setFont(times);
    	chattingMessageArea.setForeground(Color.DARK_GRAY);
    	messagingTextField = new JTextField();
        sendBtn = new JButton();
           setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client");
        chattingMessageArea.setColumns(20);
        chattingMessageArea.setRows(5);
        clientMainScrollPane.setViewportView(chattingMessageArea);
        sendBtn.setText("Send");
        sendBtn.addActionListener(this);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
          .addComponent(clientMainScrollPane)
          .addGroup(layout.createSequentialGroup()
          .addComponent(messagingTextField,GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
          .addGap(18, 18, 18)
          .addComponent(sendBtn, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)));
        
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
           .addGroup(layout.createSequentialGroup()
           .addComponent(clientMainScrollPane, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE)
           .addGap(18, 18, 18)
           .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
           .addComponent(messagingTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
           .addComponent(sendBtn))
           .addGap(0, 11, Short.MAX_VALUE)));

        	pack();
    	}
    public static void main(String args[]) {
        new ClientChattingInterface().setVisible(true);
        try {
             s = new Socket("localhost", 8888);
             dataInputStream = new DataInputStream(s.getInputStream());
             dataOutputStream = new DataOutputStream(s.getOutputStream());
            String messageIn = "  ";
            while (!messageIn.equalsIgnoreCase("exit")) {
                messageIn = dataInputStream.readUTF();
                chattingMessageArea.setText(chattingMessageArea.getText().trim() + "\n" + messageIn);
           }
         
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        String messageOut="";
        try {
                 messageOut = messagingTextField.getText().trim();
                 dataOutputStream.writeUTF("Client: " + messageOut);
                 chattingMessageArea.setText(chattingMessageArea.getText().trim() + "\n" + "You: " + messageOut);
                 dataOutputStream.flush();
            if (messageOut.equalsIgnoreCase("exit")) {
                dataOutputStream.writeUTF("The Client Has Left");
                dataOutputStream.flush();
                JOptionPane.showMessageDialog(null, "The chat has ended, program will close now!", "Title", JOptionPane.INFORMATION_MESSAGE);
                s.close();
                System.exit(0);
            }
        }

        catch(Exception messageException) {
        	System.out.println(messageException.getMessage());
    }
}
    
}
 
