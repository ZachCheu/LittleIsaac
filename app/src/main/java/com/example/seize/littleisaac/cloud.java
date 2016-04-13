package com.example.seize.littleisaac;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class cloud {
    private float x;
    private float y;
    private int height;
    private int width;
    private Bitmap cloud_render;


    public cloud(float x, float y, Bitmap cloud_render) {
        this.x = x;
        this.y = y;
        this.cloud_render = cloud_render;
    }

    public Bitmap getCloud_render() {
        return cloud_render;
    }

    public void setCloud_render(Bitmap cloud_render) {
        this.cloud_render = cloud_render;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.cloud_render, this.x, this.y, null);
    }
}
