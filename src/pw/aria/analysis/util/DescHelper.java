package pw.aria.analysis.util;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;
import pw.aria.analysis.descs.ClassDesc;
import pw.aria.analysis.descs.FieldDesc;
import pw.aria.analysis.descs.MethodDesc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

@SuppressWarnings("unchecked")
public class DescHelper {
    private static final Printer printer = new Textifier();
    private static final TraceMethodVisitor mp = new TraceMethodVisitor(printer);

    public static boolean isPublic(int mod) {
        return (mod & ACC_PUBLIC) != 0;
    }

    public static boolean isProtected(int mod) {
        return (mod & ACC_PROTECTED) != 0;
    }

    public static boolean isPrivate(int mod) {
        return (mod & ACC_PRIVATE) != 0;
    }

    public static boolean isStatic(int mod) {
        return (mod & ACC_STATIC) != 0;
    }

    public static boolean isAbstract(int mod) {
        return (mod & ACC_ABSTRACT) != 0;
    }

    public static boolean isFinal(int mod) {
        return (mod & ACC_FINAL) != 0;
    }

    public static boolean isSynthetic(int mod) {
        return (mod & ACC_SYNTHETIC) != 0;
    }

    public static boolean isVolatile(int mod) {
        return (mod & ACC_VOLATILE) != 0;
    }

    public static boolean isBridge(int mod) {
        return (mod & ACC_BRIDGE) != 0;
    }

    public static boolean isSynchronized(int mod) {
        return (mod & ACC_SYNCHRONIZED) != 0;
    }

    public static boolean isInterface(int mod) {
        return (mod & ACC_INTERFACE) != 0;
    }

    public static boolean isEnum(int mod) {
        return (mod & ACC_ENUM) != 0;
    }

    public static boolean isAnnotation(int mod) {
        return (mod & ACC_ANNOTATION) != 0;
    }

    public static boolean isVoid(String desc) {
        return desc.endsWith("V");
    }

    public static boolean isBoolean(String desc) {
        return desc.endsWith("Z");
    }

    public static boolean isChar(String desc) {
        return desc.endsWith("C");
    }

    public static boolean isByte(String desc) {
        return desc.endsWith("B");
    }

    public static boolean isShort(String desc) {
        return desc.endsWith("S");
    }

    public static boolean isInt(String desc) {
        return desc.endsWith("I");
    }

    public static boolean isFloat(String desc) {
        return desc.endsWith("F");
    }

    public static boolean isLong(String desc) {
        return desc.endsWith("J");
    }

    public static boolean isDouble(String desc) {
        return desc.endsWith("D");
    }

    public static boolean isArray(String desc) {
        return desc.startsWith("[");
    }

    public static boolean isObject(String desc) {
        return desc.endsWith(";");
    }

    public static String getVersion(int java) {
        switch(java) {
            case 45:
                return "Java 1.1";
            case 46:
                return "Java 1.2";
            case 47:
                return "Java 1.3";
            case 48:
                return "Java 1.4";
            case 49:
                return "Java 5.0";
            case 50:
                return "Java 6.0";
            case 51:
                return "Java 7";
            case 52:
                return "Java 8";
            default:
                return "'Unknown Java Version'";
        }
    }

    public static String insnToString(AbstractInsnNode insn) {
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }

    public static String classToString(ClassDesc c) {
        StringBuilder sb = new StringBuilder();
        String tempName = c.getClassName();
        String[] tempArr = tempName.split("/");
        String className = tempArr[tempArr.length - 1];
        sb.append("// Decompiled ").append(className).append(".class\n").append("// Decompiled from ")
                .append(getVersion(c.getJavaVersion())).append(" (Bytecode version ").append(c.getJavaVersion())
                .append(")\n\n");
        if(tempName.contains("/")) {
            String pkg = tempName.replaceAll("/", ".").substring(0, tempName.length() - (className.length() + 1));
            sb.append("package ").append(pkg).append(";\n\n");
        }

        if(isPublic(c.getAccessLevel())) {
            sb.append("public ");
        } else if(isProtected(c.getAccessLevel())) {
            sb.append("protected ");
        } else if(isPrivate(c.getAccessLevel())) {
            sb.append("private ");
        }
        if(isStatic(c.getAccessLevel())) {
            sb.append("static ");
        }
        if(isFinal(c.getAccessLevel())) {
            sb.append("final ");
        } else if(isAbstract(c.getAccessLevel())) {
            sb.append("abstract ");
        }
        if(isAnnotation(c.getAccessLevel())) {
            sb.append("@interface ");
        } else if(isInterface(c.getAccessLevel())) {
            sb.append("interface ");
        } else if(isEnum(c.getAccessLevel())) {
            sb.append("enum ");
        } else {
            sb.append("class ");
        }

        sb.append(className);
        if(c.getSuperClassName() != null && !c.getSuperClassName().isEmpty() && !c.getSuperClassName().equals("java/lang/Object")) {
            sb.append(" extends ").append(c.getSuperClassName().replaceAll("/", "."));
        }
        if(c.getInterfaceNames() != null && c.getInterfaceNames().length > 0) {
            sb.append(" implements ");
            for(int i = 0; i < c.getInterfaceNames().length; i++) {
                if(c.getInterfaceNames()[i] == null) {
                    continue;
                }
                if(i == c.getInterfaceNames().length - 1) {
                    sb.append(c.getInterfaceNames()[i].replaceAll("/", "."));
                } else {
                    sb.append(c.getInterfaceNames()[i].replaceAll("/", ".")).append(", ");
                }
            }
        }

        String res = sb.toString().trim();
        if(res.endsWith(",")) {
            res = res.substring(0, res.length() - 1);
        }
        return res;
    }

    public static String fieldToString(FieldDesc f) {
        StringBuilder sb = new StringBuilder();
        if(isPublic(f.getAccessLevel())) {
            sb.append("public ");
        } else if(isProtected(f.getAccessLevel())) {
            sb.append("protected ");
        } else if(isPrivate(f.getAccessLevel())) {
            sb.append("private ");
        }
        if(isStatic(f.getAccessLevel())) {
            sb.append("static ");
        }
        if(isFinal(f.getAccessLevel())) {
            sb.append("final ");
        } else if(isVolatile(f.getAccessLevel())) {
            sb.append("volatile ");
        }
        if(isBoolean(f.getDescription())) {
            sb.append("boolean");
        } else if(isChar(f.getDescription())) {
            sb.append("char");
        } else if(isByte(f.getDescription())) {
            sb.append("byte");
        } else if(isShort(f.getDescription())) {
            sb.append("short");
        } else if(isInt(f.getDescription())) {
            sb.append("int");
        } else if(isFloat(f.getDescription())) {
            sb.append("float");
        } else if(isLong(f.getDescription())) {
            sb.append("long");
        } else if(isDouble(f.getDescription())) {
            sb.append("double");
        } else if(isObject(f.getDescription())) {
            sb.append(f.getDescription().replaceFirst("\\((.*)\\)", "").replaceFirst("L", "").replaceFirst(";", "")
                    .replaceAll("/", ".").replaceFirst("\\[", "").trim());
        } else {
            sb.append("unknown");
        }
        if(isArray(f.getDescription())) {
            sb.append("[]");
        }
        sb.append(" ").append(f.getName()).append(" = ");
        if(f.getValue() != null) {
            if(isLong(f.getDescription())) {
                sb.append(f.getValue()).append("; // 0x").append(Long.toHexString((Long) f.getValue()));
            } else if(isInt(f.getDescription())) {
                sb.append(f.getValue()).append("; // 0x").append(Integer.toHexString((Integer) f.getValue()));
            } else {
                sb.append(f.getValue()).append(";");
            }
        } else {
            if(isBoolean(f.getDescription())) {
                sb.append("false");
            } else if(isObject(f.getDescription())) {
                sb.append("null");
            } else {
                sb.append("0");
            }
            sb.append(";");
        }

        return sb.toString();
    }

    public static String methodToString(MethodDesc m) {
        StringBuilder sb = new StringBuilder();
        if(m.getName().equals("<clinit>")) {
            sb.append("static");
        } else {
            int counter = 0;
            for (String e : m.getAnnotations()) {
                if (counter > 0) {
                    sb.append("    ");
                }
                sb.append(e).append("\n");
                ++counter;
            }
            if (m.getAnnotations().length > 0) {
                sb.append("    ");
            }
            String tempDesc = "";
            if (isPublic(m.getAccessLevel())) {
                sb.append("public ");
            } else if (isProtected(m.getAccessLevel())) {
                sb.append("protected ");
            } else if (isPrivate(m.getAccessLevel())) {
                sb.append("private ");
            }
            if (!m.getName().equals("<init>")) {
                if (isStatic(m.getAccessLevel())) {
                    sb.append("static ");
                }
                if (isAbstract(m.getAccessLevel())) {
                    sb.append("abstract ");
                }
                if (isSynthetic(m.getAccessLevel())) {
                    sb.append("/*synthetic*/ ");
                }
                if (isBridge(m.getAccessLevel())) {
                    sb.append("/*bridge*/ ");
                }
                if (isSynchronized(m.getAccessLevel())) {
                    sb.append("synchronized ");
                }
                if (isFinal(m.getAccessLevel())) {
                    sb.append("final ");
                }

                if (isVoid(m.getDescription())) {
                    sb.append("void");
                } else if (isBoolean(m.getDescription())) {
                    sb.append("boolean");
                } else if (isChar(m.getDescription())) {
                    sb.append("char");
                } else if (isByte(m.getDescription())) {
                    sb.append("byte");
                } else if (isShort(m.getDescription())) {
                    sb.append("short");
                } else if (isInt(m.getDescription())) {
                    sb.append("int");
                } else if (isFloat(m.getDescription())) {
                    sb.append("float");
                } else if (isLong(m.getDescription())) {
                    sb.append("long");
                } else if (isDouble(m.getDescription())) {
                    sb.append("double");
                } else if (isObject(m.getDescription())) {
                    tempDesc = m.getDescription().replaceFirst("\\((.*)\\)L", "").replaceFirst(";", "").replaceAll("/", ".")
                            .replaceAll("\\((.*)\\)", "").trim();
                    sb.append(tempDesc.replaceFirst("\\[L", ""));
                } else {
                    sb.append("unknown");
                }
                // Hideously ugly, I know qwq
                if (!tempDesc.equals("")) {
                    if (isArray(tempDesc)) {
                        sb.append("[]");
                    }
                } else {
                    if (isArray(m.getDescription())) {
                        sb.append("[]");
                    }
                }
                sb.append(" ").append(m.getName());
            } else {
                String[] e = m.getOwner().name.split("/");
                sb.append(e[e.length - 1]);
            }
            sb.append("(");
            Type[] args = Type.getArgumentTypes(m.getDescription());
            for (int i = 0; i < args.length; i++) {
                if (i == args.length - 1) {
                    sb.append(args[i].getClassName()).append(" param").append(i);
                } else {
                    sb.append(args[i].getClassName()).append(" param").append(i).append(", ");
                }
            }
            sb.append(")");
            if (m.getThrownExceptions() != null) {
                if (m.getThrownExceptions().length > 0) {
                    sb.append(" throws ");
                    for (int i = 0; i < m.getThrownExceptions().length; i++) {
                        if (i == m.getThrownExceptions().length - 1) {
                            sb.append(m.getThrownExceptions()[i].replaceAll("/", "."));
                        } else {
                            sb.append(m.getThrownExceptions()[i].replaceAll("/", ".")).append(", ");
                        }
                    }
                }
            }
            if (isAbstract(m.getAccessLevel())) {
                sb.append(";");
                return sb.toString();
            }
        }
        sb.append(" {\n");
        Iterator<AbstractInsnNode> i = m.getNode().instructions.iterator();
        while(i.hasNext()) {
            AbstractInsnNode insn = i.next();
            /*sb.append("        // Type: ").append(insn.getType())
                    .append(", opcode: ").append(insn.getOpcode()).append("\n");*/
            sb.append("        // ");
            if(insn instanceof LabelNode) {
                sb.append(insnToString(insn).trim());
            } else {
                sb.append(" ").append(insnToString(insn).trim());
            }
            sb.append("\n");
        }
        sb.append("    }");


        return sb.toString();
    }
}
