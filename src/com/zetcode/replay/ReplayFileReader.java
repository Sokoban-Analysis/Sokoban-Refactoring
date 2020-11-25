package com.zetcode.replay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import static com.zetcode.var.StaticVar.*;

public class ReplayFileReader {
    private int keyCode;
    private boolean saveStatue;
    public int level;
    public int time;
    private int player;
    private double delay;
    private File file;
    private ArrayList<String> arrayList;
    private int index;

    public ReplayFileReader(File file){
        this.file = file;
        arrayList = new ArrayList<>();
        System.out.println(file.getName());
        player = Integer.parseInt(file.getName().split("-")[1]
                .replaceAll("[^0-9]",""));
        readLine();
        this.level = Integer.parseInt(arrayList.get(0));
        this.time = Integer.parseInt(arrayList.get(1));
        index = 2;
    }

    public int getNextKey(){
        if(index < arrayList.size()){
            int key = Integer.parseInt(arrayList.get(index).split("-")[0]);
            index++;
            return key;
        }else{
            replayMod = false;
            return 0;
        }
    }
    public String getTime(){
        return arrayList.get(index).split("-")[2];
    }

    public double getScore(){
        return Integer.parseInt(arrayList.get(index).split("-")[1]);
    }

    public int getLength(){
        return arrayList.size();
    }
    public double getFinalTime(){
        double time;
        if(arrayList.get(arrayList.size() - 1).split("-").length < 2){
            time = this.time;
        }else{
            time = Double.parseDouble(arrayList.get(arrayList.size() - 1).split("-")[2]);
        }
        return time;
    }

    public void readLine(){
        try {
            FileReader ir = new FileReader(file);
            BufferedReader reader = new BufferedReader(ir);
            String line = "";
            while ( (line = reader.readLine()) != null ) {
                arrayList.add(line);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}