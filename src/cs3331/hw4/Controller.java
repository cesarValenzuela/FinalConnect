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

    private static Board model;
    private static ConnectFive gui;

    protected Controller(Board model, ConnectFive gui) {
        this.model = model;
        this.gui = gui;

        //add listeners to GUI
        gui.addPlayListener(new PlayListener());
        gui.addPaintListener(new PaintListener());
        gui.addEasyListener(new EasyListener());
        gui.addMediumListener(new MediumListener());
        gui.addMouseListener(new ClickAdapter());

        gui.addPaintHelperListener(new PaintHelperListener());
        gui.addPaintHelper2Listener(new PaintHelper2Listener());
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
            Sound.playInvalidTileSound();
        } catch (Exception ex1) {
            System.out.println("TIE");
        }
        //gui.getBoardPanel().repaint();
        winHelper();
    }

    public void HumanVsAI(int x, int y, Board board) {
        try {
            gui.getMessage().setText("Player 2's turn");
            System.out.println("HUMAN MOVE");
            gui.getBoardPanel().getP1().setMove(x, y);
            gui.getBoardPanel().getBoard().addDisc(gui.getBoardPanel().getP1().currX - 1, gui.getBoardPanel().getP1().currY - 1, 1);
            System.out.println("AI MOVE");
            gui.getBoardPanel().getP2().setMove(x, y, board);
            gui.getBoardPanel().getBoard().addDisc(gui.getBoardPanel().getP2().currX, gui.getBoardPanel().getP2().currY, 2);

        } catch (InValidDiskPositionException ex1) {
            gui.getMessage().setText("INVALID PLACEMENT");
            new Thread(() -> {
                Sound.playInvalidTileSound();
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
                Sound.playWinSound();
            }).start();
            setNewBoard(gui.getBoardPanel().getBoard().size());
        }
    }


    /**
     * Action Listener for the Play Button on the Tool Bar
     */
    public static class PlayListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            sizeRequest("Start new game?");
        }
    }

    /**
     * Action Listener for the Paintbrush Button
     */
    static class PaintListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gui.colorChooser();
        }
    }

    /**
     * Action Listener for easy AI button
     */
    static class EasyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Controller.sizeRequest2("Start a new game against easyAI?", 'e');
        }
    }

    protected static void easyListenerHelper(int boardSize) {
        System.out.println("ez");
        Color p1ColorTmp = gui.getBoardPanel().getColorP1();
        setNewBoard(boardSize);
        gui.getBoardPanel().setColorP1(p1ColorTmp);
        gui.getBoardPanel().getP1().setTileColor(p1ColorTmp);
        gui.getBoardPanel().setColorP2(Color.BLACK);

    }

    static void setNewBoard(int boardSize) {
        gui.getBoardPanel().setBoard(new Board(boardSize));
        gui.setSquareSize(boardSize);
        gui.getBoardPanel().setP2('e');
        gui.getBoardPanel().setGrid(boardSize);
        gui.getBoardPanel().drawBoard();
        gui.getBoardPanel().setVisible(true);
    }

    protected static void MediumListenerHelper(int boardSize) {
        System.out.println("MEDIUM");
        gui.getBoardPanel().setP2('m');
        setNewBoard(boardSize);
        gui.getBoardPanel().setColorP2(Color.BLACK);
        gui.getBoardPanel().setP2('m');
    }

    /**
     * Action Listener for medium AI button
     */
    static class MediumListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Controller.sizeRequest2("Start a new game against mediumAI?", 'm');
        }
    }

    /**
     *
     */
    class ClickAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            int x = gui.locateXY(e.getX());
            int y = gui.locateXY(e.getY());
            new Thread(() -> {
                Sound.playTileSound();
            }).start();
            System.out.println(gui.getBoardPanel().getP2().getIsReal());
            System.out.println("P2 is of  type: " + gui.getBoardPanel().getP2().getClass());
            if (gui.getBoardPanel().getP2() instanceof Human) {
                HumanVHuman(x, y);
            } else if (gui.getBoardPanel().getP2() instanceof MedCompAI) {
                HumanVsAI(x, y, model);
            } else if (gui.getBoardPanel().getP2() instanceof EasyCompAI) {
                HumanVsAI(x, y, model);
            }
            gui.getBoardPanel().drawBoard();
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
     *
     */
    class PaintHelper2Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gui.colorChooserHelper('2');
        }
    }

    private static void sizeRequest2(String text, char test) {
        Object[] options = {"15x15", "9x9"};
        Object[] yesOrNo = {"Yes", "No"};
        new Thread(() -> {
            Sound.playAlertSound();
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

    protected static void sizeRequest(String text) {
        Object[] options = {"15x15", "9x9"};
        Object[] yesOrNo = {"Yes", "No"};

        Sound.playAlertSound();

        int confirm = JOptionPane.showOptionDialog(gui, text, "confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, yesOrNo, yesOrNo[1]);

        if (confirm == JOptionPane.YES_OPTION) {
            int n = JOptionPane.showOptionDialog(gui,
                    "pick a size", "New Game",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[1]);
            if (n == JOptionPane.YES_OPTION) {
                setNewBoard(15);
            } else {
                setNewBoard(9);
            }
        }
    }


}
