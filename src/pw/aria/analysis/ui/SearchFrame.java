package pw.aria.analysis.ui;

import pw.aria.analysis.Main;
import pw.aria.analysis.impl.BetterClassAnalyser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

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

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jTextField1 = new JTextField();
        jButton1 = new JButton();
        jScrollPane1 = new JScrollPane();
        jTextPane1 = new JTextPane();

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        jTextField1.setToolTipText("Search query. Case-insensitive");

        jButton1.setText("Search");

        jButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                List<String> results = new ArrayList<>();
                String query = jTextField1.getText().toLowerCase();
                for(Map.Entry<JarEntry, BetterClassAnalyser> e : Main.getAnalysers().entrySet()) {
                    // We only care about the things in the analysers, not the entries
                    BetterClassAnalyser a = e.getValue();
                    if(a.getClassDesc().getClassName().toLowerCase().contains(query)) {
                        results.add(a.getClassDesc().getClassName());
                    }
                    results.addAll(a.getMethods().stream().filter(m -> m.getName().toLowerCase().contains(query))
                            .map(m -> m.getOwner().name + "#" + m.getName()).collect(Collectors.toList()));
                    results.addAll(a.getFields().stream().filter(f -> f.getName().toLowerCase().contains(query))
                            .map(f -> f.getOwner().name + "#" + f.getName()).collect(Collectors.toList()));
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Results:\n")
                        .append("--------\n\n");
                for(String e : results) {
                    sb.append(e).append("\n");
                }
                jTextPane1.setText(sb.toString().trim());
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
