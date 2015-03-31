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

@Data
public class FieldDesc {
    private final int accessLevel;
    private final String name;
    private final String description;
    private final String signature;
    private final Object value;
    private final FieldNode node;
    private final String[] annotations;
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
