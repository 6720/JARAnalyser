package me.curlpipesh.analysis.util;

import me.curlpipesh.analysis.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Utility class that extracts a JAR into a directory in the same directory
 * that the application is being run from.
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions", "unused"})
public class JARExtractor {
    /**
     * The directory where extracted files will be stored
     */
    private final File extractionDirectory;
    /**
     * The JAR file to be extracted
     */
    private final File jarToExtract;

    /**
     * A {@link java.util.List} of the extracted classes
     */
    private List<File> extractedFiles;

    public JARExtractor(String jarFile) {
        File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        while(!f.isDirectory()) { // Makes sure that we can run from a JAR
            f = f.getParentFile();
        }
        final String $PATH = String.format("%s/extractionDir", f.getAbsolutePath());
        System.out.println($PATH);
        extractionDirectory = new File($PATH);
        jarToExtract = new File(jarFile);
        extractedFiles = new CopyOnWriteArrayList<>();
    }

    /**
     * Extracts the JAR file
     */
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

    /**
     * Extraction wrapper method.
     *
     * @param pathToJar Path to the JAR file to be extracted
     * @param destDir Path to the extraction directory
     * @throws IOException If the extraction fails
     */
    private void extractJar(String pathToJar, String destDir) throws IOException {
        pickyExtract(pathToJar, destDir, "");
    }

    /**
     * Actually does the extraction
     *
     * @param pathToJar Path to the JAR file to be extracted
     * @param destDir Path to the extraction directory
     * @param ext File extension, anything without this extension will be
     *            ignored
     * @throws IOException
     */
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

    /**
     * Get the list of extracted files
     * @return The list of extracted files
     */
    public List<File> getExtractedFiles() {
        return extractedFiles;
    }

    /**
     * Get the directory that files were extracted to
     * @return The directory that files were extracted to
     */
    public File getExtractionDirectory() {
        return extractionDirectory;
    }
}
