package com.zetcode.var;

import com.zetcode.frame.PanelChange;
import com.zetcode.game.Player;
import com.zetcode.game.SoundSystem;
import com.zetcode.listener.BoardKeyListner;
import com.zetcode.replay.ReplayFileReader;
import com.zetcode.replay.ReplayFileWriter;

import java.io.FileWriter;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class StaticVar {
    public static int Xoffset = 0;//거리
    public static int Yoffset = 0;//거리
    public static int score;
    public static double time;
    public static Player[] allPlayer = new Player[3];
    public static ScheduledExecutorService scoreTimer;
    public static final int SPACE = 20;
    public static final int LEFT_COLLISION = 1;
    public static final int RIGHT_COLLISION = 2;
    public static final int TOP_COLLISION = 3;
    public static final int BOTTOM_COLLISION = 4;
    public static final int RUNNABLE = 1;
    public static final int SUCCESS = 2;
    public static final int FAILED = 0;
    public static final int OnePLAYER = 1;
    public static final int TwoPLAYER = 2;
    public static int modStatue = OnePLAYER;
    public static boolean replayMod = false;
    public static boolean conMod = false;
    public static PanelChange panelChange = new PanelChange();
    public static BoardKeyListner boardKeyListner;
    public static ReplayFileReader replayFileReader;
    public static int count=0;//이동 횟수
    public static boolean darkMode = false;//이동 횟수

}