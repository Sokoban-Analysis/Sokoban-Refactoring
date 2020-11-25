package com.zetcode.panel;

import com.zetcode.frame.PanelChange;
import com.zetcode.game.SoundSystem;
import com.zetcode.tool.MakeButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.net.URL;
import java.util.Scanner;

import static com.zetcode.var.StaticVar.*;

public class MainUI extends JPanel implements ActionListener {
    private PanelChange change;
    private ImageIcon backImg = new ImageIcon(getClass().getClassLoader().getResource("resources/main/mainBack.png"));
    private URL playButtonPath = getClass().getClassLoader().getResource("resources/main/playButton.png");
    private URL playButtonDownPath = getClass().getClassLoader().getResource("resources/main/playButtonDown.png");
    private MakeButton playbutton;

    public MainUI(PanelChange change) {
        setLayout(null);
        this.change = change;
        playbutton = new MakeButton(this, playButtonPath, playButtonDownPath);
        playbutton.setup(500,432,298,128, this::actionPerformed);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource()==playbutton.getButton()){
            change.initMenuUI();
            change.changePanel();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backImg.getImage(), 0,0, 1280,720,null);
    }
}