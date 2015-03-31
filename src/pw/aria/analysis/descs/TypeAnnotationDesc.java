package pw.aria.analysis.descs;

import lombok.Data;
import org.objectweb.asm.TypePath;

@Data
public class TypeAnnotationDesc {
    private final int typeRef;
    private final TypePath typePath;
    private final String description;
    private final boolean visible;
    private final Object[] values;

    public TypeAnnotationDesc(int a, TypePath b, String c, boolean d, Object[] e) {
        typeRef = a;
        typePath = b;
        description = c;
        visible = d;
        values = e;
    }
}
