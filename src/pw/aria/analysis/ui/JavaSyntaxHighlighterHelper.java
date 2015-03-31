package pw.aria.analysis.ui;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Attempts to match Java keywords in a {@link javax.swing.JTextPane} in order
 * to apply syntax highlighting.
 */
public class JavaSyntaxHighlighterHelper {
    private static final String[] JAVA_KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float", "for", "goto",
            "if", "implements", "import", "instanceof", "int", "long",
            "native", "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try",
            "void", "volatile", "while", "false", "null", "true"
    };

    public static final String KEYWORD_REGEX;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String keyword : JAVA_KEYWORDS) {
            sb.append("\\b").append(keyword).append("\\b").append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        KEYWORD_REGEX = sb.toString();
    }

    public static void applyRegex(String regex, JTextPane pane, Color highlightColor) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pane.getText());
        while(matcher.find()) {
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                    StyleConstants.Foreground, highlightColor);
            pane.getStyledDocument()
                    .setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), aset, true);
        }
    }
}
