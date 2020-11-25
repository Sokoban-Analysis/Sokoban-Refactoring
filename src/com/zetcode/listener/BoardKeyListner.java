package com.zetcode.listener;

import com.zetcode.panel.Board;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static com.zetcode.var.StaticVar.*;

public class BoardKeyListner extends KeyAdapter{
    private Board board;


    public BoardKeyListner(Board board){
        this.board = board;

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (board.isCompleted || replayMod) {
            return;
        }
        int key = e.getKeyCode();
        keyEventProcess(key);
    }

    public void keyEventProcess(int key){//Cyclomatic Complexity 높음
        FileWritebyMod(key);
        switch (key) {
            case KeyEvent.VK_LEFT:
                if (collisionDetection(LEFT_COLLISION, -SPACE, 0, OnePLAYER)) return;
                break;
            case KeyEvent.VK_RIGHT:
                if (collisionDetection(RIGHT_COLLISION, SPACE, 0, OnePLAYER)) return;
                break;
            case KeyEvent.VK_UP:
                if (collisionDetection(TOP_COLLISION, 0, -SPACE, OnePLAYER)) return;
                break;
            case KeyEvent.VK_DOWN:
                if (collisionDetection(BOTTOM_COLLISION, 0, SPACE, OnePLAYER)) return;
                break;
            case KeyEvent.VK_A:
                if (collisionDetectionFor2(LEFT_COLLISION, -SPACE, 0, TwoPLAYER)) return;
                break;
            case KeyEvent.VK_D:
                if (collisionDetectionFor2(RIGHT_COLLISION, SPACE, 0, TwoPLAYER)) return;
                break;
            case KeyEvent.VK_W:
                if (collisionDetectionFor2(TOP_COLLISION, 0, -SPACE, TwoPLAYER)) return;
                break;
            case KeyEvent.VK_S:
                if (collisionDetectionFor2(BOTTOM_COLLISION, 0, SPACE, TwoPLAYER)) return;
                break;
            case KeyEvent.VK_R:
                board.restartLevel();
                break;
            default:
                break;
        }
        board.repaint();
    }

    private boolean collisionDetectionFor2(int col, int x, int y, int p){
        if (modStatue != TwoPLAYER){ return false;}
        return collisionDetection(col, x, y, p);
    }

    private boolean collisionDetection(int col, int x, int y, int p) {
        board.player = p;
        if (board.checkWallCollision(allPlayer[p], col)) { return true; }
        if (board.checkBagCollision(allPlayer[p], col)) { return true; }
        allPlayer[p].move(x, y);
        count++;
        return false;
    }

    private void FileWritebyMod(int key){
        if(!replayMod){
            board.replayFileWriter.setKetCode(key);
        }
    }
}

