package pw.aria.analysis.descs;

import lombok.Data;

@Data
public class ParameterDesc {
    private final String name;
    private final int accessLevel;

    public ParameterDesc(String a, int b) {
        name = a;
        accessLevel = b;
    }
}
