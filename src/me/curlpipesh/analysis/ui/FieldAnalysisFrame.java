package me.curlpipesh.analysis.ui;

import me.curlpipesh.analysis.impl.BetterClassAnalyser;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("FieldCanBeLocal")
public class FieldAnalysisFrame extends JFrame {
    private JScrollPane jScrollPane3;
    private JTextPane jTextPane2;

    public FieldAnalysisFrame(BetterClassAnalyser analyser) {
        super("Field access analysis for " + analyser.getClassDesc().getClassName().replaceAll("/", ".") + ".class");
        initComponents(analyser);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents(BetterClassAnalyser analyser) {
        jScrollPane3 = new JScrollPane();
        jTextPane2 = new JTextPane();
        jTextPane2.setText(analyser.getFieldAnalysis());
        jTextPane2.setEditable(false);
        jTextPane2.setFont(Font.getFont("Monospaced"));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jScrollPane3.setViewportView(jTextPane2);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );

        setBounds(0, 0, 610, 430);
    }
}
