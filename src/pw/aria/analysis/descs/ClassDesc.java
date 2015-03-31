package pw.aria.analysis.descs;

import lombok.Data;

@Data
public class ClassDesc {
    private String className;
    private String classSignature;
    private String superClassName;
    private String[] interfaceNames;
    private int accessLevel;
    private int javaVersion;

    public ClassDesc(String a, String b, String c, String[] d, int e, int f) {
        className = a;
        classSignature = b;
        superClassName = c;
        interfaceNames = d;
        accessLevel = e;
        javaVersion = f;
    }
}
