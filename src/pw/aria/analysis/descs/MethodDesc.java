package pw.aria.analysis.descs;

import lombok.Data;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import pw.aria.analysis.Main;
import pw.aria.analysis.impl.BetterClassAnalyser;
import pw.aria.analysis.util.DescHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;

/**
 * Description of a method
 */
@Data
public class MethodDesc {
    /**
     * Access modifier of the method
     */
    private final int accessLevel;

    /**
     * Name of the method
     */
    private final String name;

    /**
     * Type description of the method
     */
    private final String description;

    /**
     * Signature of the method
     */
    private final String signature;

    /**
     * Array of Strings representing the exceptions the method throws. May be
     * empty
     */
    private final String[] thrownExceptions;

    /**
     * Array of Strings representing the annotations on the method. May be
     * empty
     */
    private final String[] annotations;

    /**
     * {@link org.objectweb.asm.tree.MethodNode} for this method
     */
    private final MethodNode node;

    /**
     * {@link org.objectweb.asm.tree.ClassNode} to which this method belongs
     */
    private final ClassNode owner;

    /**
     * List of places where this method is invoked
     */
    private final List<String> methodCallLocations = new ArrayList<>();

    public MethodDesc(MethodNode n, ClassNode owner, int a, String b, String c, String d, String[] e) {
        accessLevel = a;
        name = b;
        description = c;
        signature = d;
        thrownExceptions = e;
        node = n;
        this.owner = owner;
        annotations = DescHelper.methodNodeAnnotations(node);
    }

    /**
     * Fills the invocation location list, if it has not already been done
     */
    @SuppressWarnings("unchecked")
    public void updateCallLocations() {
        if(methodCallLocations.size() > 0) {
            return;
        }
        for(Map.Entry<JarEntry, BetterClassAnalyser> e : Main.getAnalysers().entrySet()) {
            BetterClassAnalyser a = e.getValue();
            for(MethodDesc m : a.getMethods()) {
                Iterator<AbstractInsnNode> i = m.getNode().instructions.iterator();
                while (i.hasNext()) {
                    AbstractInsnNode insn = i.next();
                    if (insn instanceof MethodInsnNode) {
                        MethodInsnNode minsn = ((MethodInsnNode) insn);
                        if (minsn.name.equals(name)
                                && minsn.desc.equals(description)
                                && !name.equals("<init>")
                                && !name.equals("<clinit>")) {
                            methodCallLocations.add((m.getOwner().name.replaceAll("/", ".") + "#" + m.name + " invokes " + name).trim());
                            break;
                        }
                    }
                }
            }
        }
    }
}
