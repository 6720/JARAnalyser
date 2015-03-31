package pw.aria.analysis.descs;

import lombok.Data;

@Data
public class AnnotationDesc {
    private final String description;
    private final boolean visible;
    private final Object[] values;

    public AnnotationDesc(String a, boolean b, Object[] c) {
        description = a;
        visible = b;
        values = c;
    }
}
