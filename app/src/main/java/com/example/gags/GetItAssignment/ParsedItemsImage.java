package com.example.gags.GetItAssignment;

import android.graphics.Bitmap;

/**
 * Created by gags on 28/07/15.
 */
public class ParsedItemsImage {
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public Bitmap getImage_bitmap() {
        return image_bitmap;
    }

    public void setImage_bitmap(Bitmap image_bitmap) {
        this.image_bitmap = image_bitmap;
    }

    private String label;
    private String image;
    private String web_url;
    private Bitmap image_bitmap;


}
