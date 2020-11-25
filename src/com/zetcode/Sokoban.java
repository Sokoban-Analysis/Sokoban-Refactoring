package com.zetcode;

import com.zetcode.frame.PanelChange;

import java.awt.*;
import static com.zetcode.var.StaticVar.*;

public class Sokoban{

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {//하고 있는 일 하고 나중에 이 밑에 것들을 호출해라
            PanelChange change = new PanelChange();
            panelChange = change;
            change.setVisible(true);//game창 화면에 나타냄
        });
    }
}
