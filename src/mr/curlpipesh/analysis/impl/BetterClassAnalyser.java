package mr.curlpipesh.analysis.impl;

import lombok.Data;
import mr.curlpipesh.analysis.descs.*;
import mr.curlpipesh.analysis.util.DescHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Data
public class BetterClassAnalyser {
    /**
     * {@link org.objectweb.asm.ClassReader} for the class being analysed
     */
    private ClassReader cr;
    /**
     * {@link org.objectweb.asm.tree.ClassNode} for the class being analysed
     */
    private ClassNode cn;

    /**
     * {@link mr.curlpipesh.analysis.descs.ClassDesc} of the class being analysed
     */
    private ClassDesc classDesc;

    /**
     * {@link mr.curlpipesh.analysis.descs.MethodDesc}s for the class being analysed
     */
    private final List<MethodDesc> methods = new ArrayList<>();

    /**
     * {@link mr.curlpipesh.analysis.descs.FieldDesc}s for the class being analysed
     */
    private final List<FieldDesc> fields = new ArrayList<>();

    /**
     * {@link mr.curlpipesh.analysis.descs.AnnotationDesc}s for the class being analysed
     */
    private final List<AnnotationDesc> annotations = new ArrayList<>();

    /**
     * {@link mr.curlpipesh.analysis.descs.TypeAnnotationDesc}s for the class being analysed
     */
    private final List<TypeAnnotationDesc> typeAnnotations = new ArrayList<>();

    /**
     * {@link mr.curlpipesh.analysis.descs.InnerClassDesc}s for the class being analysed
     */
    private final List<InnerClassDesc> innerClasses = new ArrayList<>();

    public BetterClassAnalyser(ClassReader c) {
        this.cr = c;
        cn = new ClassNode();
        cr.accept(cn, 0);
    }

    /**
     * Does the actual analysis of the class
     */
    @SuppressWarnings("unchecked")
    public void analyze() {
        // Main class desc
        classDesc = new ClassDesc(cn, cn.name, cn.signature, cn.superName,
                (String[])cn.interfaces.toArray(new String[cn.innerClasses.size()]), cn.access, cn.version);
        // Method descs
        methods.addAll(((List<MethodNode>) cn.methods).stream().map(m -> new MethodDesc(m, cn, m.access, m.name, m.desc, m.signature,
                (String[]) m.exceptions.toArray(new String[m.exceptions.size()]))).collect(Collectors.toList()));
        // Field descs
        fields.addAll(((List<FieldNode>) cn.fields).stream().map(f -> new FieldDesc(f, cn, f.access, f.name, f.desc, f.signature, f.value))
                .collect(Collectors.toList()));
        // Annotation descs
        try { // Under some conditions (unknown as of yet), the map() call seems to cause NPEs.
              // This is probably due to "a.values.toArray()", but is still to be ascertained.
            if (cn.visibleAnnotations != null) {
                annotations.addAll(((List<AnnotationNode>) cn.visibleAnnotations).stream()
                        .map(a -> new AnnotationDesc(a.desc, true, a.values.toArray())).collect(Collectors.toList()));
            }
        } catch(Exception e) {
            System.err.println("Error processing " + cn.name + ":");
            e.printStackTrace();
        }
        try {
            if (cn.invisibleAnnotations != null) {
                annotations.addAll(((List<AnnotationNode>) cn.invisibleAnnotations).stream()
                        .map(a -> new AnnotationDesc(a.desc, true, a.values.toArray())).collect(Collectors.toList()));
            }
        } catch(Exception e) {
            System.err.println("Error processing " + cn.name + ":");
            e.printStackTrace();
        }
        // Type annotation descs
        try {
            if (cn.visibleTypeAnnotations != null) {
                typeAnnotations.addAll(((List<TypeAnnotationNode>) cn.visibleTypeAnnotations).stream()
                        .map(t -> new TypeAnnotationDesc(t.typeRef, t.typePath, t.desc, true, t.values.toArray())).collect(Collectors.toList()));
            }
        } catch(Exception e) {
            System.err.println("Error processing " + cn.name + ":");
            e.printStackTrace();
        }
        try {
            if (cn.invisibleTypeAnnotations != null) {
                typeAnnotations.addAll(((List<TypeAnnotationNode>) cn.invisibleTypeAnnotations).stream()
                        .map(t -> new TypeAnnotationDesc(t.typeRef, t.typePath, t.desc, true, t.values.toArray())).collect(Collectors.toList()));
            }
        } catch(Exception e) {
            System.err.println("Error processing " + cn.name + ":");
            e.printStackTrace();
        }
        // Inner classes
        innerClasses.addAll(((List<InnerClassNode>) cn.innerClasses).stream()
                .map(i -> new InnerClassDesc(i.name, i.outerName, i.innerName, i.access)).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        /*StringBuilder sb = new StringBuilder();
        // Main class information
        sb.append(DescHelper.classToString(classDesc)).append(" {\n");
        // Field information
        if(fields.size() > 0) {
            for (FieldDesc m : fields) {
                sb.append("    ").append(DescHelper.fieldToString(m)).append("\n");
            }
            sb.append("\n");
        }
        // Method information
        if(methods.size() > 0) {
            for (MethodDesc m : methods) {
                sb.append("    ").append(DescHelper.methodToString(m)).append("\n");
            }
        }
        sb.append("}\n");

        return sb.toString();*/
        return DescHelper.betterClassAnalyserToString(this);
    }

    /**
     * Condenses the information gathered in the analysis into a somewhat
     * succinct analysis report
     * @return The information gathered from the analysis
     */
    public String getFieldAnalysis() {
        StringBuilder sb = new StringBuilder();
        String[] e = classDesc.getClassName().split("/");
        String tmp = "";
        for(int i = 0; i < e[e.length - 1].length(); i++) {
            tmp += "-";
        }
        sb.append("Field Access Analysis of ").append(e[e.length - 1]).append(":\n")
          .append("-------------------------").append(tmp).append("-\n");
        for(FieldDesc f : fields) {
            sb.append(" * ").append(f.getName()).append("\n");
            for(String s : f.getFieldAccessLocations()) {
                sb.append("   - ").append(s).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Condenses the information gathered in the analysis into a somewhat
     * succinct analysis report
     * @return The information gathered from the analysis
     */
    public String getMethodAnalysis() {
        StringBuilder sb = new StringBuilder();
        String[] e = classDesc.getClassName().split("/");
        String tmp = "";
        for(int i = 0; i < e[e.length - 1].length(); i++) {
            tmp += "-";
        }
        sb.append("Method Invocation Analysis of ").append(e[e.length - 1]).append(":\n")
          .append("------------------------------").append(tmp).append("-\n");
        for(MethodDesc m : methods) {
            sb.append(" * ").append(m.getName()).append("\n");
            for(String s : m.getMethodCallLocations()) {
                sb.append("   - ").append(s).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns interesting statistics about the analysed class
     * @return Interesting statistics about the analysed class
     */
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        String[] e = classDesc.getClassName().split("/");
        String tmp = "";
        for(int i = 0; i < e[e.length - 1].length(); i++) {
            tmp += "-";
        }
        sb.append("Statistics for ").append(e[e.length - 1]).append(":\n");
        sb.append("---------------").append(tmp).append("-\n");
        sb.append("Methods: ").append(methods.size()).append("\n")
                .append("Fields: ").append(fields.size()).append("\n")
                .append("Compiled for: ").append(DescHelper.getVersion(classDesc.getJavaVersion())).append("\n")
                .append("Has static initialiser: ")
                .append(methods.parallelStream().filter(m -> m.getName().equals("<clinit>")).count() == 1);
        return sb.toString().trim();
    }
}
