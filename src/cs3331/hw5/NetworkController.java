package cs3331.hw5;

import cs3331.hw4.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
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
public class NetworkController extends Controller implements NetworkAdapter.MessageListener {

    public NetworkGUI view;
    private Board model;
    private NetworkAdapter network;
    //private Sound sound;

    private NetworkController(Board model, NetworkGUI gui) {
        super(model, gui);

        view = gui;
        this.model = model;

        view.addOnlineButtonListener(new OnlineListener());
        view.addMouseListener(new ClickAdapter());
        view.addConnectListener(new ClientListener());
        view.addHostButtonListener(new ServerListener());
        view.addDisconnectListener(new DisconnectListener());
    }

    private int popUpAns(String prompt) {
        int reponse = JOptionPane.showConfirmDialog(null, prompt);
        if (reponse == JOptionPane.YES_OPTION) {
            return 0;
        } else {
            return 1;
        }
    }

    private void writeNewPopUP() {
        int respon = popUpAns("Hey Someone wants a new board");
        if (respon == JOptionPane.YES_OPTION) {

            view.setVisiblePlayWithFriendVisibility(true);
            network.writeNewAck(true);
            sizeRequest("Hey Let's Set Up A New Game");
        }else {
            network.writeNewAck(false);
        }
    }

    private void pairAServer(Socket socket) {

        network = new NetworkAdapter(socket);
        network.setMessageListener(this);
        network.receiveMessagesAsync();

    }

    private void pairAsClient(Socket socket) {

        network = new NetworkAdapter(socket);
        network.setMessageListener(this);

        network.writeJoin();
        network.receiveMessagesAsync();
    }

    /**
     * Checks if network is connected or null
     *
     * @return boolean, true if connected
     */
    private boolean isNetwork() {
        if (network == null) {
            return false;
        }
        return true;
    }

    @Override
    public void messageReceived(NetworkAdapter.MessageType type, int x, int y, int z, int[] others) {
        switch (type) {
            case JOIN:
                int n = JOptionPane.showConfirmDialog(null, "Join client?");
                if (n == JOptionPane.YES_OPTION) {
                    network.writeJoinAck(15);
                } else {
                    network.writeJoinAck();
                }
                //sound.playConnectedSound();
                break;

            case JOIN_ACK:
                int jc = popUpAns("blah");
                if (jc == 0) {
                    System.out.println("Yes, game joined");
                } else {
                    System.out.println("Game declined");
                }
                break;

            case NEW:
                System.out.println("NEW");
                writeNewPopUP();

                break;

            case NEW_ACK:
                System.out.println("NEW ACK");
                break;

            case FILL:
                System.out.println("FILL CASE");
                try {
                    model.addDisc(x, y, z);
                } catch (InValidDiskPositionException ex) {
                    System.out.println("WRONG PLACEMENT");
                }
                break;

            case FILL_ACK:
                System.out.println("FILL ACK");
                try {

                    model.addDisc(x, y, z);
                } catch (InValidDiskPositionException ex) {
                    System.out.println("WRONG PLACEMENT");
                }
                break;

            case QUIT:
                System.out.println("Quitting : One moment");
                int quit = popUpAns("BLEE");
                if(quit == 0){
                    System.exit(-1);
                }
                break;

            case CLOSE:
                System.out.println("Close case.");
                checkClose();
                break;

            case UNKNOWN:
                System.out.println("unknown");
                break;
        }
    }

    private void checkClose() {
        network.writeQuit();
        System.out.println("check close");
    }

    class ClickAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            //super.mousePressed(e);
            int x = view.locateXY(e.getX());
            int y = view.locateXY(e.getY());
            System.out.println("network connected: " + isNetwork());

            if (network != null) {
                network.writeFill(x, y, 1);
            }
        }
    }

    /**
     * Listener for the Host Button
     */
    class ServerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                try {
                    System.out.println("Server Starting");
                    ServerSocket servSocket = new ServerSocket(view.getPortNumber());
                    Socket incoming = servSocket.accept();
                    pairAServer(incoming);
                } catch (Exception ex) {
                    System.out.println("SERVER FAILURE");
                }
            }).start();
        }

    }

    /**
     * Client button listener
     */
    class ClientListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                System.out.println("client starting");
                try {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(view.getNameField(), view.getPortField2()), 5000);
                    pairAsClient(socket);

                } catch (Exception e1) {
                    System.out.println("CLIENT FAILURE");
                }
            }).start();
        }
    }

    /**
     * Listener for the online play button
     */
    class OnlineListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.createOnlinePanel();
        }
    }

    /**
     * Listener for the disconnect from server button
     */
    class DisconnectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            network.close();
            isNetwork();
        }
    }

    public static void main(String[] args) {
        Board model = new Board(15);
        NetworkGUI view = new NetworkGUI();
        new NetworkController(model, view);
    }
}
