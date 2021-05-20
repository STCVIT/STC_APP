package com.mstc.mstcapp.model;

import java.io.Serializable;

public class DomainModel implements Serializable {
    private final String domain;
    private final int drawable;
    private final int color;

    public DomainModel(String domain, int drawable, int color) {
        this.domain = domain;
        this.drawable = drawable;
        this.color = color;
    }

    public String getDomain() {
        return domain;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getColor() {
        return color;
    }
}

