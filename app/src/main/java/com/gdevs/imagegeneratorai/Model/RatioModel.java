package com.gdevs.imagegeneratorai.Model;

public class RatioModel {
    private int iconResourceId;
    private String ratioText;

    public RatioModel(int iconResourceId, String ratioText) {
        this.iconResourceId = iconResourceId;
        this.ratioText = ratioText;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public String getRatioText() {
        return ratioText;
    }
}

