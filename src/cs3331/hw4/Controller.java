package cs3331.hw4;

import cs3331.hw4.Board;
import cs3331.hw4.ConnectFive;

import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Author: Cesar Valenzuela
 * Date: 7/22/2018
 * Course: CS3331
 * Assignment: HW4
 * Instructor: Edgar Padilla
 * <p>
 * Controller for Connect Five game
 */
public class Controller {

    private Board model;
    private ConnectFive gui;
    protected Sound sound;

    private boolean localPlay;
    private boolean easyPlay;
    private boolean medPlay;

    protected Controller(Board model, ConnectFive gui) {
        this.model = model;
        this.gui = gui;
        sound = new Sound();

        //add listeners to GUI
        gui.addPlayListener(new PlayListener());
        gui.addPaintListener(new PaintListener());
        gui.addEasyListener(new EasyListener());
        gui.addMediumListener(new MediumListener());
        gui.addMouseListener(new ClickAdapter());

        gui.addPaintHelperListener(new PaintHelperListener());
        gui.addPaintHelper2Listener(new PaintHelper2Listener());
    }


    public void gameDecide(int x, int y){

        if(localPlay){
            HumanVHuman(x,y);
        }else if(easyPlay){
            HumanVsAI(x,y,model);
        }else if(medPlay){

        }else{
            gui.getMessage().setText("CHOOSE A GAME MODE");
        }
    }

    public void HumanVHuman(int x, int y) {
        try {
            if (gui.isTurn()) {
                gui.getMessage().setText("Player 2's turn");
                gui.getBoardPanel().getP1().setMove(x, y);
                gui.getBoardPanel().getBoard().addDisc(gui.getBoardPanel().getP1().currX - 1, gui.getBoardPanel().getP1().currY - 1, 1);
                gui.setTurn(false);
            } else {
                gui.getMessage().setText("Player 1's turn");
                gui.getBoardPanel().getP2().setMove(x, y);
                gui.getBoardPanel().getBoard().addDisc(gui.getBoardPanel().getP2().currX - 1, gui.getBoardPanel().getP2().currY - 1, 2);
                gui.setTurn(true);
            }
        } catch (InValidDiskPositionException ex) {
            gui.getMessage().setText("INVALID PLACEMENT");
            sound.playInvalidTileSound();
        } catch (Exception ex1) {
            // System.out.println("TIE");
        }
        winHelper();
    }

    public void HumanVsAI(int x, int y, Board board) {
        try {
            gui.getMessage().setText("Player 2's turn");
            //System.out.println("HUMAN MOVE");
            gui.getBoardPanel().getP1().setMove(x, y);
            gui.getBoardPanel().getBoard().addDisc(gui.getBoardPanel().getP1().currX - 1, gui.getBoardPanel().getP1().currY - 1, 1);
            //System.out.println("AI MOVE");
            gui.getBoardPanel().getP2().setMove(x,y,board);
            gui.getBoardPanel().getBoard().addDisc(gui.getBoardPanel().getP2().currX, gui.getBoardPanel().getP2().currY,2);
        } catch (InValidDiskPositionException ex1) {
            gui.getMessage().setText("INVALID PLACEMENT");
            new Thread(() -> {
                sound.playInvalidTileSound();
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("oops tie");
        }
        winHelper();
    }

    private void winHelper() {
        if (gui.getBoardPanel().getBoard().getBoardWon()) {
            if (gui.getBoardPanel().getBoard().getWinner() == 1) {
                gui.getMessage().setText("PLAYER 1 WINS");
            } else {
                gui.getMessage().setText("PLAYER 2 WINS");
            }
            //gui.getBoardPanel().setVisible(false);
            new Thread(() -> {
                sound.playWinSound();
            }).start();
            setNewBoard(gui.getBoardPanel().getBoard().size(),gui.getBoardPanel().getP2().playerType,gui.getBoardPanel().getColorP1(),gui.getBoardPanel().getColorP2());
        }
    }

    /**
     * Dialog pops up asking if the user wants to play against AI
     */
    private void sizeRequest2(String text, char test) {
        Object[] options = {"15x15", "9x9"};
        Object[] yesOrNo = {"Yes", "No"};
        new Thread(() -> {
            sound.playAlertSound();
        }).start();


        int confirm = JOptionPane.showOptionDialog(gui, text, "confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, yesOrNo, yesOrNo[1]);

        if (confirm == JOptionPane.YES_OPTION) {
            int n = JOptionPane.showOptionDialog(gui,
                    "pick a size", "New Game",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[1]);
            // 15 x 15
            if (n == JOptionPane.YES_OPTION) {
                if (test == 'e') {
                    easyListenerHelper(15);
                } else if (test == 'm') {
                    MediumListenerHelper(15);
                }
            } else {
                if (test == 'e') {
                    easyListenerHelper(9);
                } else if (test == 'm') {
                    MediumListenerHelper(9);
                }
            }
        }
    }

    protected void sizeRequest(String text) {
        Object[] options = {"15x15", "9x9"};
        Object[] yesOrNo = {"Yes", "No"};

        new Thread(() -> {
            sound.playAlertSound();
        }).start();

        int confirm = JOptionPane.showOptionDialog(gui, text, "confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, yesOrNo, yesOrNo[1]);

        if (confirm == JOptionPane.YES_OPTION) {
            int n = JOptionPane.showOptionDialog(gui,
                    "pick a size", "New Game",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[1]);
            if (n == JOptionPane.YES_OPTION) {
                setNewBoard(15,gui.getBoardPanel().getP2().playerType,gui.getBoardPanel().getColorP1(),gui.getBoardPanel().getColorP2());
            } else {
                setNewBoard(9,gui.getBoardPanel().getP2().playerType,gui.getBoardPanel().getColorP1(),gui.getBoardPanel().getColorP2());
            }
        }
    }

    protected void easyListenerHelper(int boardSize) {
        Color p1ColorTmp=gui.getBoardPanel().getColorP1();
        Color p2ColorTmp=gui.getBoardPanel().getColorP2();
        setNewBoard(boardSize,gui.getBoardPanel().getP2().playerType,gui.getBoardPanel().getColorP1(),gui.getBoardPanel().getColorP2());
        gui.getBoardPanel().setColorP1(p1ColorTmp);
        gui.getBoardPanel().getP1().setTileColor(p1ColorTmp);
        gui.getBoardPanel().setColorP2(p2ColorTmp);

    }

    public   void setNewBoard(int boardSize,char gameType,Color p1C,Color p2C){
        gui.getBoardPanel().setBoard(new Board(boardSize));
        gui.setSquareSize(boardSize);
        gui.getBoardPanel().setP2(gameType);
        gui.getBoardPanel().setGrid(boardSize);
        gui.getBoardPanel().drawBoard();
        gui.getBoardPanel().setVisible(true);
        gui.getBoardPanel().setColorP1(p1C);
        gui.getBoardPanel().setColorP2(p2C);
    }

    protected void MediumListenerHelper(int boardSize) {
        gui.getBoardPanel().setP2('m');
        setNewBoard(boardSize,gui.getBoardPanel().getP2().playerType,gui.getBoardPanel().getColorP1(),gui.getBoardPanel().getColorP2());
        gui.getBoardPanel().setColorP2(Color.BLACK);
        gui.getBoardPanel().setP2('m');
    }

    /**
     *
     */
    protected class ClickAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            int x = gui.locateXY(e.getX());
            int y = gui.locateXY(e.getY());
            sound.playTileSound();
//            System.out.println(gui.getBoardPanel().getP2().getIsReal());
//            System.out.println("P2 is of  type: " + gui.getBoardPanel().getP2().getClass());
//            if (gui.getBoardPanel().getP2() instanceof Human) {
//                HumanVHuman(x, y);
//            } else if (gui.getBoardPanel().getP2() instanceof MedCompAI) {
//                HumanVsAI(x, y, model);
//            } else if (gui.getBoardPanel().getP2() instanceof EasyCompAI) {
//                HumanVsAI(x, y, model);
//            }

            gameDecide(x,y);

            gui.getBoardPanel().drawBoard();
        }

        // LOCAL BUTTON TO SEPARATE GAME LOGIC METHODS
    }

    /**
     * Action Listener for the Play Button on the Tool Bar
     */
    class PlayListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            sizeRequest("Start new game?");
            easyPlay = false;
            medPlay = false;
            localPlay = true;
        }
    }

    /**
     * Action Listener for the Paintbrush Button
     */
    class PaintListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            gui.colorChooser();
        }
    }

    /**
     * Action Listener for easy AI button
     */
    class EasyListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            sizeRequest2("Start a new game against easyAI?", 'e');
            localPlay = false;
            medPlay = false;
            easyPlay = true;
        }
    }

    /**
     * Action Listener for medium AI button
     */
    class MediumListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            sizeRequest2("Start a new game against mediumAI?", 'm');
            localPlay = false;
            medPlay = false;
            easyPlay = true;
        }

    }

    /**
     *
     */
    class PaintHelperListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gui.colorChooserHelper('1');
        }

    }

    /**
     * Listener for button within the Color Chooser reserved for player two
     */
    class PaintHelper2Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gui.colorChooserHelper('2');
        }

    }


}
