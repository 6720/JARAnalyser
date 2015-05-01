package me.curlpipesh.analysis.descs;

import lombok.Data;
import org.objectweb.asm.TypePath;

/**
 * Description of a type annotation
 */
@Data
public class TypeAnnotationDesc {
    /**
     * Type reference of this type annotation
     */
    private final int typeRef;

    /**
     * {@link org.objectweb.asm.TypePath} of this type annotation
     */
    private final TypePath typePath;

    /**
     * Type description of this annotation
     */
    private final String description;

    /**
     * Whether or not this annotation is visible
     */
    private final boolean visible;

    /**
     * Array of values stored in this annotation. May be empty
     */
    private final Object[] values;

    public TypeAnnotationDesc(int a, TypePath b, String c, boolean d, Object[] e) {
        typeRef = a;
        typePath = b;
        description = c;
        visible = d;
        values = e;
    }
}
