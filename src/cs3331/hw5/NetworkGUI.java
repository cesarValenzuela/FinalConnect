package cs3331.hw5;

import cs3331.hw4.ConnectFive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Author: Cesar Valenzuela
 * Date: 7/27/2018
 * Course:
 * Assignment:
 * Instructor:
 * T.A:
 */
public class NetworkGUI extends ConnectFive {


    private JButton onlineButton;
    private JButton host = new JButton("HOST");
    private JButton connectButton = new JButton("connect");
    private JButton disconnectButton = new JButton("disconnect");
    private JButton playWithFriend;

    private JTextField portField;
    private JTextField nameField;
    private JTextField portField2;


    NetworkGUI() {
        super();
    }

    NetworkGUI(int boardSize, char difficulty) {
        super(boardSize, difficulty);
    }

    protected JToolBar toolBar() {
        JToolBar toolBar = super.toolBar();

        onlineButton = new JButton(createImageIcon("wifi-red.png"));
        onlineButton.setToolTipText("Play against Online Player");


        playWithFriend = new JButton("playWithFriend");
        playWithFriend.setToolTipText("ok");

        toolBar.add(onlineButton);
        toolBar.add(playWithFriend);
        return toolBar;
    }

    protected int getPortField2() {
        int portNum = Integer.parseInt(portField2.getText());
        System.out.println("Client Port Num: " + portNum);
        return portNum;
    }

    protected String getNameField() {
        String ip = nameField.getText();
        return ip;
    }

    protected int getPortNumber() {
        int portNum = Integer.parseInt(portField.getText());
        System.out.println("Server Port Num: " + portNum);
        return portNum;
    }

    public void createOnlinePanel() {
        JFrame f = new JFrame("Connection");
        JPanel panel = new JPanel();
        panel.setSize(400, 400);
        f.setResizable(false);
        f.add(panel);
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);

        JPanel playerPanel = makePlayerPanel();
        panel.add(playerPanel);

        JPanel peerPanel = makePeerPanel();
        panel.add(peerPanel);
    }

    protected JPanel makePlayerPanel() {
        JPanel panel = new JPanel();

        JLabel ipNumber = new JLabel("IP number: ");

        JTextField ipField = new JTextField("localhost");
        ipField.setEditable(false);

        JLabel portNum = new JLabel("Port number: ");
        portField = new JTextField("8000", 12);

        panel.setBorder(BorderFactory.createTitledBorder("Player"));
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.add(ipNumber);
        panel.add(ipField);
        panel.add(portNum);
        panel.add(portField);
        panel.add(host);
        panel.setVisible(true);

        return panel;
    }

    protected JPanel makePeerPanel() {
        JPanel panel = new JPanel();
        JLabel hostName = new JLabel("Host name/IP: ");

        nameField = new JTextField("127.0.0.1", 12);
        JLabel portNum = new JLabel("Port number: ");
        portField2 = new JTextField("8000", 12);

        panel.setBorder(BorderFactory.createTitledBorder("Peer"));
        panel.setLayout(new GridLayout(3, 2, 5, 5));
        panel.add(hostName);
        panel.add(nameField);
        panel.add(portNum);
        panel.add(portField2);
        panel.add(connectButton);
        panel.add(disconnectButton);
        panel.setVisible(true);

        return panel;

    }

    JButton getPlayWithFriend() {
        return getPlayWithFriend();
    }

    // Modifies Status of the Network on toolbar(),  Green = ON  Red = OFF
    protected void changeNetworkImageOFF() {
        onlineButton.setIcon(createImageIcon("wifi-red.png")) ;
    }
    protected void changeNetworkImageON() {
        onlineButton.setIcon(createImageIcon("wifi-green.png")) ;
    }
    //Alerts
    protected void alertUser() {
        JOptionPane.showMessageDialog( null, "Connection to online Player Successful!" );
    }
    protected void clientDenied() {
        JOptionPane.showMessageDialog( null, "Host did not accept you :( " );
    }

    void setVisiblePlayWithFriendVisibility(boolean visibility){
        playWithFriend.setVisible(visibility);
    }



    void addHostButtonListener(ActionListener e) {
        host.addActionListener(e);
    }

    void addOnlineButtonListener(ActionListener e) {
        onlineButton.addActionListener(e);
    }

    void addConnectListener(ActionListener e) {
        connectButton.addActionListener(e);
    }

    void addDisconnectListener(ActionListener e) {
        disconnectButton.addActionListener(e);
    }

    void addPlayWithFriendListener(ActionListener actionListener){
        playWithFriend.addActionListener(actionListener);
    }
}

