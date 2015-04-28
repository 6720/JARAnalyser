package mr.curlpipesh.analysis.descs;

import lombok.Data;
import mr.curlpipesh.analysis.util.DescHelper;
import org.objectweb.asm.tree.ClassNode;

/**
 * Description of a class
 */
@Data
public class ClassDesc {
    /**
     * Fully-qualified name of the class
     */
    private final String className;

    /**
     * Signature of the class
     *
     * TODO: Does this contain information about generics?
     */
    private final String classSignature;

    /**
     * Fully-qualified name of the class' superclass
     */
    private final String superClassName;

    /**
     * Array of fully-qualified names of interfaces the class implements
     */
    private final String[] interfaceNames;

    /**
     * Access modifier for the class
     */
    private final int accessLevel;

    /**
     * Major version of Java that the class was compiled for
     */
    private final int javaVersion;

    /**
     * Array of String representations of annotations on the class. May be
     * empty
     */
    private final String[] annotations;

    /**
     * {@link org.objectweb.asm.tree.ClassNode} for the class
     */
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
