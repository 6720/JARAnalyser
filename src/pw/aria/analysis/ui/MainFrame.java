package pw.aria.analysis.ui;

import org.apache.commons.io.FileUtils;
import pw.aria.analysis.Main;
import pw.aria.analysis.descs.FieldDesc;
import pw.aria.analysis.descs.MethodDesc;
import pw.aria.analysis.impl.BetterClassAnalyser;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("FieldCanBeLocal")
public class MainFrame extends JFrame {

    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTabbedPane jTabbedPane1;
    private JTextPane jTextPane1;
    private JTree jTree1;

    public MainFrame() {
        super("JAR Analyser");
        initialise();
        setLocationRelativeTo(null);
    }

    public void initialise() {
        initComponents();
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    FileUtils.deleteDirectory(Main.getExtractor().getExtractionDirectory());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initComponents() {
        jScrollPane1 = new JScrollPane();
        jTree1 = new JTree();
        jTabbedPane1 = new JTabbedPane();
        jScrollPane2 = new JScrollPane();
        jTextPane1 = new JTextPane();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(jTree1);
        jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        jTree1.setModel(new FileSystemModel());
        jTree1.setCellRenderer((jTree, o, b, b1, b2, i, b3) -> new JLabel(((File)o).getName()));
        jTree1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                File filee = null;
                try {
                    filee = (File) jTree1.getPathForRow(jTree1.getRowForLocation(mouseEvent.getX(), mouseEvent.getY())).getLastPathComponent();
                } catch(NullPointerException ignored) {
                }
                File file = filee;
                if(file != null) {
                    if (file.getName().endsWith(".class")) {
                        Optional<Map.Entry<JarEntry, BetterClassAnalyser>> n = Main.getAnalysers().entrySet().parallelStream()
                                .filter(p -> {
                                    String[] q = p.getKey().getName().split(File.separator);
                                    return q[q.length - 1].equalsIgnoreCase(file.getName());
                                }).findFirst();
                        if(mouseEvent.getButton() == MouseEvent.BUTTON1) {
                            if (n.isPresent()) {
                                boolean tabExists = false;
                                for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
                                    String title = jTabbedPane1.getTitleAt(i);
                                    if (title.equalsIgnoreCase(file.getName())) {
                                        tabExists = true;
                                        break;
                                    }
                                }
                                if (!tabExists) {
                                    LoadingFrame loadingFrame = new LoadingFrame();
                                    loadingFrame.setVisible(true);
                                    n.get().getValue().getFields().forEach(FieldDesc::updateAccessLocations);
                                    n.get().getValue().getMethods().forEach(MethodDesc::updateCallLocations);
                                    loadingFrame.setVisible(false);
                                    if(loadingFrame.isVisible()) { // ;_;
                                        loadingFrame.setVisible(false);
                                    }
                                    JTextPane pane = new JTextPane();
                                    pane.setEditable(false);
                                    pane.setFont(Font.getFont("Monospaced"));
                                    pane.setText(n.get().getValue().toString());

                                    JScrollPane pane2 = new JScrollPane();
                                    pane2.setViewportView(pane);

                                    JavaSyntaxHighlighterHelper.applyRegex(JavaSyntaxHighlighterHelper.KEYWORD_REGEX, pane, Color.BLUE);
                                    JavaSyntaxHighlighterHelper.applyRegex("\\/\\/(.*)", pane, new Color(0x80, 0x80, 0x80));
                                    JavaSyntaxHighlighterHelper.applyRegex("(?s)/\\*.*?\\*/", pane, new Color(0x80, 0x80, 0x80));

                                    jTabbedPane1.addTab(file.getName(), pane2);
                                    for (int i = 0; i < jTabbedPane1.getTabCount(); i++) {
                                        String title = jTabbedPane1.getTitleAt(i);
                                        if (title.equalsIgnoreCase(file.getName())) {
                                            jTabbedPane1.setTabComponentAt(i, getClosePanel(title, jTabbedPane1));
                                            jTabbedPane1.setSelectedIndex(i);
                                            break;
                                        }
                                    }
                                }
                            }
                        } else if(mouseEvent.getButton() == MouseEvent.BUTTON3) {
                            if(n.isPresent()) {
                                TreeContextMenu menu = new TreeContextMenu(n);
                                menu.show(jTree1, mouseEvent.getX(), mouseEvent.getY());
                            }
                        }
                    }
                }
            }
        });

        jScrollPane2.setViewportView(jTextPane1);

        //jTabbedPane1.addTab("tab1", jScrollPane2);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jTabbedPane1)
                                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                                .addContainerGap())
        );

        setBounds(0, 0, 610, 430);
    }

    /**
     * http://stackoverflow.com/questions/11553112/how-to-add-close-button-to-a-jtabbedpane-tab
     * @param title Title of the given tab
     * @return JPanel to replace things and stuff
     */
    private Component getClosePanel(String title, JTabbedPane pane) {
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        JButton btnClose = new JButton("x");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);
        btnClose.addActionListener(new TabCloseActionHandler(title, pane));

        return pnlTab;
    }

    private class TabCloseActionHandler implements ActionListener {
        private String tabName;
        private JTabbedPane pane;

        public TabCloseActionHandler(String tabName, JTabbedPane pane) {
            this.tabName = tabName;
            this.pane = pane;
        }

        public String getTabName() {
            return tabName;
        }

        public void actionPerformed(ActionEvent evt) {
            int index = pane.indexOfTab(getTabName());
            if (index >= 0) {
                pane.removeTabAt(index);
                // It would probably be worthwhile getting the source
                // casting it back to a JButton and removing
                // the action handler reference ;)
            }
        }
    }
}

