package me.curlpipesh.analysis.ui;

import me.curlpipesh.analysis.impl.BetterClassAnalyser;

import javax.swing.*;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarEntry;

public class TreeContextMenu extends JPopupMenu {
    public TreeContextMenu(Optional<Map.Entry<JarEntry, BetterClassAnalyser>> n) {
        JMenuItem one = new JMenuItem("Statistics");
        one.addActionListener(actionEvent -> {
            if(n.isPresent()) {
                StatisticsFrame frame = new StatisticsFrame(n.get().getValue());
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
        add(one);
        JMenuItem two = new JMenuItem("Method Invocation Analysis");
        two.addActionListener(actionEvent -> {
            if(n.isPresent()) {
                MethodAnalysisFrame frame = new MethodAnalysisFrame(n.get().getValue());
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
        add(two);
        JMenuItem three = new JMenuItem("Field Access Analysis");
        three.addActionListener(actionEvent -> {
            if(n.isPresent()) {
                FieldAnalysisFrame frame = new FieldAnalysisFrame(n.get().getValue());
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
        add(three);
    }
}
