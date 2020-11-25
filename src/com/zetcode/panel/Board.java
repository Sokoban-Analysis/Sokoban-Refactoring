package com.zetcode.panel;

import com.zetcode.frame.PanelChange;
import com.zetcode.game.*;
import com.zetcode.replay.ReplayFileWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import static com.zetcode.var.StaticVar.*;

public class Board extends JPanel{

    public int                   finishedBags = 0;
    public int                   nOfBags;
    public Map<Integer, Integer> twoBags = new HashMap<>();
    public boolean               isCompleted = false;
    public PanelChange           panelChange;
    public ReplayFileWriter      replayFileWriter;


    private Map<Integer,ArrayList<Wall>> walls = new HashMap<>();
    private Map<Integer,ArrayList<Baggage>> baggs = new HashMap<>();
    private Map<Integer,ArrayList<Area>> areas = new HashMap<>();

    public int player;
    private int timeto;

    private String    level;
    private ImageIcon backImg = new ImageIcon(getClass().getClassLoader().getResource("resources/board/background.png"));
    private ImageIcon darkImg = new ImageIcon(getClass().getClassLoader().getResource("resources/board/dark.png"));


    public Board(String level, PanelChange panelChange, int timeto) {
        setOpaque(false);
        setLayout(null);
        this.timeto = timeto;
        this.level = level;
        this.panelChange = panelChange;
        initVar();
        initMod();
    }

    public void initVar(){
        player = 0;
        score = 0;
        count = 0;
        time = timeto;
        scoreTimer = Executors.newScheduledThreadPool(0);
    }

    public void initMod(){
        setDraw();
        if(!replayMod) {
            replayFileWriter = new ReplayFileWriter(level, timeto);
        }
    }

    public void setDraw(){
        if(modStatue == TwoPLAYER){
            playerInitWorld(OnePLAYER);
            playerInitWorld(TwoPLAYER);
        }else{
            playerInitWorld(OnePLAYER);
        }
    }

    public void playerInitWorld(int player){
        this.player = player;
        setOffsetPosition();
        initWorld();
        nOfBags = baggs.get(player).size();
    }


    private void setOffsetPosition(){
        int maxOffset = 0;
        Xoffset = 0;
        Yoffset = 0;
        for (int i = 0; i < level.length(); i++) {
            char item = level.charAt(i);//level의 i번째 인덱스 가져오기
            switch (item) {
                case '\n':
                    Yoffset += SPACE;
                    if(Xoffset > maxOffset){
                        maxOffset = Xoffset;
                    }
                    Xoffset = 0;
                    break;
                default:
                    Xoffset += SPACE;
                    break;
            }
        }
        Xoffset = maxOffset/2;
        Yoffset = Yoffset/2;
    }

    private void initWorld() {
        walls.put(player, new ArrayList<>());
        baggs.put(player, new ArrayList<>());
        areas.put(player, new ArrayList<>());

        int x = 640 - Xoffset;
        if(modStatue == TwoPLAYER){
            if(player == TwoPLAYER){
                x = 640 - 2*Xoffset - SPACE;
            }else if(player == OnePLAYER){
                x = 640 + SPACE;
            }
        }
        int y = 360 - Yoffset;

        Wall wall;
        Baggage b;
        Area a;

        for (int i = 0; i < level.length(); i++) {
            char item = level.charAt(i);//level의 i번째 인덱스 가져오기
            switch (item) {
                case '\n':
                    y += SPACE;
                    x = 640 - Xoffset;
                    if(modStatue == TwoPLAYER){
                        if(player == TwoPLAYER){
                            x = 640 - 2*Xoffset - SPACE;
                        }else if(player == OnePLAYER){
                            x = 640 + SPACE;
                        }
                    }
                    break;
                case '#'://벽
                    wall = new Wall(x, y);
                    walls.get(player).add(wall);//walls 배열에 wall 추가
                    x += SPACE;
                    break;
                case '$'://옮겨야하는 공
                    b = new Baggage(x, y);//초기화??
                    baggs.get(player).add(b);
                    x += SPACE;
                    break;
                case '.':
                    a = new Area(x, y);//공을 이 여섯칸짜리 Area에다 옮겨야 함
                    areas.get(player).add(a);
                    x += SPACE;
                    break;
                case '@'://플레이어(공 옮기는 역할)
                    allPlayer[player] = new Player(x, y);
                    x += SPACE;
                    break;
                case ' '://공백
                    x += SPACE;
                    break;
                default:
                    break;
            }
        }
    }

    private void buildWorld(Graphics g) {
        g.setColor(new Color(250, 240, 170));
        g.drawImage(backImg.getImage(), 0, 0, 1280, 720, null);
        for(int p = 1; p <= modStatue; p++){
            ArrayList<Actor> world = new ArrayList<>();
            world.addAll(walls.get(p));
            world.addAll(areas.get(p));
            world.addAll(baggs.get(p));
            world.add(allPlayer[p]);

            for (int i = 0; i < world.size(); i++) {
                Actor item = world.get(i);//Actor의 item 객체에 Actor 배열인 world를 채움
                if (item instanceof Player || item instanceof Baggage) {//item이 Player를 참조하는지 true/false 반환
                    g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
                } else {
                    g.drawImage(item.getImage(), item.x(), item.y(), this);
                }
            }
        }
    }

    private void paintDarkMod(Graphics g){
        if(!darkMode){ return; }
        BufferedImage image = toBufferedImage(darkImg.getImage());
        for(int x = -640; x <= 640; x++){
            for(int y = -360; y <= 360; y++) {//반지름 100x100
                if (x * x + y * y <= 10000) {
                    image.setRGB(x + allPlayer[1].x()*2 + 20,y+ allPlayer[1].y()*2 + 10,0);
                    if(modStatue == TwoPLAYER){ image.setRGB(x + allPlayer[2].x()*2 + 20,y+ allPlayer[2].y()*2 + 10,0); }
                }
            }
        }
        g.drawImage(image, 0, 0, 1280, 720, null);
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage) { return (BufferedImage) img; }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        buildWorld(g);
        paintDarkMod(g);
    }

    public boolean checkObjectCollision(Actor actor, int type, Wall wall){
        switch (type) {
            case LEFT_COLLISION:
                return actor.isLeftCollision(wall);
            case RIGHT_COLLISION:
                return actor.isRightCollision(wall);
            case TOP_COLLISION:
                return actor.isTopCollision(wall);
            case BOTTOM_COLLISION:
                return actor.isBottomCollision(wall);
            default:
                break;
        }
        return false;
    }

    public boolean checkObjectCollision(Actor soko, int type, Baggage bag){
        switch (type) {
            case LEFT_COLLISION:
                return soko.isLeftCollision(bag);
            case RIGHT_COLLISION:
                return soko.isRightCollision(bag);
            case TOP_COLLISION:
                return soko.isTopCollision(bag);
            case BOTTOM_COLLISION:
                return soko.isBottomCollision(bag);
            default:
                break;
        }
        return false;
    }

    public boolean checkObjectCollision(Baggage bag, int type, Baggage item){
        switch (type) {
            case LEFT_COLLISION:
                return bag.isLeftCollision(item);
            case RIGHT_COLLISION:
                return bag.isRightCollision(item);
            case TOP_COLLISION:
                return bag.isTopCollision(item);
            case BOTTOM_COLLISION:
                return bag.isBottomCollision(item);
            default:
                break;
        }
        return false;
    }

    public void checkMove(Baggage bag, int type){
        switch (type) {
            case LEFT_COLLISION:
                bag.move(-SPACE, 0);
                break;
            case RIGHT_COLLISION:
                bag.move(SPACE, 0);
                break;
            case TOP_COLLISION:
                bag.move(0, -SPACE);
                break;
            case BOTTOM_COLLISION:
                bag.move(0, SPACE);
                break;
            default:
                break;
        }
    }

    public boolean checkWallCollision(Actor actor, int type) {
        for (int i = 0; i < walls.get(player).size(); i++) {
            Wall wall = walls.get(player).get(i);
            if (checkObjectCollision(actor, type, wall)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkBagCollision(Actor soko, int type){
        score += 50;
        for (int i = 0; i < baggs.get(player).size(); i++) {
            Baggage bag = baggs.get(player).get(i);
            if (checkObjectCollision(soko, type, bag)) {
                for (int j = 0; j < baggs.get(player).size(); j++) {
                    Baggage item = baggs.get(player).get(j);
                    if (!bag.equals(item)) {
                        if (checkObjectCollision(bag, type, item)) { return true; }
                    }
                    if (checkWallCollision(bag, type)) { return true; }
                }
                checkMove(bag, type);
                isCompleted(player);
            }
        }
        return false;
    }

    public void isCompleted(int player) {//게임 끝
        nOfBags = baggs.get(player).size();
        finishedBags = 0;
        twoBags.put(player, 0);
        for (int i = 0; i < nOfBags; i++) {
            Baggage bag = baggs.get(player).get(i);
            for (int j = 0; j < nOfBags; j++) {
                Area area =  areas.get(player).get(j);
                if (bag.x() == area.x() && bag.y() == area.y()) {
                    finishedBags += 1;
                    twoBags.put(player, finishedBags);
                    score += 1000;
                }
            }
        }
    }

    public void restartLevel() {
        score = 0;
        time = timeto;
        count = 0;//?
        areas.clear();
        baggs.clear();
        walls.clear();
        setDraw();
        if (isCompleted) { isCompleted = false; }
    }
}