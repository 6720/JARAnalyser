package pw.aria.analysis.descs;

import lombok.Data;

/**
 * Description of a parameter
 */
@Data
@Deprecated
@SuppressWarnings("unused")
public class ParameterDesc {
    /**
     * Name of the parameter
     */
    private final String name;

    /**
     * Access modifier of the parameter
     */
    private final int accessLevel;

    public ParameterDesc(String a, int b) {
        name = a;
        accessLevel = b;
    }
}
