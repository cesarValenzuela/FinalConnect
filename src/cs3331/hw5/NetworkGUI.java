package cs3331.hw5;

import cs3331.hw4.ConnectFive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

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
    private JButton networkButton;
    private JButton serverButton;
    private static JButton host = new JButton("HOST");
    private static JButton connectButton = new JButton( "connect" );
    private static JButton disconnectButton = new JButton( "disconnect" );
    private static JTextField portField;
    private static JTextField nameField;
    private static JTextField portField2;
    private ImageIcon NETWORK_ON = createImageIcon("wifi-connected.png");
    private ImageIcon NETWORK_OFF;


    NetworkGUI() {
        super();
    }

    NetworkGUI(int boardSize, char difficulty) {
        super(boardSize, difficulty);
    }

    protected JToolBar toolBar() {
        JToolBar toolBar = super.toolBar();
        //NETWORK_OFF = createImageIcon("wifi-disconnected.png");
        //NETWORK_ON = createImageIcon("wifi-connected.png");

        //networkButton = new JButton("Client");
        //serverButton = new JButton("Server");
        onlineButton = new JButton(createImageIcon("wifi-red.png"));
        onlineButton.setToolTipText("Play against Online Player");

        //networkButton.addActionListener(this::networkbuttonClicked);
        //networkButton.setToolTipText("client");
        //networkButton.setFocusPainted(false);

        //serverButton.addActionListener(this::serverButtonClicked);
        //serverButton.setToolTipText("server");
        //serverButton.setFocusPainted(false);


        //toolBar.add(networkButton);
        //toolBar.add(serverButton);
        toolBar.add(onlineButton);
        return toolBar;
    }

    protected static int getPortField2(){
        int portNum = Integer.parseInt(portField2.getText());
        System.out.println(portNum + " this is portNum 2");
        return portNum;
    }

    protected static String getNameField(){
        String ip = nameField.getText();
        return ip;
    }

    protected static int getPortNumber(){
        int portNum = Integer.parseInt(portField.getText());
        System.out.println(portNum + "this is portNUm");
        return portNum;
    }

    protected static void maker() {
        JTextArea eventBox;

        eventBox = new JTextArea( 10, 32 );
        eventBox.setEditable( false );
        JPanel button;
        button = new JPanel();
        button.add( eventBox );
        button.setBorder( BorderFactory.createEtchedBorder() );

        JPanel player;
        player = makePlayerPanel();

        JPanel peer;
        peer = makePeerPanel();

    }

    protected static JPanel makePlayerPanel() {
        JPanel panel = new JPanel();

        JLabel ipNumber = new JLabel( "IP number: " );

        JTextField ipField = new JTextField("localhost");
        ipField.setEditable(false);

        JLabel portNum = new JLabel( "Port number: " );
        portField = new JTextField("8000",12);

        panel.setBorder( BorderFactory.createTitledBorder( "Player" ) );
        panel.setLayout( new GridLayout( 4, 2, 5, 5 ) );
        panel.add( ipNumber );
        panel.add( ipField );
        panel.add( portNum );
        panel.add( portField );
        panel.add( host );
        panel.setVisible( true );

        return panel;
    }

    public static void createOnlinePanel() {
        JFrame f = new JFrame( "Connection" );
        JPanel panel = new JPanel();
        panel.setSize( 400,400 );
        // panel.setBackground(Color.blue);
        f.setResizable( false );
        f.add( panel );
        f.setSize( 400, 400 );
        f.setLayout( null );
        f.setVisible( true );

        JPanel playerPanel = makePlayerPanel();
        panel.add( playerPanel );

        JPanel peerPanel = makePeerPanel();
        panel.add( peerPanel );
    }

    protected static JPanel makePeerPanel() {
        JPanel panel = new JPanel();
        JLabel hostName = new JLabel( "Host name/IP: " );

        nameField = new JTextField( "127.0.0.1",12 );
        JLabel portNum = new JLabel( "Port number: " );
        portField2 = new JTextField( "8000",12 );

        panel.setBorder( BorderFactory.createTitledBorder( "Peer" ) );
        panel.setLayout( new GridLayout( 3, 2, 5, 5 ) );
        panel.add( hostName );
        panel.add( nameField );
        panel.add( portNum );
        panel.add( portField2 );
        panel.add( connectButton );
        panel.add( disconnectButton );
        panel.setVisible( true );

        return panel;

    }

    void addHostButtonListener(ActionListener abe) {
        host.addActionListener(abe);
    }

    void addOnlineButtonListener(ActionListener abc){
        onlineButton.addActionListener(abc);
    }

    void addNetworkClientListener(ActionListener actionListener) {
        networkButton.addActionListener(actionListener);
    }

    void addNetworkServerListener(ActionListener actionListener) {

        serverButton.addActionListener(actionListener);
    }

    void addConnectListener(ActionListener actionListener) {
        connectButton.addActionListener( actionListener );
    }

    void addDisconnectListener(ActionListener actionListener) {
        disconnectButton.addActionListener( actionListener );
    }

}

