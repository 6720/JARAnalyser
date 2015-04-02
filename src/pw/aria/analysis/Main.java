package pw.aria.analysis;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import pw.aria.analysis.impl.BetterClassAnalyser;
import pw.aria.analysis.ui.BetterMainFrame;
import pw.aria.analysis.util.JARExtractor;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Getter
    private static final Map<JarEntry, BetterClassAnalyser> analysers = new HashMap<>();

    @Getter
    private static JARExtractor extractor;

    public static void main(String[] args) throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.showOpenDialog(null);
        final JarFile jarFile = new JarFile(chooser.getSelectedFile().getAbsolutePath());
        extractor = new JARExtractor(chooser.getSelectedFile().getAbsolutePath());
        extractor.extract();

        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if(entry.getName().endsWith(".class")) {
                try {
                    InputStream stream = new BufferedInputStream(jarFile.getInputStream(entry), 1024);
                    ClassReader cr = new ClassReader(stream);
                    BetterClassAnalyser an = new BetterClassAnalyser(cr);
                    analysers.put(entry, an);
                    an.analyze();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    FileUtils.deleteDirectory(extractor.getExtractionDirectory());
                    System.exit(1);
                }
            }
        }
        EventQueue.invokeLater(() -> new BetterMainFrame().setVisible(true));
    }
}
