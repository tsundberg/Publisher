package se.somath.publisher.formatter;

import java.util.LinkedList;
import java.util.List;

public class HtmlEncoder {
    public List<String> encode(List<String> sourceCode) {
        List<String> result = new LinkedList<String>();

        for (String tmp : sourceCode) {
            tmp = tmp.replaceAll("&", "&amp;");
            tmp = tmp.replaceAll("<", "&lt;");
            tmp = tmp.replaceAll(">", "&gt;");
            tmp = tmp.replaceAll("'", "&apos;");
            tmp = tmp.replaceAll("\"", "&quot;");

            result.add(tmp);
        }

        return result;
    }
}
