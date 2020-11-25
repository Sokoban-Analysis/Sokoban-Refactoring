package com.zetcode.panel;

import com.zetcode.frame.PanelChange;
import com.zetcode.replay.ReplayFileReader;
import com.zetcode.tool.MakeButton;
import com.zetcode.tool.MakeFont;
import com.zetcode.tool.MakeLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import static com.zetcode.var.StaticVar.*;
import static com.zetcode.var.StaticVar.replayFileReader;

public class ReplayListUI extends JPanel implements ActionListener {

    private PanelChange change;
    private MakeButton backButton;
    private JPanel panel;
    private int modTemp;
    private URL backButtonpath = getResource("resources/board/back.png");
    private URL backButtonDownpath = getResource("resources/board/down/back.png");
    private ImageIcon backImg = new ImageIcon(getClass().getClassLoader().getResource("resources/menu/replayUI.png"));
    private ImageIcon buttonImg = new ImageIcon(getClass().getClassLoader().getResource("resources/menu/replayBtn.png"));
    private ImageIcon buttonImgDown = new ImageIcon(getClass().getClassLoader().getResource("resources/menu/down/replayBtn.png"));
    public ReplayListUI(PanelChange change){
        this.change = change;
        setLayout(null);
        panel  = new JPanel();
        JScrollPane scroll = new JScrollPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        scroll.setMaximumSize(new Dimension(580, 530));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        loadFile();
        scroll.setBounds(340,177,610,510);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        add(scroll);
        backButton = new MakeButton(this, backButtonpath, backButtonDownpath);
        backButton.setup(1150, 591, 100, 100, this::actionPerformed);
        System.out.println(change.getContentPane().getComponents().length);
    }

    private void loadFile(){
        try {
            File file = new File("src/resources/data/replay");
            for(final File file1 : file.listFiles()){
                JButton button;
                button = new JButton("",buttonImg);
                button.setVerticalTextPosition(SwingConstants.CENTER);
                button.setHorizontalTextPosition(SwingConstants.CENTER);
                int idx =  file1.getName().lastIndexOf(".");
                int mod = Integer.parseInt(file1.getName().substring(0, idx).split("-")[1]);
                button.setText("<html><font face='Power Pixel-7'> "+file1.getName().substring(0, idx)+"P </font></html>");
                button.setFont(new Font(button.getFont().getName(), button.getFont().getStyle(), 35));
                button.setForeground(Color.WHITE);
                button.setPreferredSize(new Dimension(100,100));
                button.setRolloverIcon(buttonImgDown);
                button.setPressedIcon(buttonImgDown);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        replayMod = true;
                        modTemp = modStatue;
                        modStatue = mod;
                        replayFileReader = new ReplayFileReader(file1);
                        change.initBoard(levelFileRead(replayFileReader.level), replayFileReader.time);
                        change.changePanel();
                    }
                });
                panel.add(button);
            }
        }catch (Exception e){}
    }

    private String levelFileRead(int level){
        String str = "";
        try{
            File file = new File("src/resources/data/level/"+level+".txt");
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                str += scan.nextLine() + "\n";
            }
        }catch (FileNotFoundException e) {
        }
        return str;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource()==backButton.getButton()){
            System.out.println(change.getContentPane().getComponents().length);
            change.getContentPane().remove(change.getContentPane().getComponents().length -1);
            change.changePanel();
            modStatue = modTemp;
            removeAll();
            validate();

        }
    }
    private URL getResource(String path){
        return getClass().getClassLoader().getResource(path);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backImg.getImage(), 0,0, 1280,720,null);
    }

}