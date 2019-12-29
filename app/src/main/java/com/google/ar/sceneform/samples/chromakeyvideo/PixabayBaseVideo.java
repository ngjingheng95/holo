package com.google.ar.sceneform.samples.chromakeyvideo;

// handles individual video types (large/medium/small/tiny) from Pixabay API

public class PixabayBaseVideo {
    private String url;
    private int width;
    private int height;
    private int size;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
