package com.zetcode.listener;

import com.zetcode.panel.Board;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WinListner implements WindowListener {

    private Board board;

    public void setBoard(Board board){
        this.board = board;
    }
    @Override
    public void windowClosing(WindowEvent e) {
        if(board != null){
            board.replayFileWriter.setContinueFile();
        }
        System.exit(0);
    }

    @Override
    public void windowOpened(WindowEvent e) { }
    @Override
    public void windowClosed(WindowEvent e) { }
    @Override
    public void windowIconified(WindowEvent e) { }
    @Override
    public void windowDeiconified(WindowEvent e) { }
    @Override
    public void windowActivated(WindowEvent e) { }
    @Override
    public void windowDeactivated(WindowEvent e) { }

}