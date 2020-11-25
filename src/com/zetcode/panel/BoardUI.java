package com.zetcode.panel;

import com.zetcode.frame.PanelChange;
import com.zetcode.game.Player;
import com.zetcode.game.SoundSystem;
import com.zetcode.tool.MakeButton;
import com.zetcode.tool.MakeLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zetcode.var.StaticVar.*;

public class BoardUI extends JPanel implements ActionListener {

    private Board board;

    private MakeLabel timeLabel;
    private MakeLabel scoreLabel;
    private MakeLabel countLabel;
    private MakeLabel winnderLabel;

    private URL       backButtonpath = getResource("resources/board/back.png");
    private URL       backButtonDownpath = getResource("resources/board/down/back.png");
    private URL       menuButtonpath = getResource("resources/board/menu.png");
    private URL       menuButtonDownpath = getResource("resources/board/down/menu.png");

    private ImageIcon success = new ImageIcon(getResource("resources/board/success.png"));
    private ImageIcon failed = new ImageIcon(getResource("resources/board/failed.png"));

    private MakeButton backButton;
    private MakeButton menuButton;

    private SoundSystem soundBG;
    private SoundSystem resultSound;

    private int result;
    private JPanel panel;

    private URL getResource(String path){
        return getClass().getClassLoader().getResource(path);
    }

    public BoardUI(Board board){
        this.board = board;
        initBoardUI();
        initLabel();
        continueModInit();
        runTimer();
    }

    private void initBoardUI(){
        setOpaque(false);
        setLayout(null);
        result = RUNNABLE;
        panel = this;
    }

    private void continueModInit(){
        if(conMod){
            while(true){
                int key = replayFileReader.getNextKey();
                if(key == 0){
                    time = replayFileReader.getFinalTime();
                    break;
                }
                boardKeyListner.keyEventProcess(key);
            }
        }
    }


    private void initLabel(){
        timeLabel = new MakeLabel(300, 89, 300, 70, 60f);//x원래 300
        timeLabel.add(this);
        scoreLabel = new MakeLabel(720, 89, 300, 70, 60f);//x원래 720
        scoreLabel.add(this);
        countLabel = new MakeLabel(950, 160, 300, 70, 40f);//?
        countLabel.add(this);//?
        soundBG = new SoundSystem("/resources/background.wav");
        soundBG.loop();
        soundBG.play();
        backButton = new MakeButton(this, backButtonpath, backButtonDownpath);
        backButton.setup(1150, 591, 100, 100, this::actionPerformed);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(result == SUCCESS){
            g.drawImage(success.getImage(), 204,289, 888,108,this);
        }else if(result == FAILED){
            g.drawImage(failed.getImage(), 204,289, 888,108,this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource()==backButton.getButton()){
            replayMod = false;
            soundBG.stop();
            scoreTimer.shutdown();
            board.isCompleted = true;
            board.panelChange.StopBoard();
            board.panelChange.changePanel();
            board.removeAll();
            board.validate();
            board.replayFileWriter.setContinueFile();
        }
    }


    public void setScore(){
        String scoreStr = "";
        try{
            InputStream in = getClass().getResourceAsStream("/resources/data/score.txt");
            InputStreamReader ir = new InputStreamReader(in);
            BufferedReader  reader = new BufferedReader(ir);
            scoreStr = reader.readLine();
            if(Integer.parseInt(scoreStr) < score){
                File file = new File("src/resources/data/score.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                System.out.println(String.valueOf(score));
                writer.write(String.valueOf(score));
                writer.close();
            }
        }catch (Exception e){}
    }

    private void setNonTime(){
        time = (time - 0.01);
        labelSetText(timeLabel, getFormat("%.2f", time));
        labelSetText(countLabel, String.format("%d", count));//?
        if(replayMod){
            if(getFormat("%.2f", time).equals(replayFileReader.getTime())){
                boardKeyListner.keyEventProcess(replayFileReader.getNextKey());
            }
        }else{
            board.replayFileWriter.saveReplay(getFormat("%.2f", time));
        }
        if(score < 0 ){
            labelSetText(scoreLabel, "0");
        }else{
            score--;
            labelSetText(scoreLabel, String.valueOf(score));
        }
    }

    private void setEndTime(){
        board.isCompleted = true;
        labelSetText(timeLabel, "0.0");
        labelSetText(countLabel, "0");//?
        soundBG.stop();
        resultSound = new SoundSystem("/resources/failed.wav");
        resultSound.play();
        result = FAILED;
        menuButton = new MakeButton(panel, menuButtonpath, menuButtonDownpath);
        menuButton.setup(475, 422, 348, 108, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.panelChange.StopBoard();
                board.panelChange.changePanel();
                board.removeAll();
                board.validate();
            }
        });
        repaint();
        scoreTimer.shutdown();
    }

    private void setEndGame(){
        try {
            board.isCompleted = true;
            soundBG.stop();
            resultSound = new SoundSystem("/resources/success.wav");
            resultSound.play();
            result = SUCCESS;
            menuButton = new MakeButton(panel, menuButtonpath, menuButtonDownpath);
            menuButton.setup(475, 422, 348, 108, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    board.panelChange.StopBoard();
                    board.panelChange.changePanel();
                    board.removeAll();
                    board.validate();
                }
            });
            repaint();
            scoreTimer.shutdown();
        } catch (Exception e) { }
    }

    private void setEndScore(){
        if(modStatue == TwoPLAYER){
            for(int key : board.twoBags.keySet()){
                if(board.twoBags.get(key) == board.finishedBags){
                    winnderLabel = new MakeLabel(450, 212, 400, 70, 60f);
                    labelSetText(winnderLabel, "PLAYER " + key);
                    winnderLabel.add(panel);
                }
            }
        }else{ setScore(); }
    }

    private void labelSetText(MakeLabel scoreLabel, String s) {
        scoreLabel.setText(s);
    }
    private String getFormat(String s, double time) {
        return String.format(s, time);
    }

    private void runTimer(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setNonTime();
                if(time < 0){
                    setEndTime();
                }else if(board.finishedBags == board.nOfBags) {
                    setEndScore();
                    setEndGame();
                }
            }
        };
        scoreTimer.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.MILLISECONDS);
    }
}