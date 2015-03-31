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

@Data
public class MethodDesc {
    private final int accessLevel;
    private final String name;
    private final String description;
    private final String signature;
    private final String[] thrownExceptions;
    private final String[] annotations;
    private final MethodNode node;
    private final ClassNode owner;
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
