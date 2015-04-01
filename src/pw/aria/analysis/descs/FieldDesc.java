package pw.aria.analysis.descs;

import lombok.Data;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import pw.aria.analysis.Main;
import pw.aria.analysis.impl.BetterClassAnalyser;
import pw.aria.analysis.util.DescHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;

/**
 * Description of a field
 */
@Data
public class FieldDesc {
    /**
     * Access modifier for the field
     */
    private final int accessLevel;

    /**
     * Name of the field
     */
    private final String name;

    /**
     * Type description of the field
     */
    private final String description;

    /**
     * Signature of the field
     *
     * TODO: Can this be used to get information about generics?
     */
    private final String signature;

    /**
     * Default value of the field. May be null
     */
    private final Object value;

    /**
     * {@link org.objectweb.asm.tree.FieldNode} for this field
     */
    private final FieldNode node;

    /**
     * Array of String representations of all annotations on this field. May be
     * empty
     */
    private final String[] annotations;

    /**
     * List of places where this field has been accessed
     */
    private final List<String> fieldAccessLocations = new ArrayList<>();

    public FieldDesc(FieldNode n, int a, String b, String c, String d, Object e) {
        accessLevel = a;
        name = b;
        description = c;
        signature = d;
        value = e;
        node = n;
        annotations = DescHelper.fieldNodeAnnotations(node);
    }

    /**
     * Fills the access location list, if it has not already been done
     */
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public void updateAccessLocations() {
        if(fieldAccessLocations.size() > 0) {
            return;
        }
        for(Map.Entry<JarEntry, BetterClassAnalyser> e : Main.getAnalysers().entrySet()) {
            BetterClassAnalyser a = e.getValue();
            for(MethodDesc m : a.getMethods()) {
                Iterator<AbstractInsnNode> i = m.getNode().instructions.iterator();
                while (i.hasNext()) {
                    AbstractInsnNode insn = i.next();
                    if (insn instanceof FieldInsnNode) {
                        FieldInsnNode finsn = ((FieldInsnNode) insn);
                        if (finsn.name.equals(name)
                                && finsn.desc.equals(description)) {
                            fieldAccessLocations.add((m.getOwner().name.replaceAll("/", ".") + "#" + m.getName() + " accesses " + name).trim());
                            break;
                        }
                    }
                }
            }
        }
    }
}
