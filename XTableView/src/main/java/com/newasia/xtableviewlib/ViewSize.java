package com.newasia.xtableviewlib;

public class ViewSize
{
    private int width;
    private int height;

    public ViewSize()
    {
    }

    public ViewSize(int w,int h)
    {
        width = w;
        height = h;
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
}
