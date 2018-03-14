package uk.gov.dvsa.mot.framework.html;

import java.util.Map;

public class HtmlWriter {
    /**
     * Get a full html tag.
     *
     * @param tagName of the html tag
     * @param attributes to include in the tag
     * @param text the contents of the tag
     * @return the html tag with it's contents
     */
    public static String tag(String tagName, Map<String, String> attributes, String text) {
        StringBuilder output = new StringBuilder();

        output.append("<")
                .append(tagName);

        String collapsedAttributes = collapseAttributes(attributes);

        if (collapsedAttributes != null) {
            output.append(" ").append(collapsedAttributes);
        }

        output.append(">")
                .append(text)
                .append("</")
                .append(tagName)
                .append(">");

        return output.toString();
    }

    private static String collapseAttributes(Map<String, String> attributes) {
        if (attributes == null || attributes.size() == 0) {
            return null;
        }

        StringBuilder output = new StringBuilder();

        for (String key : attributes.keySet()) {
            output.append(key)
                    .append("=\"")
                    .append(attributes.get(key))
                    .append("\" ");
        }

        return output.toString();
    }
}
