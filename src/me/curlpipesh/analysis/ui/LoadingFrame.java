package me.curlpipesh.analysis.ui;

import javax.swing.*;
import java.awt.*;

public class LoadingFrame extends JFrame {
    public LoadingFrame() {
        super("Analysing...");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int w = 200;
        int h = 50;
        setPreferredSize(new Dimension(w, h));
        setSize(new Dimension(w, h));
        JProgressBar bar = new JProgressBar();
        bar.setString("Analysing...");
        bar.setStringPainted(true);
        bar.setIndeterminate(true);
        bar.setPreferredSize(new Dimension(w, h));
        bar.setSize(new Dimension(w, h));
        add(bar);
        setLocationRelativeTo(null);
    }
}
