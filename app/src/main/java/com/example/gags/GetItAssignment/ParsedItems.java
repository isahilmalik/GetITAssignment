package com.example.gags.GetItAssignment;

import java.util.ArrayList;

/**
 * Created by gags on 28/07/15.
 */
public class ParsedItems {
    private String label;
    private String template;
    ArrayList<ParsedItemsImage> items;
   // private ParsedItemsImage items = new ;


    public ParsedItems(String label, String template, ArrayList<ParsedItemsImage> items) {
        this.items = new ArrayList<ParsedItemsImage>(items);
        this.label = label;
        this.template = template;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public ArrayList<ParsedItemsImage> getItems() {
        return items;
    }

    public void setItems(ArrayList<ParsedItemsImage> items) {
        this.items = items;
    }
}
