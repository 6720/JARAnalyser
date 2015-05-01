package me.curlpipesh.analysis.ui;

import me.curlpipesh.analysis.util.search.SearchHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class SearchFrame extends JFrame {
    private JButton jButton1;
    private JScrollPane jScrollPane1;
    private JTextField jTextField1;
    private JTextPane jTextPane1;

    public SearchFrame() {
        super("Search...");
        initComponents();
        setLocationRelativeTo(null);
    }

    private void doSearch() {
        List<String> results = SearchHelper.getSearchResults(jTextField1.getText().toLowerCase());
        StringBuilder sb = new StringBuilder();
        sb.append("Results:\n")
                .append("========\n\n");
        for(String e : results) {
            sb.append(e).append("\n");
        }
        jTextPane1.setText(sb.toString().trim());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jTextField1 = new JTextField();
        jButton1 = new JButton();
        jScrollPane1 = new JScrollPane();
        jTextPane1 = new JTextPane();

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        jTextField1.setToolTipText("Search query. Case-insensitive");

        jTextField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    doSearch();
                }
            }
        });

        jButton1.setText("Search");

        jButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                doSearch();
            }
        });

        jTextPane1.setEditable(false);
        jTextPane1.setFont(new Font("Monospaced", 0, 12));
        jScrollPane1.setViewportView(jTextPane1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 268, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                                .addComponent(jButton1)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }
}
