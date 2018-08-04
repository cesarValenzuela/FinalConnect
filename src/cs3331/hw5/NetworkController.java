package cs3331.hw5;

import cs3331.hw4.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.BindException;
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


    private int isServer;
    private boolean pass = false; // check server accepted client
    private ServerSocket servSocket;
    private boolean onlinePlay;

    private NetworkController(Board model, NetworkGUI gui) {
        super(model, gui);

        view = gui;
        this.model = model;
        //view.getBoardPanel().set

        view.addMouseListener(new NetworkClickAdapter());
        view.addOnlineButtonListener(new OnlineListener());
        view.addConnectListener(new ClientListener());
        view.addHostButtonListener(new ServerListener());
        view.addDisconnectListener(new DisconnectListener());
        view.addPlayWithFriendListener(new PlayWithFriendListener());
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
        } else {
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

    @Override
    public void messageReceived(NetworkAdapter.MessageType type, int x, int y, int z, int[] others) {
        switch (type) {
            case JOIN:

                int n = JOptionPane.showConfirmDialog(null, "Join client?");
                if (n == JOptionPane.YES_OPTION) {
                    //pass = true;
                    network.writeJoinAck(15);
                    updateToolBarON(); // icon
                    System.out.println("connected = true ");

                    // missing connect sound //

                    alertUser();
                } else {

                    network.writeJoinAck();
                }
                //sound.playConnectedSound();
                break;

            case JOIN_ACK:


                if (x == 1) {
                    if(popUpAns("join?") == 0){
                        System.out.println("game joined");
                        updateToolBarON();
                        alertUser();
                    }else{
                        System.out.println("denied");
                        clientDenied();
                    }
                }else{
                    clientDenied();
                }
                break;

            case NEW:

                System.out.println("NEW");
                writeNewPopUP();

                break;

            case NEW_ACK:

                System.out.println("NEW ACK");
                if (x == 1) {
                    setNewBoard();
                } else {
                    popUpAns("New Game Denied");
                }
                break;

            case FILL:

                System.out.println("FILL CASE");
                try {
                    view.getBoardPanel().getBoard().addDisc(x-1, y-1, z);
                    network.writeFillAck(x, y, z);
                    view.getBoardPanel().repaint();
                } catch (InValidDiskPositionException ex) {
                    System.out.println("WRONG PLACEMENT");
                }
                break;

            case FILL_ACK:

                System.out.println("FILL ACK");
                try {

                    System.out.println("fill ack x y z");
                    view.getBoardPanel().getBoard().addDisc(x, y, z);
                    view.getBoardPanel().repaint();
                } catch (InValidDiskPositionException ex) {
                    System.out.println("WRONG PLACEMENT");
                }
                break;

            case QUIT:

                System.out.println("Quitting : One moment");
                int quit = popUpAns("BLEE");
                if (quit == 0) {
                    System.exit(-1);
                }
                break;

            case CLOSE:
                System.out.println("Close case.");
                updateToolBarOFF();
                checkClose();
                break;

            case UNKNOWN:

                System.out.println("unknown");
                break;
        }
    }

    protected void setNewBoard() {
        Object[] options = {"15x15", "9x9"};
        int n = JOptionPane.showOptionDialog(view,
                "pick a size", "New Game",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);
        if (n == JOptionPane.YES_OPTION) {
            System.out.println("new board 15X15");
            super.setNewBoard(15, view.getBoardPanel().getP2().playerType, view.getBoardPanel().getColorP1(), view.getBoardPanel().getColorP2());
        } else {
            System.out.println("New board 9X9");
            super.setNewBoard(9, view.getBoardPanel().getP2().playerType, view.getBoardPanel().getColorP1(), view.getBoardPanel().getColorP2());
        }
        //HERE MAYBE SEND SOMETHING?

    }

    private void checkClose() {
        network.writeQuit();
        System.out.println("method called from close");
    }

    public void onlineGame(int x, int y) {
        try {
            if(view.isTurn()) {
                //change label at bottom
                view.getMessage().setText("Player 2's turn");
                network.writeFill(x,y,isServer);
                view.setTurn(false);

                //view.getBoardPanel().getP1().setMove(x, y);
                //view.getBoardPanel().getBoard().addDisc(view.getBoardPanel().getP1().currX - 1, view.getBoardPanel().getP1().currY - 1, 1);
            }else {
                view.getMessage().setText("Player 1's turn");
                network.writeFill(x,y,isServer);
                        view.setTurn(true);
                //iew.getBoardPanel().getP2().setMove(x, y);
                //view.getBoardPanel().getBoard().addDisc(view.getBoardPanel().getP2().currX - 1, view.getBoardPanel().getP2().currY - 1, 2);
            }

        } catch (Exception ex) {
            System.out.println("inv");
        }
    }

    @Override
    public void gameDecide(int x, int y) {
        super.gameDecide(x, y);
        if (onlinePlay) {
            onlineGame(x, y);
        }
    }

    protected void sizeRequest3(String text) {
        Object[] yesOrNo = {"Yes", "No"};

        sound.playAlertSound();

        int confirm = JOptionPane.showOptionDialog(view, text, "confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, yesOrNo, yesOrNo[1]);
        if (confirm == JOptionPane.YES_OPTION) {
            network.writeNew(15);//here size doesn't matter
        }
    }

    protected void updateToolBarON() {
        view.changeNetworkImageON();
    }

    protected void updateToolBarOFF() {
        view.changeNetworkImageOFF();
    }

    protected void alertUser() {
        view.alertUser();
    }

    protected void clientDenied() {
        view.clientDenied();
    }

    class NetworkClickAdapter extends ClickAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = view.locateXY(e.getX());
            int y = view.locateXY(e.getY());
            gameDecide(x, y);
//            if(network == null){
//                System.out.println("Socket closed");
//                if(view.getBoardPanel().getP2() instanceof Human){
//                    HumanVHuman(x,y);
//                }else if(view.getBoardPanel().getP2() instanceof MedCompAI){
//                    HumanVsAI(x,y,model);
//                }else if(view.getBoardPanel().getP2() instanceof EasyCompAI){
//                    HumanVsAI(x,y,model);
//                }
//            }else{
//                network.writeFill(x,y, isServer);
//            }


//            if (network != null) {
//                network.writeFill(x, y, 1);
//            }
//            if (network.socket() != null)
//
//            if (view.getBoardPanel().getP2() instanceof Human) {
//                HumanVHuman(x, y);
//            } else if (view.getBoardPanel().getP2() instanceof MedCompAI) {
//                HumanVsAI(x, y, model);
//            } else if (view.getBoardPanel().getP2() instanceof EasyCompAI) {
//                HumanVsAI(x, y, model);
//            }
//            view.getBoardPanel().drawBoard();
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
                    servSocket = new ServerSocket(view.getPortNumber());
                    Socket incoming = servSocket.accept();
                    isServer = 1;

                    pairAServer(incoming);
                } catch (BindException ex) {
                    ex.printStackTrace();

                    System.out.println("SERVER FAILURE");
                } catch (IOException ex) {
                    ex.printStackTrace();
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
                    isServer = 2;
                } catch (Exception e1) {
                    System.out.println("CLIENT FAILURE");
                }
            }).start();
        }
    }

    /**
     *
     */
    class PlayWithFriendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //if not connected
            new Thread(() -> {
                try {
                    System.out.println("Hey I want to start a new game");
                    //view.getBoardPanel().setVisible(true);
                    sizeRequest3("Create a new game?");
                } catch (NullPointerException ex) {
                    System.out.println("Play withFriendsListener error");
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
            onlinePlay = true;
        }
    }

    /**
     * Listener for the disconnect from server button
     */
    class DisconnectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                network.close();
                servSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                System.out.println("Another Disconnect Exception");
            }
            //isNetwork();
        }
    }

    public static void main(String[] args) {
        Board model = new Board(15);
        NetworkGUI view = new NetworkGUI();
        new NetworkController(model, view);
    }
}
