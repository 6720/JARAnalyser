package pw.aria.analysis.descs;

import lombok.Data;
import org.objectweb.asm.tree.ClassNode;
import pw.aria.analysis.util.DescHelper;

@Data
public class ClassDesc {
    private final String className;
    private final String classSignature;
    private final String superClassName;
    private final String[] interfaceNames;
    private final int accessLevel;
    private final int javaVersion;
    private final String[] annotations;
    private final ClassNode node;

    public ClassDesc(ClassNode n, String a, String b, String c, String[] d, int e, int f) {
        className = a;
        classSignature = b;
        superClassName = c;
        interfaceNames = d;
        accessLevel = e;
        javaVersion = f;
        node = n;
        annotations = DescHelper.classNodeAnnotations(node);
    }
}
