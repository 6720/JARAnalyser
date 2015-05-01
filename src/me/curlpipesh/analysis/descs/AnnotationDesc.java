package me.curlpipesh.analysis.descs;

import lombok.Data;

/**
 * Description of an annotation
 */
@Data
public class AnnotationDesc {
    /**
     * The annotation's type description
     */
    private final String description;

    /**
     * Whether or not the annotation is visible
     */
    private final boolean visible;

    /**
     * Values stored in the annotation
     */
    private final Object[] values;

    public AnnotationDesc(String a, boolean b, Object[] c) {
        description = a;
        visible = b;
        values = c;
    }
}
