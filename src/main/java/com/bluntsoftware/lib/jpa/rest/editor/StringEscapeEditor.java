package com.bluntsoftware.lib.jpa.rest.editor;

import org.apache.commons.lang.StringEscapeUtils;
import java.beans.PropertyEditorSupport;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander Mcknight
 * Date: 6/26/15
 * Time: 3:02 AM
 */
public class StringEscapeEditor extends PropertyEditorSupport {

    private boolean escapeHTML;

    private boolean escapeJavaScript;

    private boolean escapeSQL;

    public StringEscapeEditor() {
        super();
    }

    public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript,
                              boolean escapeSQL) {
        super();
        this.escapeHTML = escapeHTML;
        this.escapeJavaScript = escapeJavaScript;
        this.escapeSQL = escapeSQL;
    }

    public void setAsText(String text) {
        if (text == null) {
            setValue(null);
        } else {
            String value = text;
            if (escapeHTML) {
                value = StringEscapeUtils.escapeHtml(value);
            }
            if (escapeJavaScript) {
                value = StringEscapeUtils.escapeJavaScript(value);
            }
            if (escapeSQL) {
                value = StringEscapeUtils.escapeSql(value);
            }
            setValue(value);
        }
    }

    public String getAsText() {
        Object value = getValue();
        return (value != null ? value.toString() : "");
    }
}
