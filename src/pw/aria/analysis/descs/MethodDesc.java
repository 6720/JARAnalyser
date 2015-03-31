package pw.aria.analysis.descs;

import lombok.Data;
import org.objectweb.asm.tree.*;
import pw.aria.analysis.Main;
import pw.aria.analysis.impl.BetterClassAnalyser;

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
        if(node.visibleAnnotations != null) {
            if(node.visibleAnnotations.size() > 0) {
                annotations = new String[node.visibleAnnotations.size()];
                for(int i = 0; i < node.visibleAnnotations.size(); i++) {
                    AnnotationNode an = (AnnotationNode) node.visibleAnnotations.get(i);
                    annotations[i] = "@" + an.desc.replaceFirst("L", "").replaceFirst(";", "").replaceAll("/", ".");
                    if(an.values != null) {
                        int counter = 0;
                        annotations[i] += "(";
                        for (Object o : an.values) {
                            String oString = o.toString();
                            if(o.getClass().isArray()) {
                                boolean wasReallyAFieldReference = false;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("{");
                                for(int z = 0; z < ((Object[])o).length; z++) {
                                    if(z == ((Object[])o).length - 1) {
                                        sb2.append(((Object[])o)[z]);
                                    } else {
                                        String tempOString = ((Object[])o)[z].toString();
                                        // We can probably safely assume that
                                        // this is something like
                                        // {Lcom/example/CustomEnum;, TESTENUMOPTION}
                                        if(tempOString.startsWith("L") && tempOString.endsWith(";")) {
                                            wasReallyAFieldReference = true;
                                            sb2.append(tempOString.replaceFirst("L", "").replaceFirst(";", "")
                                                    .replaceAll("/", ".").trim())
                                                    .append(".").append(((Object[]) o)[++z]);
                                        } else {
                                            sb2.append(((Object[]) o)[z]).append(", ");
                                        }
                                    }
                                }
                                sb2.append("}");
                                oString = sb2.toString().trim();
                                if(wasReallyAFieldReference && an.values.size() == 2) {
                                    oString = oString.replaceFirst("\\{", "").replaceFirst("\\}", "");
                                }
                            }
                            annotations[i] += oString + (counter % 2 == 0 ? " = " : o.equals(an.values.get(an.values.size() - 1)) ? "" : ", ");
                            ++counter;
                        }
                        annotations[i] += ")";
                    }
                }
            } else {
                annotations = new String[0];
            }
        } else {
            annotations = new String[0];
        }
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
