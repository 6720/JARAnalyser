package pw.aria.analysis.util;

import pw.aria.analysis.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions", "unused"})
public class JARExtractor {
    private final File extractionDirectory;
    private final File jarToExtract;

    private List<File> extractedFiles;

    public JARExtractor(String jarFile) {
        File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        while(!f.isDirectory()) {
            f = f.getParentFile();
        }
        final String $PATH = String.format("%s/extractionDir", f.getAbsolutePath());
        System.out.println($PATH);
        extractionDirectory = new File($PATH);
        jarToExtract = new File(jarFile);
        extractedFiles = new CopyOnWriteArrayList<>();
    }

    public void extract() {
        if(!extractionDirectory.exists()) {
            extractionDirectory.mkdir();
        }
        try {
            pickyExtract(jarToExtract.getAbsolutePath(), extractionDirectory.getAbsolutePath(), ".class");
        } catch(IOException io) {
            io.printStackTrace();
        }
        // Collections.addAll(extractedFiles, extractionDirectory.listFiles());
    }

    private void extractJar(String pathToJar, String destDir) throws IOException {
        pickyExtract(pathToJar, destDir, "");
    }

    private void pickyExtract(String pathToJar, String destDir, String ext) throws IOException {
        if(!destDir.endsWith(File.separator)) {
            destDir += File.separator;
        }
        JarFile jar = new JarFile(pathToJar);
        Enumeration enumEntries = jar.entries();
        while (enumEntries.hasMoreElements()) {
            JarEntry file = (JarEntry) enumEntries.nextElement();
            File f = new File(destDir + File.separator + file.getName());
            if (f.isDirectory()) { // if its a directory, create it
                // f.mkdir();
                f.getParentFile().mkdirs();
                f.mkdirs();
                f.mkdir();
                continue;
            }
            if(!ext.equals("")) {
                if(!f.getName().endsWith(ext)) {
                    continue;
                }
            }
            if(!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } else {
                continue;
            }
            // Main.logger.info(String.format("Extracting %s...", f.getName()));
            InputStream is = jar.getInputStream(file); // get the input stream
            FileOutputStream fos = new FileOutputStream(f);
            while (is.available() > 0) {
                fos.write(is.read());
            }
            fos.close();
            is.close();
            extractedFiles.add(f);
        }
    }

    public List<File> getExtractedFiles() {
        return extractedFiles;
    }

    public File getExtractionDirectory() {
        return extractionDirectory;
    }
}
