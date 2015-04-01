package pw.aria.analysis.descs;

import lombok.Data;

/**
 * Description of an inner class
 */
@Data
public class InnerClassDesc {
    /**
     * Name of the inner class
     */
    private final String name;

    /**
     * Name of the enclosing class
     */
    private final String outerName;

    /**
     * Name of the inner class
     *
     * TODO: Same as {@link #name}?
     */
    private final String innerName;

    /**
     * Access modifier for the inner class
     */
    private final int accessLevel;

    public InnerClassDesc(String a, String b, String c, int d) {
        name = a;
        outerName = b;
        innerName = c;
        accessLevel = d;
    }
}
