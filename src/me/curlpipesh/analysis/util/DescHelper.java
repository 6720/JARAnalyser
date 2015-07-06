package me.curlpipesh.analysis.util;

import me.curlpipesh.analysis.descs.ClassDesc;
import me.curlpipesh.analysis.descs.FieldDesc;
import me.curlpipesh.analysis.descs.MethodDesc;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;
import me.curlpipesh.analysis.impl.BetterClassAnalyser;
import sun.reflect.generics.parser.SignatureParser;
import sun.reflect.generics.tree.ClassSignature;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/**
 * Utility class to make dealing with the various classes in
 * <tt>pw.aria.analysis.descs</tt> easier.
 */
@SuppressWarnings("unchecked")
public class DescHelper {
    /**
     * Used for converting arbitrary {@link org.objectweb.asm.tree.AbstractInsnNode}s
     * to String-form.
     */
    private static final Printer printer = new Textifier();

    /**
     * Used for converting arbitrary {@link org.objectweb.asm.tree.AbstractInsnNode}s
     * to String-form.
     */
    private static final TraceMethodVisitor mp = new TraceMethodVisitor(printer);

    /**
     * Tells whether a given access modifier is public
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is public, false otherwise
     */
    public static boolean isPublic(int mod) {
        return (mod & ACC_PUBLIC) != 0;
    }

    /**
     * Tells whether a given access modifier is protected
     * @param mod The access modifier to check
     * @return True if the access modifier is protected, false otherwise
     */
    public static boolean isProtected(int mod) {
        return (mod & ACC_PROTECTED) != 0;
    }

    /**
     * Tells whether a given access modifier is private
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is private, false otherwise
     */
    public static boolean isPrivate(int mod) {
        return (mod & ACC_PRIVATE) != 0;
    }

    /**
     * Tells whether a given access modifier is static
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is static, false otherwise
     */
    public static boolean isStatic(int mod) {
        return (mod & ACC_STATIC) != 0;
    }

    /**
     * Tells whether a given access modifier is abstract
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is abstract, false otherwise
     */
    public static boolean isAbstract(int mod) {
        return (mod & ACC_ABSTRACT) != 0;
    }

    /**
     * Tells whether a given access modifier is final
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is final, false otherwise
     */
    public static boolean isFinal(int mod) {
        return (mod & ACC_FINAL) != 0;
    }

    /**
     * Tells whether a given access modifier is synthetic. A modifier is
     * synthetic if it is marked with the ACC_SYNTHETIC flag (0x1000), as
     * specified in JLS8, 4.6 <tt>Methods</tt>.
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is synthetic, false otherwise
     */
    public static boolean isSynthetic(int mod) {
        return (mod & ACC_SYNTHETIC) != 0;
    }

    /**
     * Tells whether a given access modifier is volatile
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is volatile, false otherwise
     */
    public static boolean isVolatile(int mod) {
        return (mod & ACC_VOLATILE) != 0;
    }

    /**
     * Tells whether a given access modifier is bridge. A modifier is bridge if
     * it is marked with the ACC_BRIDGE flag (0x0040), as specified in
     * JLS8, 4.6 <tt>Methods</tt>.
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is bridge, false otherwise
     */
    public static boolean isBridge(int mod) {
        return (mod & ACC_BRIDGE) != 0;
    }

    /**
     * Tells whether a given access modifier is synchronized. A modifier is
     * synchronized if it is marked with the ACC_SYNCHRONIZED flag (0x0020), as
     * specified in JLS8, 4.6 <tt>Methods</tt> and
     * JLS8, 2.11.10 <tt>Synchronization</tt>
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is synchronized, false otherwise
     */
    public static boolean isSynchronized(int mod) {
        return (mod & ACC_SYNCHRONIZED) != 0;
    }

    /**
     * Tells whether a given access modifier is interface
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is interface, false otherwise
     */
    public static boolean isInterface(int mod) {
        return (mod & ACC_INTERFACE) != 0;
    }

    /**
     * Tells whether a given access modifier is enum
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is enum, false otherwise
     */
    public static boolean isEnum(int mod) {
        return (mod & ACC_ENUM) != 0;
    }

    /**
     * Tells whether a given access modifier is annotation (@interface)
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is annotation, false otherwise
     */
    public static boolean isAnnotation(int mod) {
        return (mod & ACC_ANNOTATION) != 0;
    }

    /**
     * Tells whether a given access modifier is deprecated
     *
     * @param mod The access modifier to check
     * @return True if the access modifier is deprecated, false otherwise
     */
    public static boolean isDeprecated(int mod) {
        return (mod & ACC_DEPRECATED) != 0;
    }

    /**
     * Tells whether a given type is a void
     *
     * @param desc The type description to check
     * @return True if the type is a void, false otherwise
     */
    public static boolean isVoid(String desc) {
        return desc.endsWith("V");
    }

    /**
     * Tells whether a given type is a boolean
     *
     * @param desc The type description to check
     * @return True if the type is a boolean, false otherwise
     */
    public static boolean isBoolean(String desc) {
        return desc.endsWith("Z");
    }

    /**
     * Tells whether a given type is a char
     *
     * @param desc The type description to check
     * @return True if the type is a char, false otherwise
     */
    public static boolean isChar(String desc) {
        return desc.endsWith("C");
    }

    /**
     * Tells whether a given type is a byte
     *
     * @param desc The type description to check
     * @return True if the type is a byte, false otherwise
     */
    public static boolean isByte(String desc) {
        return desc.endsWith("B");
    }

    /**
     * Tells whether a given type is a short
     *
     * @param desc The type description to check
     * @return True if the type is a short, false otherwise
     */
    public static boolean isShort(String desc) {
        return desc.endsWith("S");
    }

    /**
     * Tells whether a given type is an int
     *
     * @param desc The type description to check
     * @return True if the type is an int, false otherwise
     */
    public static boolean isInt(String desc) {
        return desc.endsWith("I");
    }

    /**
     * Tells whether a given type is a float
     *
     * @param desc The type description to check
     * @return True if the type is a float, false otherwise
     */
    public static boolean isFloat(String desc) {
        return desc.endsWith("F");
    }

    /**
     * Tells whether a given type is a long
     *
     * @param desc The type description to check
     * @return True if the type is a long, false otherwise
     */
    public static boolean isLong(String desc) {
        return desc.endsWith("J");
    }

    /**
     * Tells whether a given type is a double
     *
     * @param desc The type description to check
     * @return True if the type is a double, false otherwise
     */
    public static boolean isDouble(String desc) {
        return desc.endsWith("D");
    }

    /**
     * Tells whether a given type is an array
     *
     * @param desc The type description to check
     * @return True if the type is an array, false otherwise
     */
    public static boolean isArray(String desc) {
        return desc.startsWith("[");
    }

    /**
     * Tells whether a given type is an Object
     *
     * @param desc The type description to check
     * @return True if the type is an Object, false otherwise
     */
    public static boolean isObject(String desc) {
        return desc.endsWith(";");
    }

    /**
     * Tells whether the given method signature is generic. The method
     * is considered generic if its signature ends with something along the
     * lines of ")TV;"
     *
     * @param desc The method signature to check
     * @return True if the method signature is generic, false otherwise
     */
    public static boolean isMethodReturnTypeGeneric(String desc) {
        return desc.contains(")T");
    }

    /**
     * Tells whether the given field description+signature is generic.
     *
     * @param desc Description of the field
     * @param signature Signature of the field
     * @return True if the field is generic, false otherwise
     */
    public static boolean isFieldGeneric(String desc, String signature) {
        return
                signature != null
                && desc != null
                && signature.startsWith("T")
                && signature.endsWith(";")
                && Character.isUpperCase(signature.charAt(1))
                && desc.contains("java/lang/Object");
    }

    /**
     * Converts a Java major version number into a more human-readable String.
     *
     * @param java The Java major version number
     * @return The human-readable version number
     */
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

    /**
     * Returns a String representation of a given JVM instruction
     *
     * @param insn The instruction to textualise
     * @return The String representation of the given instruction
     */
    public static String insnToString(AbstractInsnNode insn) {
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }

    /**
     * Returns a String representation of a given class access level
     * @param access The access level
     * @return A String representatino of a given class access level
     */
    public static String classAccessLevelToString(int access) {
        StringBuilder sb = new StringBuilder();
        if(isPublic(access)) {
            sb.append("public ");
        } else if(isProtected(access)) {
            sb.append("protected ");
        } else if(isPrivate(access)) {
            sb.append("private ");
        }
        if(isStatic(access)) {
            sb.append("static ");
        }
        if(isFinal(access)) {
            sb.append("final ");
        } else if(isAbstract(access)) {
            sb.append("abstract ");
        }
        if(isAnnotation(access)) {
            sb.append("@interface ");
        } else if(isInterface(access)) {
            sb.append("interface ");
        } else if(isEnum(access)) {
            sb.append("enum ");
        } else {
            sb.append("class ");
        }
        return sb.toString();
    }

    /**
     * Creates a <tt>String[]</tt> out of the annotations on a given MethodNode
     * Ex: {"@EventHandler", "@RuntimePriority(Priotity.HIGH)"}
     *
     * @param node The node to get annotations from
     * @return An array of String representations of the node's annotations
     */
    public static String[] methodNodeAnnotations(MethodNode node) {
        return annotationListToStringArray(node.visibleAnnotations);
    }

    /**
     * Creates a <tt>String[]</tt> out of the annotations on a given FieldNode
     * Ex: {"@EventHandler", "@RuntimePriority(Priotity.HIGH)"}
     *
     * @param node The node to get annotations from
     * @return An array of String representations of the node's annotations
     */
    public static String[] fieldNodeAnnotations(FieldNode node) {
        return annotationListToStringArray(node.visibleAnnotations);
    }

    /**
     * Creates a <tt>String[]</tt> out of the annotations on a given ClassNode
     * Ex: {"@EventHandler", "@RuntimePriority(Priotity.HIGH)"}
     *
     * @param node The node to get annotations from
     * @return An array of String representations of the node's annotations
     */
    public static String[] classNodeAnnotations(ClassNode node) {
        return annotationListToStringArray(node.visibleAnnotations);
    }

    /**
     * Does the heavy lifting for {@link #methodNodeAnnotations},
     * {@link #fieldNodeAnnotations}, and {@link #classNodeAnnotations}.
     *
     * @param annotationsList The list of {@link org.objectweb.asm.tree.AnnotationNode}s
     *                        to turn into a String array.
     * @return An array of String representations of the given annotations
     */
    private static String[] annotationListToStringArray(List<AnnotationNode> annotationsList) {
        String[] annotations;
        if(annotationsList != null) {
            if(annotationsList.size() > 0) {
                annotations = new String[annotationsList.size()];
                for(int i = 0; i < annotationsList.size(); i++) {
                    AnnotationNode an = annotationsList.get(i);
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
        return annotations;
    }

    /**
     * Creates a more "readable" form of the {@link me.curlpipesh.analysis.descs.ClassDesc}
     * given. Used in the "bytecode viewing" pane of the GUI.
     */
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
        if(isDeprecated(c.getAccessLevel())) {
            sb.append("@Deprecated\n");
        }
        for(String e : c.getAnnotations()) {
            if(e.contains("java.lang.Deprecated")) {
                continue;
            }
            sb.append(e).append("\n");
        }
        sb.append(classAccessLevelToString(c.getAccessLevel())).append(className);

        if(!isEnum(c.getAccessLevel()) && !isAnnotation(c.getAccessLevel())) {
            if (c.getClassSignature() != null) {
                if (!c.getClassSignature().isEmpty()) {
                    ClassSignature sig = SignatureParser.make().parseClassSig(c.getClassSignature());
                    sb.append("<");
                    for (int i = 0; i < sig.getFormalTypeParameters().length; i++) {
                        if (i != sig.getFormalTypeParameters().length - 1) {
                            sb.append(sig.getFormalTypeParameters()[i].getName()).append(", ");
                        } else {
                            sb.append(sig.getFormalTypeParameters()[i].getName());
                        }
                    }
                    sb.append(">");
                }
            }
        }

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

    /**
     * Creates a more "readable" form of the {@link me.curlpipesh.analysis.descs.FieldDesc}
     * given. Used in the "bytecode viewing" pane of the GUI.
     */
    public static String fieldToString(FieldDesc f) {
        StringBuilder sb = new StringBuilder();
        if(isDeprecated(f.getAccessLevel())) {
            sb.append("@Deprecated\n");
        }
        int counter = 0;
        for(String e : f.getAnnotations()) {
            if (counter > 0) {
                sb.append("    ");
            }
            sb.append(e).append("\n");
            ++counter;
        }
        if(f.getAnnotations().length > 0) {
            sb.append("    ");
        }
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
            if(isFieldGeneric(f.getDescription(), f.getSignature())) {
                sb.append(f.getSignature().replaceFirst("T", "").replaceFirst(";", ""));
            } else {
                sb.append(f.getDescription().replaceFirst("\\((.*)\\)", "").replaceFirst("L", "").replaceFirst(";", "")
                        .replaceAll("/", ".").replaceFirst("\\[", "").trim());
            }
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
                if(isObject(f.getDescription())) {
                    if(f.getDescription().contains("java/lang/String")) {
                        if(!isArray(f.getDescription())) {
                            sb.append('"').append(f.getValue()).append('"');
                        }
                    } else {
                        sb.append(f.getValue()).append(";");
                    }
                }
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

    /**
     * Creates a more "readable" form of the {@link me.curlpipesh.analysis.descs.MethodDesc}
     * given. Used in the "bytecode viewing" pane of the GUI.
     */
    public static String methodToString(MethodDesc m) {
        StringBuilder sb = new StringBuilder();
        if(m.getName().equals("<clinit>")) {
            sb.append("static");
        } else {
            if(isDeprecated(m.getAccessLevel())) {
                sb.append("@Deprecated\n");
            }
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
                    if(m.getSignature() != null) {
                        if(!m.getSignature().isEmpty()) {
                            if (isMethodReturnTypeGeneric(m.getSignature())) {
                                String[] temp = m.getSignature().split("\\)");
                                String generic = temp[temp.length - 1].replaceFirst("T", "").replaceFirst(";", "");
                                sb.append(generic);
                            }
                        }
                    } else {
                        tempDesc = m.getDescription().replaceFirst("\\((.*)\\)L", "").replaceFirst(";", "").replaceAll("/", ".")
                                .replaceAll("\\((.*)\\)", "").trim();
                        sb.append(tempDesc.replaceFirst("\\[L", ""));
                    }
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

    public static String betterClassAnalyserToString(BetterClassAnalyser analyser) {
        StringBuilder sb = new StringBuilder();
        sb.append(classToString(analyser.getClassDesc())).append(" {\n");
        if(analyser.getFields().size() > 0) {
            for(FieldDesc f : analyser.getFields()) {
                sb.append("    ").append(fieldToString(f)).append("\n");
            }
            if(analyser.getMethods().size() > 0) {
                sb.append("\n");
            }
        }
        if(analyser.getMethods().size() > 0) {
            int i = 0;
            for(MethodDesc m : analyser.getMethods()) {
                sb.append("    ").append(methodToString(m)).append("\n");
                if(i != analyser.getMethods().size() - 1) {
                    sb.append("\n");
                }
                ++i;
            }
        }
        sb.append("}\n");
        return sb.toString().trim();
    }
}
