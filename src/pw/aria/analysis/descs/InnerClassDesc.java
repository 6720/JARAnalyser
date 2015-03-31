package pw.aria.analysis.descs;

import lombok.Data;

@Data
public class InnerClassDesc {
    private final String name;
    private final String outerName;
    private final String innerName;
    private final int accessLevel;

    public InnerClassDesc(String a, String b, String c, int d) {
        name = a;
        outerName = b;
        innerName = c;
        accessLevel = d;
    }
}
