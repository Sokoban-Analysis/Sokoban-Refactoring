package com.zetcode.tool;



import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MakeLabel extends MakeFont {

    private JLabel label;

    public MakeLabel(int x, int y, int width, int height, float size) {
        super(size);
        label = new JLabel();
        label.setFont(getFont());
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(x, y, width,height);
    }

    public void setText(String str){
        label.setText(str);
    }

    public void add(JPanel panel){
        panel.add(label);
    }

    public JLabel getLabel(){
        return label;
    }

}
