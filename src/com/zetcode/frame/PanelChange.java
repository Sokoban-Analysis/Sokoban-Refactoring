package com.zetcode.frame;

import com.zetcode.game.SoundSystem;
import com.zetcode.listener.BoardKeyListner;
import com.zetcode.listener.WinListner;
import com.zetcode.panel.*;
import com.zetcode.tool.MakeLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.zetcode.var.StaticVar.*;

public class PanelChange extends JFrame {

    private Board        board = null;
    private MainUI       mainUi = null;
    private MenuUI       menuUI = null;
    private ReplayListUI replayListUI = null;
    private CardLayout   cards = new CardLayout();
    private static SoundSystem  soundMenuBG;
    private WinListner   winListner = new WinListner();

    public PanelChange() {
        setSize(1280, 755);
        addWindowListener(winListner);
        getContentPane().setLayout(cards);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //어플리케이션 종료 열려 있는 모든 윈도우 종료
        setLocationRelativeTo(null);//윈도우 창을 화면의 가운데에 띄우는 역할
        initBGsound();
        initMainUI();
    }

    private void initBGsound(){
        if(soundMenuBG == null){
            soundMenuBG = new SoundSystem("/resources/soundMain.wav");
            soundMenuBG.loop();
            soundMenuBG.play();
        }
    }



    public void initBoard(String level, int timeto){
        soundMenuBG.stopNoClose();
        board = new Board(level, this ,timeto);
        boardKeyListner = new BoardKeyListner(board);
        board.setLayout(new BorderLayout());
        board.add(new BoardUI(board), BorderLayout.CENTER);
        getContentPane().add("board", board);
        getContentPane().addKeyListener(boardKeyListner);
        getContentPane().setFocusable(true);
        setTitle("Sokoban - Board");
        winListner.setBoard(board);
    }

    public void StopBoard(){
        soundMenuBG.play();
        if(getContentPane().getComponents().length > 2){
            System.out.println(getContentPane().getComponents().length);
            getContentPane().remove(getContentPane().getComponents().length - 1);
        }
    }

    public void initMainUI(){
        mainUi = new MainUI(this);
        getContentPane().add("main", mainUi);
        setTitle("Sokoban - MainUI");
    }
    public void initReplayUI(){
        replayListUI = new ReplayListUI(this);
        getContentPane().add("replay", replayListUI);
        setTitle("Sokoban - ReplayListUI");
    }

    public void initMenuUI(){
        menuUI = new MenuUI(this);
        getContentPane().add("menu", menuUI);
        setTitle("Sokoban - MenuUI");
    }

    public void changePanel(){
        cards.last(getContentPane());
    }
}